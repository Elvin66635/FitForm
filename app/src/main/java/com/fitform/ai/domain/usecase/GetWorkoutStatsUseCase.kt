package com.fitform.ai.domain.usecase

import com.fitform.ai.domain.model.WorkoutStats
import com.fitform.ai.domain.repository.IWorkoutRepository

class GetWorkoutStatsUseCase(
    private val repository: IWorkoutRepository
) {
    operator fun invoke(): WorkoutStats = repository.getStats()
}





