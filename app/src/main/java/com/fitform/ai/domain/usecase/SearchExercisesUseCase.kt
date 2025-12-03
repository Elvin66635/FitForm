package com.fitform.ai.domain.usecase

import com.fitform.ai.domain.model.Difficulty
import com.fitform.ai.domain.model.Exercise
import com.fitform.ai.domain.model.ExerciseCategory
import com.fitform.ai.domain.repository.IExerciseRepository

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



