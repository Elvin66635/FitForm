package com.fitform.ai.domain.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String, // Emoji или название иконки
    val type: AchievementType,
    val requirement: Int, // Количество для получения
    val rarity: AchievementRarity = AchievementRarity.COMMON,
    val category: AchievementCategory
)

data class UserAchievement(
    val achievementId: String,
    val userId: String,
    val unlockedAt: Long = System.currentTimeMillis(),
    val progress: Int = 0,
    val isUnlocked: Boolean = false
)

enum class AchievementType {
    TOTAL_WORKOUTS,           // Общее количество тренировок
    TOTAL_REPS,               // Общее количество повторений
    STREAK_DAYS,              // Серия дней подряд
    PERFECT_FORM,             // Идеальная техника
    CALORIES_BURNED,          // Сожжено калорий
    EXERCISE_MASTERY,         // Мастерство в упражнении
    PROGRAM_COMPLETION,       // Завершение программы
    WEEKLY_GOAL               // Недельная цель
}

enum class AchievementRarity {
    COMMON,      // Обычное
    RARE,        // Редкое
    EPIC,        // Эпическое
    LEGENDARY    // Легендарное
}

enum class AchievementCategory {
    TRAINING,    // Тренировки
    PROGRESS,    // Прогресс
    SKILL,       // Навыки
    MILESTONE    // Вехи
}



