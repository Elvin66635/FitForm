package com.formsynth.ai.domain.usecase

import com.formsynth.ai.domain.model.User
import com.formsynth.ai.domain.repository.IAuthRepository

class SignUpUseCase(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        displayName: String
    ): Result<User> {
        return authRepository.signUpWithEmail(email, password, displayName)
    }
}



