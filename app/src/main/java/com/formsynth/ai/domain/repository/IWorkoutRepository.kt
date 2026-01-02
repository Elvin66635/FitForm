package com.formsynth.ai.domain.repository

import com.formsynth.ai.domain.model.WorkoutSession
import com.formsynth.ai.domain.model.WorkoutStats
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface IWorkoutRepository {
    fun getAllSessions(): Flow<List<WorkoutSession>>
    fun getSessionsByDate(date: LocalDate): List<WorkoutSession>
    fun addSession(session: WorkoutSession)
    fun getStats(): WorkoutStats
}





