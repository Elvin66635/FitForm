package com.fitform.ai.domain.repository

import com.fitform.ai.domain.model.ExerciseCategory
import com.fitform.ai.domain.model.WorkoutProgram

interface IProgramRepository {
    fun getAllPrograms(): List<WorkoutProgram>
    fun getProgramById(id: String): WorkoutProgram?
    fun getProgramsByCategory(category: ExerciseCategory): List<WorkoutProgram>
    fun getFreePrograms(): List<WorkoutProgram>
}



