package com.formsynth.ai.ui.screens.programs

import androidx.lifecycle.ViewModel
import com.formsynth.ai.domain.model.Exercise
import com.formsynth.ai.domain.model.WorkoutProgram
import com.formsynth.ai.domain.repository.IExerciseRepository
import com.formsynth.ai.domain.repository.IProgramRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgramDetailViewModel(
    private val programId: String,
    private val programRepository: IProgramRepository,
    private val exerciseRepository: IExerciseRepository
) : ViewModel() {
    
    private val _program = MutableStateFlow<WorkoutProgram?>(null)
    val program: StateFlow<WorkoutProgram?> = _program.asStateFlow()
    
    private val _exercises = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()
    
    init {
        loadProgram()
    }
    
    private fun loadProgram() {
        val prog = programRepository.getProgramById(programId)
        _program.value = prog
        
        // Get all unique exercise IDs from all workouts
        if (prog != null) {
            val exerciseIds = prog.workouts
                .flatMap { it.exercises }
                .map { it.exerciseId }
                .distinct()
            
            // Load exercise objects
            _exercises.value = exerciseIds.mapNotNull { id ->
                exerciseRepository.getExerciseById(id)
            }
        }
    }
}










