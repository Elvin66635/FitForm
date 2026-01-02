package com.formsynth.ai.domain.usecase

import com.formsynth.ai.domain.model.Achievement
import com.formsynth.ai.domain.repository.IAchievementRepository
import com.formsynth.ai.domain.repository.IWorkoutRepository

class CheckAchievementsUseCase(
    private val achievementRepository: IAchievementRepository,
    private val workoutRepository: IWorkoutRepository
) {
    suspend operator fun invoke(userId: String): List<Achievement> {
        val stats = workoutRepository.getStats()
        val statsMap = mapOf(
            "totalWorkouts" to stats.totalWorkouts,
            "totalReps" to 0, // TODO: Add to WorkoutStats
            "currentStreak" to stats.currentStreak,
            "totalCalories" to stats.totalCalories,
            "perfectFormCount" to 0, // TODO: Add to WorkoutStats
            "exerciseMastery" to 0,
            "completedPrograms" to 0,
            "weeklyWorkouts" to stats.thisWeekWorkouts
        )
        return achievementRepository.checkAndUnlockAchievements(userId, statsMap)
    }
}



