package com.fitform.ai.domain.usecase

import com.fitform.ai.domain.model.Exercise
import com.fitform.ai.domain.repository.IExerciseRepository

class GetExerciseByIdUseCase(
    private val repository: IExerciseRepository
) {
    operator fun invoke(id: String): Exercise? = repository.getExerciseById(id)
}



