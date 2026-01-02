package com.formsynth.ai.domain.usecase

import com.formsynth.ai.domain.model.Difficulty
import com.formsynth.ai.domain.model.Exercise
import com.formsynth.ai.domain.model.ExerciseCategory
import com.formsynth.ai.domain.repository.IExerciseRepository

class SearchExercisesUseCase(
    private val repository: IExerciseRepository
) {
    operator fun invoke(
        query: String = "",
        category: ExerciseCategory? = null,
        difficulty: Difficulty? = null
    ): List<Exercise> {
        var exercises = repository.getAllExercises()
        
        if (query.isNotBlank()) {
            exercises = repository.searchExercises(query)
        }
        
        if (category != null) {
            exercises = exercises.filter { it.category == category }
        }
        
        if (difficulty != null) {
            exercises = exercises.filter { it.difficulty == difficulty }
        }
        
        return exercises
    }
}





