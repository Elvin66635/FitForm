package com.fitform.ai.domain.model

import java.util.UUID

data class Exercise(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val category: ExerciseCategory,
    val difficulty: Difficulty,
    val muscleGroups: List<MuscleGroup>,
    val calories: Int,
    val videoUrl: String? = null,
    val imageUrl: String? = null,
    val instructions: List<String> = emptyList(),
    val tips: List<String> = emptyList(),
    val isPremium: Boolean = false
)

enum class ExerciseCategory {
    STRENGTH,
    CARDIO,
    FLEXIBILITY,
    HIIT,
    YOGA,
    CALISTHENICS,
    CROSSFIT
}

enum class Difficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}

enum class MuscleGroup {
    CHEST,
    BACK,
    SHOULDERS,
    BICEPS,
    TRICEPS,
    FOREARMS,
    ABS,
    OBLIQUES,
    QUADRICEPS,
    HAMSTRINGS,
    GLUTES,
    CALVES,
    FULL_BODY,
    CORE
}

