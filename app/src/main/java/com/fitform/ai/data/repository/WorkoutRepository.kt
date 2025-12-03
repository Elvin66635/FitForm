package com.fitform.ai.data.repository

import com.fitform.ai.domain.model.WorkoutSession
import com.fitform.ai.domain.model.WorkoutStats
import com.fitform.ai.domain.repository.IWorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

class WorkoutRepository : IWorkoutRepository {
    
    private val _sessions = MutableStateFlow<List<WorkoutSession>>(emptyList())
    
    override fun getAllSessions(): Flow<List<WorkoutSession>> = _sessions
    
    override fun getSessionsByDate(date: LocalDate): List<WorkoutSession> {
        val startOfDay = date.atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC) * 1000
        val endOfDay = date.plusDays(1).atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC) * 1000
        return _sessions.value.filter { it.startTime in startOfDay until endOfDay }
    }
    
    override fun addSession(session: WorkoutSession) {
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

