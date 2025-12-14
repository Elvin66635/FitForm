package com.fitform.ai.domain.usecase

import com.fitform.ai.domain.repository.IProgramRepository

class GetAllProgramsUseCase(
    private val repository: IProgramRepository
) {
    operator fun invoke() = repository.getAllPrograms()
}





