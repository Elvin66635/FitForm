package com.fitform.ai.data.repository

import com.fitform.ai.domain.model.Achievement
import com.fitform.ai.domain.model.AchievementCategory
import com.fitform.ai.domain.model.AchievementRarity
import com.fitform.ai.domain.model.AchievementType
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Инициализатор достижений для Firestore
 * Вызывается один раз для создания базовых достижений
 */
object AchievementInitializer {
    
    private val defaultAchievements = listOf(
        Achievement(
            id = "first_workout",
            title = "Первая тренировка",
            description = "Завершите свою первую тренировку",
            icon = "🎯",
            type = AchievementType.TOTAL_WORKOUTS,
            requirement = 1,
            rarity = AchievementRarity.COMMON,
            category = AchievementCategory.MILESTONE
        ),
        Achievement(
            id = "week_warrior",
            title = "Воин недели",
            description = "Завершите 5 тренировок за неделю",
            icon = "🔥",
            type = AchievementType.WEEKLY_GOAL,
            requirement = 5,
            rarity = AchievementRarity.RARE,
            category = AchievementCategory.TRAINING
        ),
        Achievement(
            id = "streak_7",
            title = "Неделя подряд",
            description = "Тренируйтесь 7 дней подряд",
            icon = "💪",
            type = AchievementType.STREAK_DAYS,
            requirement = 7,
            rarity = AchievementRarity.RARE,
            category = AchievementCategory.PROGRESS
        ),
        Achievement(
            id = "perfect_form",
            title = "Идеальная техника",
            description = "Получите 100% точность в 10 тренировках",
            icon = "⭐",
            type = AchievementType.PERFECT_FORM,
            requirement = 10,
            rarity = AchievementRarity.EPIC,
            category = AchievementCategory.SKILL
        ),
        Achievement(
            id = "calorie_master",
            title = "Мастер калорий",
            description = "Сожгите 10,000 калорий",
            icon = "🔥",
            type = AchievementType.CALORIES_BURNED,
            requirement = 10000,
            rarity = AchievementRarity.EPIC,
            category = AchievementCategory.PROGRESS
        ),
        Achievement(
            id = "century_club",
            title = "Клуб сотни",
            description = "Завершите 100 тренировок",
            icon = "🏆",
            type = AchievementType.TOTAL_WORKOUTS,
            requirement = 100,
            rarity = AchievementRarity.LEGENDARY,
            category = AchievementCategory.MILESTONE
        )
    )
    
    suspend fun initializeAchievements() {
        val firestore = FirebaseFirestore.getInstance()
        val achievementsCollection = firestore.collection("achievements")
        
        defaultAchievements.forEach { achievement ->
            try {
                achievementsCollection.document(achievement.id)
                    .set(achievement)
                    .await()
            } catch (e: Exception) {
                // Игнорируем ошибки, если достижение уже существует
            }
        }
    }
}

