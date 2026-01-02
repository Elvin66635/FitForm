package com.formsynth.ai.domain.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String, // Emoji or icon name
    val type: AchievementType,
    val requirement: Int, // Required amount to unlock
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
    TOTAL_WORKOUTS,      // Total workout count
    TOTAL_REPS,          // Total repetitions
    STREAK_DAYS,         // Consecutive days streak
    PERFECT_FORM,        // Perfect form technique
    CALORIES_BURNED,     // Calories burned
    EXERCISE_MASTERY,    // Exercise mastery level
    PROGRAM_COMPLETION,  // Program completion
    WEEKLY_GOAL          // Weekly goal
}

enum class AchievementRarity {
    COMMON,      // Common rarity
    RARE,        // Rare rarity
    EPIC,        // Epic rarity
    LEGENDARY    // Legendary rarity
}

enum class AchievementCategory {
    TRAINING,    // Training achievements
    PROGRESS,    // Progress achievements
    SKILL,       // Skill achievements
    MILESTONE    // Milestone achievements
}



