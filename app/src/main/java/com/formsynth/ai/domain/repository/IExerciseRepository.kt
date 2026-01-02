package com.formsynth.ai.domain.repository

import com.formsynth.ai.domain.model.Difficulty
import com.formsynth.ai.domain.model.Exercise
import com.formsynth.ai.domain.model.ExerciseCategory
import com.formsynth.ai.domain.model.MuscleGroup

interface IExerciseRepository {
    fun getAllExercises(): List<Exercise>
    fun getExerciseById(id: String): Exercise?
    fun getExercisesByCategory(category: ExerciseCategory): List<Exercise>
    fun getExercisesByMuscle(muscle: MuscleGroup): List<Exercise>
    fun getExercisesByDifficulty(difficulty: Difficulty): List<Exercise>
    fun searchExercises(query: String): List<Exercise>
    fun getFreeExercises(): List<Exercise>
}





