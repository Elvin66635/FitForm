package com.fitform.ai.domain.model

import java.util.UUID

data class WorkoutProgram(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val category: ExerciseCategory,
    val difficulty: Difficulty,
    val durationWeeks: Int,
    val workoutsPerWeek: Int,
    val workouts: List<Workout>,
    val imageUrl: String? = null,
    val isPremium: Boolean = false,
    val isCustom: Boolean = false
)

data class Workout(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val dayOfWeek: Int? = null,
    val exercises: List<WorkoutExercise>,
    val estimatedDuration: Int,
    val restBetweenExercises: Int = 60
)

data class WorkoutExercise(
    val exerciseId: String,
    val sets: Int,
    val reps: Int? = null,
    val duration: Int? = null,
    val restBetweenSets: Int = 45,
    val weight: Float? = null,
    val notes: String? = null
)

data class WorkoutSession(
    val id: String = UUID.randomUUID().toString(),
    val programId: String?,
    val workoutId: String?,
    val startTime: Long,
    val endTime: Long? = null,
    val exerciseResults: List<ExerciseResult> = emptyList(),
    val totalScore: Int = 0,
    val caloriesBurned: Int = 0,
    val notes: String? = null
)

data class ExerciseResult(
    val exerciseId: String,
    val completedSets: Int,
    val completedReps: List<Int>,
    val averageScore: Float,
    val duration: Int
)

