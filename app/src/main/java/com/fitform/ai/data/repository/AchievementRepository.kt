package com.fitform.ai.data.repository

import com.fitform.ai.domain.model.Achievement
import com.fitform.ai.domain.model.AchievementType
import com.fitform.ai.domain.model.UserAchievement
import com.fitform.ai.domain.repository.IAchievementRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AchievementRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IAchievementRepository {
    
    private val achievementsCollection = firestore.collection("achievements")
    private val userAchievementsCollection = firestore.collection("user_achievements")
    
    override suspend fun getAllAchievements(): List<Achievement> {
        return try {
            val snapshot = achievementsCollection.get().await()
            snapshot.documents.mapNotNull { it.toObject(Achievement::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override suspend fun getUserAchievements(userId: String): List<UserAchievement> {
        return try {
            val snapshot = userAchievementsCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(UserAchievement::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override fun getUserAchievementsFlow(userId: String): Flow<List<UserAchievement>> = callbackFlow {
        val listenerRegistration = userAchievementsCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val achievements = snapshot?.documents?.mapNotNull { 
                    it.toObject(UserAchievement::class.java) 
                } ?: emptyList()
                trySend(achievements)
            }
        awaitClose { listenerRegistration.remove() }
    }
    
    override suspend fun unlockAchievement(
        userId: String,
        achievementId: String
    ): Result<Unit> {
        return try {
            val userAchievement = UserAchievement(
                achievementId = achievementId,
                userId = userId,
                unlockedAt = System.currentTimeMillis(),
                isUnlocked = true
            )
            userAchievementsCollection
                .document("${userId}_$achievementId")
                .set(userAchievement)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateAchievementProgress(
        userId: String,
        achievementId: String,
        progress: Int
    ): Result<Unit> {
        return try {
            val docRef = userAchievementsCollection.document("${userId}_$achievementId")
            val doc = docRef.get().await()
            
            if (doc.exists()) {
                docRef.update("progress", progress).await()
            } else {
                val userAchievement = UserAchievement(
                    achievementId = achievementId,
                    userId = userId,
                    progress = progress,
                    isUnlocked = false
                )
                docRef.set(userAchievement).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun checkAndUnlockAchievements(
        userId: String,
        stats: Map<String, Any>
    ): List<Achievement> {
        val unlockedAchievements = mutableListOf<Achievement>()
        
        try {
            val allAchievements = getAllAchievements()
            val userAchievements = getUserAchievements(userId)
            val userAchievementMap = userAchievements.associateBy { it.achievementId }
            
            for (achievement in allAchievements) {
                val userAchievement = userAchievementMap[achievement.id]
                
                if (userAchievement?.isUnlocked == true) {
                    continue
                }
                
                val currentProgress = when (achievement.type) {
                    AchievementType.TOTAL_WORKOUTS -> {
                        (stats["totalWorkouts"] as? Int) ?: 0
                    }
                    AchievementType.TOTAL_REPS -> {
                        (stats["totalReps"] as? Int) ?: 0
                    }
                    AchievementType.STREAK_DAYS -> {
                        (stats["currentStreak"] as? Int) ?: 0
                    }
                    AchievementType.CALORIES_BURNED -> {
                        (stats["totalCalories"] as? Int) ?: 0
                    }
                    AchievementType.PERFECT_FORM -> {
                        (stats["perfectFormCount"] as? Int) ?: 0
                    }
                    AchievementType.EXERCISE_MASTERY -> {
                        (stats["exerciseMastery"] as? Int) ?: 0
                    }
                    AchievementType.PROGRAM_COMPLETION -> {
                        (stats["completedPrograms"] as? Int) ?: 0
                    }
                    AchievementType.WEEKLY_GOAL -> {
                        (stats["weeklyWorkouts"] as? Int) ?: 0
                    }
                }
                
                // Обновляем прогресс
                updateAchievementProgress(userId, achievement.id, currentProgress)
                
                // Проверяем, достигнута ли цель
                if (currentProgress >= achievement.requirement) {
                    unlockAchievement(userId, achievement.id)
                    unlockedAchievements.add(achievement)
                }
            }
        } catch (e: Exception) {
            // Обработка ошибок
        }
        
        return unlockedAchievements
    }
}



