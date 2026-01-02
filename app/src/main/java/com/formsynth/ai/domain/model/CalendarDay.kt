package com.formsynth.ai.domain.model

import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean = true,
    val hasWorkout: Boolean = false,
    val workoutCount: Int = 0,
    val totalCalories: Int = 0,
    val workoutSessions: List<WorkoutSession> = emptyList(),
    val isToday: Boolean = false,
    val isSelected: Boolean = false
)
