package com.formsynth.ai.domain.usecase

import com.formsynth.ai.domain.model.Exercise
import com.formsynth.ai.domain.repository.IExerciseRepository

class GetExerciseByIdUseCase(
    private val repository: IExerciseRepository
) {
    operator fun invoke(id: String): Exercise? = repository.getExerciseById(id)
}





