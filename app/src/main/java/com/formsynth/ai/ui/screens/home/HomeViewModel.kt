package com.formsynth.ai.ui.screens.home

import androidx.lifecycle.ViewModel
import com.formsynth.ai.domain.model.Exercise
import com.formsynth.ai.domain.model.WorkoutProgram
import com.formsynth.ai.domain.model.WorkoutStats
import com.formsynth.ai.domain.repository.IExerciseRepository
import com.formsynth.ai.domain.repository.IProgramRepository
import com.formsynth.ai.domain.usecase.GetWorkoutStatsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val exerciseRepository: IExerciseRepository,
    private val programRepository: IProgramRepository,
    private val getWorkoutStatsUseCase: GetWorkoutStatsUseCase
) : ViewModel() {
    
    private val _stats = MutableStateFlow(WorkoutStats())
    val stats: StateFlow<WorkoutStats> = _stats.asStateFlow()
    
    private val _recommendedExercises = MutableStateFlow<List<Exercise>>(emptyList())
    val recommendedExercises: StateFlow<List<Exercise>> = _recommendedExercises.asStateFlow()
    
    private val _programs = MutableStateFlow<List<WorkoutProgram>>(emptyList())
    val programs: StateFlow<List<WorkoutProgram>> = _programs.asStateFlow()
    
    init {
        loadData()
    }
    
    private fun loadData() {
        _recommendedExercises.value = exerciseRepository.getAllExercises().take(6)
        _programs.value = programRepository.getFreePrograms().take(3)
        _stats.value = getWorkoutStatsUseCase()
    }
    
    fun refreshStats() {
        _stats.value = getWorkoutStatsUseCase()
    }
}

