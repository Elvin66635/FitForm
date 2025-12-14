package com.fitform.ai.domain.repository

import com.fitform.ai.domain.model.Difficulty
import com.fitform.ai.domain.model.Exercise
import com.fitform.ai.domain.model.ExerciseCategory
import com.fitform.ai.domain.model.MuscleGroup

interface IExerciseRepository {
    fun getAllExercises(): List<Exercise>
    fun getExerciseById(id: String): Exercise?
    fun getExercisesByCategory(category: ExerciseCategory): List<Exercise>
    fun getExercisesByMuscle(muscle: MuscleGroup): List<Exercise>
    fun getExercisesByDifficulty(difficulty: Difficulty): List<Exercise>
    fun searchExercises(query: String): List<Exercise>
    fun getFreeExercises(): List<Exercise>
}





