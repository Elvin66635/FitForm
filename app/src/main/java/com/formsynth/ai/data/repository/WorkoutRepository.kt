package com.formsynth.ai.data.repository

import com.formsynth.ai.domain.model.WorkoutSession
import com.formsynth.ai.domain.model.WorkoutStats
import com.formsynth.ai.domain.repository.IWorkoutRepository
// Temporarily disabled until Firebase is set up
// import com.google.firebase.auth.FirebaseAuth
// import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
// import kotlinx.coroutines.channels.awaitClose
// import kotlinx.coroutines.flow.callbackFlow
// import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class WorkoutRepository(
    // Temporarily disabled until Firebase is set up
    // private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    // private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : IWorkoutRepository {
    
    private val _sessions = MutableStateFlow<List<WorkoutSession>>(emptyList())
    
    // Temporarily disabled until Firebase is set up
    // private val workoutsCollection = firestore.collection("workout_sessions")
    
    override fun getAllSessions(): Flow<List<WorkoutSession>> {
        // TODO: Implement Firestore sync when Firebase is set up
        // For now, return local storage
        return _sessions
    }
    
    override fun getSessionsByDate(date: LocalDate): List<WorkoutSession> {
        val startOfDay = date.atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC) * 1000
        val endOfDay = date.plusDays(1).atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC) * 1000
        return _sessions.value.filter { it.startTime in startOfDay until endOfDay }
    }
    
    override fun addSession(session: WorkoutSession) {
        // TODO: Implement Firestore sync when Firebase is set up
        // For now, update local storage only
        _sessions.value = _sessions.value + session
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
