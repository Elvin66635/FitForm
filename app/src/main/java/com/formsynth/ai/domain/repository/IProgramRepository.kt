package com.formsynth.ai.domain.repository

import com.formsynth.ai.domain.model.ExerciseCategory
import com.formsynth.ai.domain.model.WorkoutProgram

interface IProgramRepository {
    fun getAllPrograms(): List<WorkoutProgram>
    fun getProgramById(id: String): WorkoutProgram?
    fun getProgramsByCategory(category: ExerciseCategory): List<WorkoutProgram>
    fun getFreePrograms(): List<WorkoutProgram>
}





