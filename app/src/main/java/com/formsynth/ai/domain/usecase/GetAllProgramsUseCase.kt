package com.formsynth.ai.domain.usecase

import com.formsynth.ai.domain.repository.IProgramRepository

class GetAllProgramsUseCase(
    private val repository: IProgramRepository
) {
    operator fun invoke() = repository.getAllPrograms()
}





