package com.fitform.ai.domain.usecase

import com.fitform.ai.domain.model.Achievement
import com.fitform.ai.domain.repository.IAchievementRepository
import com.fitform.ai.domain.repository.IWorkoutRepository

class CheckAchievementsUseCase(
    private val achievementRepository: IAchievementRepository,
    private val workoutRepository: IWorkoutRepository
) {
    suspend operator fun invoke(userId: String): List<Achievement> {
        val stats = workoutRepository.getStats()
        val statsMap = mapOf(
            "totalWorkouts" to stats.totalWorkouts,
            "totalReps" to 0, // Можно добавить в WorkoutStats
            "currentStreak" to stats.currentStreak,
            "totalCalories" to stats.totalCalories,
            "perfectFormCount" to 0, // Можно добавить в WorkoutStats
            "exerciseMastery" to 0,
            "completedPrograms" to 0,
            "weeklyWorkouts" to stats.thisWeekWorkouts
        )
        return achievementRepository.checkAndUnlockAchievements(userId, statsMap)
    }
}



