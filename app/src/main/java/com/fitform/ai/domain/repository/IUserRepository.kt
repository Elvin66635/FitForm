package com.fitform.ai.domain.repository

import com.fitform.ai.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getUserProfile(userId: String): UserProfile?
    suspend fun saveUserProfile(profile: UserProfile): Result<Unit>
    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit>
    fun getUserProfileFlow(userId: String): Flow<UserProfile?>
}



