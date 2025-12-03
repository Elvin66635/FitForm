package com.fitform.ai.domain.model

data class WorkoutStats(
    val totalWorkouts: Int = 0,
    val totalCalories: Int = 0,
    val totalMinutes: Int = 0,
    val thisWeekWorkouts: Int = 0,
    val thisMonthWorkouts: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0
)




