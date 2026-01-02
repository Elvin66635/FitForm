package com.formsynth.ai.domain.model

data class User(
    val id: String,
    val email: String,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
)

data class UserProfile(
    val userId: String,
    val weight: Float? = null,
    val height: Float? = null,
    val age: Int? = null,
    val fitnessGoal: FitnessGoal? = null,
    val activityLevel: ActivityLevel? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class FitnessGoal {
    WEIGHT_LOSS,
    MUSCLE_GAIN,
    ENDURANCE,
    FLEXIBILITY,
    GENERAL_FITNESS
}

enum class ActivityLevel {
    SEDENTARY,
    LIGHTLY_ACTIVE,
    MODERATELY_ACTIVE,
    VERY_ACTIVE,
    EXTRA_ACTIVE
}



