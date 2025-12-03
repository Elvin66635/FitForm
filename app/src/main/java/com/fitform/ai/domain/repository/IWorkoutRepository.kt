package com.fitform.ai.domain.repository

import com.fitform.ai.domain.model.WorkoutSession
import com.fitform.ai.domain.model.WorkoutStats
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IWorkoutRepository {
    fun getAllSessions(): Flow<List<WorkoutSession>>
    fun getSessionsByDate(date: LocalDate): List<WorkoutSession>
    fun addSession(session: WorkoutSession)
    fun getStats(): WorkoutStats
}



