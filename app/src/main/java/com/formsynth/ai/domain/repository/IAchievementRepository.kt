package com.formsynth.ai.domain.repository

import com.formsynth.ai.domain.model.Achievement
import com.formsynth.ai.domain.model.UserAchievement
import kotlinx.coroutines.flow.Flow

interface IAchievementRepository {
    suspend fun getAllAchievements(): List<Achievement>
    suspend fun getUserAchievements(userId: String): List<UserAchievement>
    fun getUserAchievementsFlow(userId: String): Flow<List<UserAchievement>>
    suspend fun unlockAchievement(userId: String, achievementId: String): Result<Unit>
    suspend fun updateAchievementProgress(userId: String, achievementId: String, progress: Int): Result<Unit>
    suspend fun checkAndUnlockAchievements(userId: String, stats: Map<String, Any>): List<Achievement>
}



