package com.fitform.ai.domain.usecase

import com.fitform.ai.domain.model.User
import com.fitform.ai.domain.repository.IAuthRepository

class SignInUseCase(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return authRepository.signInWithEmail(email, password)
    }
}



