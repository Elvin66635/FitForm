package com.formsynth.ai.ui.screens.exercise

import androidx.lifecycle.ViewModel
import com.formsynth.ai.domain.model.Difficulty
import com.formsynth.ai.domain.model.Exercise
import com.formsynth.ai.domain.model.ExerciseCategory
import com.formsynth.ai.domain.usecase.SearchExercisesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExerciseListViewModel(
    private val searchExercisesUseCase: SearchExercisesUseCase
) : ViewModel() {
    
    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<ExerciseCategory?>(null)
    val selectedCategory: StateFlow<ExerciseCategory?> = _selectedCategory.asStateFlow()
    
    private val _selectedDifficulty = MutableStateFlow<Difficulty?>(null)
    val selectedDifficulty: StateFlow<Difficulty?> = _selectedDifficulty.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    init {
        loadExercises()
    }
    
    private fun loadExercises() {
        _exercises.value = applyFilters()
    }
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        _exercises.value = applyFilters()
    }
    
    fun setCategory(category: ExerciseCategory?) {
        _selectedCategory.value = category
        _exercises.value = applyFilters()
    }
    
    fun setDifficulty(difficulty: Difficulty?) {
        _selectedDifficulty.value = difficulty
        _exercises.value = applyFilters()
    }
    
    fun clearFilters() {
        _selectedCategory.value = null
        _selectedDifficulty.value = null
        _searchQuery.value = ""
        _exercises.value = applyFilters()
    }
    
    private fun applyFilters(): List<Exercise> {
        return searchExercisesUseCase(
            query = _searchQuery.value,
            category = _selectedCategory.value,
            difficulty = _selectedDifficulty.value
        )
    }
    
    fun getCategories(): List<ExerciseCategory> = ExerciseCategory.entries
    
    fun getDifficulties(): List<Difficulty> = Difficulty.entries
}


