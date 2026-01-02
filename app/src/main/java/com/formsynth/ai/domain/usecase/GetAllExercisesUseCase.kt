package com.formsynth.ai.domain.usecase

import com.formsynth.ai.domain.repository.IExerciseRepository

class GetAllExercisesUseCase(
    private val repository: IExerciseRepository
) {
    operator fun invoke() = repository.getAllExercises()
}





