package com.fitform.ai.domain.usecase

import com.fitform.ai.domain.repository.IExerciseRepository

class GetAllExercisesUseCase(
    private val repository: IExerciseRepository
) {
    operator fun invoke() = repository.getAllExercises()
}



