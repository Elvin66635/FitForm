package com.formsynth.ai.domain.usecase

import com.formsynth.ai.domain.model.WorkoutSession
import com.formsynth.ai.domain.repository.IWorkoutRepository

class SaveWorkoutSessionUseCase(
    private val repository: IWorkoutRepository
) {
    operator fun invoke(session: WorkoutSession) {
        repository.addSession(session)
    }
}





