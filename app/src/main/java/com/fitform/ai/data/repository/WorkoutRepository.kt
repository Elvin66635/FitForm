package com.fitform.ai.data.repository

import com.fitform.ai.domain.model.WorkoutSession
import com.fitform.ai.domain.model.WorkoutStats
import com.fitform.ai.domain.repository.IWorkoutRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class WorkoutRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : IWorkoutRepository {
    
    private val _sessions = MutableStateFlow<List<WorkoutSession>>(emptyList())
    private val workoutsCollection = firestore.collection("workout_sessions")
    
    override fun getAllSessions(): Flow<List<WorkoutSession>> {
        val currentUser = firebaseAuth.currentUser
        return if (currentUser != null) {
            // Синхронизация с Firebase для авторизованных пользователей
            callbackFlow {
                val listenerRegistration = workoutsCollection
                    .whereEqualTo("userId", currentUser.uid)
                    .orderBy("startTime", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            trySend(_sessions.value)
                            return@addSnapshotListener
                        }
                        val sessions = snapshot?.documents?.mapNotNull { 
                            it.toObject(WorkoutSession::class.java) 
                        } ?: emptyList()
                        _sessions.value = sessions
                        trySend(sessions)
                    }
                awaitClose { listenerRegistration.remove() }
            }
        } else {
            // Локальное хранилище для неавторизованных пользователей
            _sessions
        }
    }
    
    override fun getSessionsByDate(date: LocalDate): List<WorkoutSession> {
        val startOfDay = date.atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC) * 1000
        val endOfDay = date.plusDays(1).atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC) * 1000
        return _sessions.value.filter { it.startTime in startOfDay until endOfDay }
    }
    
    override fun addSession(session: WorkoutSession) {
        val currentUser = firebaseAuth.currentUser
        val sessionWithUserId = session.copy(userId = currentUser?.uid)
        
        // Обновляем локальное хранилище
        _sessions.value = _sessions.value + sessionWithUserId
        
        // Синхронизируем с Firebase для авторизованных пользователей
        if (currentUser != null) {
            try {
                workoutsCollection.document(sessionWithUserId.id)
                    .set(sessionWithUserId)
                    .addOnFailureListener { e ->
                        // В случае ошибки можно добавить в очередь для повторной попытки
                    }
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }
    
    override fun getStats(): WorkoutStats {
        val sessions = _sessions.value
        val now = LocalDate.now()
        val startOfWeek = now.minusDays(now.dayOfWeek.value.toLong() - 1)
        val startOfMonth = now.withDayOfMonth(1)
        
        return WorkoutStats(
            totalWorkouts = sessions.size,
            totalCalories = sessions.sumOf { it.caloriesBurned },
            totalMinutes = sessions.sumOf { 
                ((it.endTime ?: it.startTime) - it.startTime).toInt() / 60000 
            },
            thisWeekWorkouts = sessions.count { 
                val sessionDate = java.time.Instant.ofEpochMilli(it.startTime)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate()
                !sessionDate.isBefore(startOfWeek)
            },
            thisMonthWorkouts = sessions.count {
                val sessionDate = java.time.Instant.ofEpochMilli(it.startTime)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate()
                !sessionDate.isBefore(startOfMonth)
            }
        )
    }
}

