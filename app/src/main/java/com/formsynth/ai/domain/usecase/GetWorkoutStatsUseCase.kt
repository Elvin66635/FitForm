package com.formsynth.ai.domain.usecase

import com.formsynth.ai.domain.model.WorkoutStats
import com.formsynth.ai.domain.repository.IWorkoutRepository

class GetWorkoutStatsUseCase(
    private val repository: IWorkoutRepository
) {
    operator fun invoke(): WorkoutStats = repository.getStats()
}





