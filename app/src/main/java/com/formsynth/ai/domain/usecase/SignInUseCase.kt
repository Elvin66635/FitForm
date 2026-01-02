package com.formsynth.ai.domain.usecase

import com.formsynth.ai.domain.model.User
import com.formsynth.ai.domain.repository.IAuthRepository

class SignInUseCase(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return authRepository.signInWithEmail(email, password)
    }
}



