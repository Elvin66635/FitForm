package com.fitform.ai.ui.screens.programs

import androidx.lifecycle.ViewModel
import com.fitform.ai.domain.model.WorkoutProgram
import com.fitform.ai.domain.repository.IProgramRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgramsViewModel(
    private val programRepository: IProgramRepository
) : ViewModel() {
    
    private val _programs = MutableStateFlow<List<WorkoutProgram>>(emptyList())
    val programs: StateFlow<List<WorkoutProgram>> = _programs.asStateFlow()
    
    private val _freePrograms = MutableStateFlow<List<WorkoutProgram>>(emptyList())
    val freePrograms: StateFlow<List<WorkoutProgram>> = _freePrograms.asStateFlow()
    
    private val _premiumPrograms = MutableStateFlow<List<WorkoutProgram>>(emptyList())
    val premiumPrograms: StateFlow<List<WorkoutProgram>> = _premiumPrograms.asStateFlow()
    
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()
    
    init {
        loadPrograms()
    }
    
    private fun loadPrograms() {
        val all = programRepository.getAllPrograms()
        _programs.value = all
        _freePrograms.value = all.filter { !it.isPremium }
        _premiumPrograms.value = all.filter { it.isPremium }
    }
    
    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }
    
    fun getProgramById(id: String): WorkoutProgram? = programRepository.getProgramById(id)
}


