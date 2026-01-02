package com.formsynth.ai.domain.repository

import com.formsynth.ai.domain.model.User
// Temporarily disabled until Firebase is set up
// import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    val currentUser: Flow<User?>
    val isAuthenticated: Flow<Boolean>
    
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signUpWithEmail(email: String, password: String, displayName: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signOut()
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun updateDisplayName(name: String): Result<Unit>
    // Temporarily disabled until Firebase is set up
    // fun getCurrentFirebaseUser(): FirebaseUser?
    fun getCurrentFirebaseUser(): Any? // Placeholder
}



