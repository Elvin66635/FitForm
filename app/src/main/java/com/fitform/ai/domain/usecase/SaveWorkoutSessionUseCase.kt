package com.fitform.ai.domain.usecase

import com.fitform.ai.domain.model.WorkoutSession
import com.fitform.ai.domain.repository.IWorkoutRepository

class SaveWorkoutSessionUseCase(
    private val repository: IWorkoutRepository
) {
    operator fun invoke(session: WorkoutSession) {
        repository.addSession(session)
    }
}





