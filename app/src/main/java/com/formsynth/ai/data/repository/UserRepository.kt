package com.formsynth.ai.data.repository

import com.formsynth.ai.domain.model.UserProfile
import com.formsynth.ai.domain.repository.IUserRepository
// Temporarily disabled until Firebase is set up
// import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
// import kotlinx.coroutines.channels.awaitClose
// import kotlinx.coroutines.flow.callbackFlow
// import kotlinx.coroutines.tasks.await

class UserRepository(
    // Temporarily disabled until Firebase is set up
    // private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : IUserRepository {
    
    // Temporary in-memory storage until Firebase is set up
    private val _userProfiles = MutableStateFlow<Map<String, UserProfile>>(emptyMap())
    
    // Temporarily disabled until Firebase is set up
    // private val usersCollection = firestore.collection("users")
    
    override suspend fun getUserProfile(userId: String): UserProfile? {
        // TODO: Implement Firestore when Firebase is set up
        return _userProfiles.value[userId]
    }
    
    override suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        // TODO: Implement Firestore when Firebase is set up
        _userProfiles.value = _userProfiles.value + (profile.userId to profile.copy(updatedAt = System.currentTimeMillis()))
        return Result.success(Unit)
    }
    
    override suspend fun updateUserProfile(
        userId: String,
        updates: Map<String, Any>
    ): Result<Unit> {
        // TODO: Implement Firestore when Firebase is set up
        val existing = _userProfiles.value[userId]
        if (existing != null) {
            // Simple update - in real implementation, merge updates properly
            _userProfiles.value = _userProfiles.value + (userId to existing.copy(updatedAt = System.currentTimeMillis()))
        }
        return Result.success(Unit)
    }
    
    override fun getUserProfileFlow(userId: String): Flow<UserProfile?> {
        // TODO: Implement Firestore when Firebase is set up
        return MutableStateFlow(_userProfiles.value[userId])
    }
}
