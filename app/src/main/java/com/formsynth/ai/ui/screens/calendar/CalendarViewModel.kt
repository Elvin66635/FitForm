package com.formsynth.ai.ui.screens.calendar

import androidx.lifecycle.ViewModel
import com.formsynth.ai.domain.model.CalendarDay
import com.formsynth.ai.domain.model.WorkoutSession
import com.formsynth.ai.domain.repository.IWorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.YearMonth

class CalendarViewModel(
    private val workoutRepository: IWorkoutRepository
) : ViewModel() {
    
    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()
    
    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()
    
    private val _calendarDays = MutableStateFlow<List<CalendarDay>>(emptyList())
    val calendarDays: StateFlow<List<CalendarDay>> = _calendarDays.asStateFlow()
    
    private val _selectedDateWorkouts = MutableStateFlow<List<WorkoutSession>>(emptyList())
    val selectedDateWorkouts: StateFlow<List<WorkoutSession>> = _selectedDateWorkouts.asStateFlow()
    
    init {
        loadCalendarDays()
    }
    
    fun previousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
        loadCalendarDays()
    }
    
    fun nextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
        loadCalendarDays()
    }
    
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        _selectedDateWorkouts.value = workoutRepository.getSessionsByDate(date)
    }
    
    private fun loadCalendarDays() {
        val month = _currentMonth.value
        val days = mutableListOf<CalendarDay>()
        
        // First day of month
        val firstDay = month.atDay(1)
        val firstDayOfWeek = firstDay.dayOfWeek.value // 1 = Monday
        
        // Add empty days at start (if month doesn't start on Monday)
        repeat(firstDayOfWeek - 1) {
            days.add(CalendarDay(
                date = firstDay.minusDays((firstDayOfWeek - 1 - it).toLong()),
                isCurrentMonth = false
            ))
        }
        
        // Add days of current month
        for (day in 1..month.lengthOfMonth()) {
            val date = month.atDay(day)
            val workouts = workoutRepository.getSessionsByDate(date)
            days.add(CalendarDay(
                date = date,
                isCurrentMonth = true,
                hasWorkout = workouts.isNotEmpty(),
                workoutCount = workouts.size,
                totalCalories = workouts.sumOf { it.caloriesBurned },
                isToday = date == LocalDate.now()
            ))
        }
        
        // Add remaining days to complete the grid (6 rows x 7 days = 42)
        val remainingDays = 42 - days.size
        val lastDay = month.atEndOfMonth()
        repeat(remainingDays) {
            days.add(CalendarDay(
                date = lastDay.plusDays((it + 1).toLong()),
                isCurrentMonth = false
            ))
        }
        
        _calendarDays.value = days
    }
}


