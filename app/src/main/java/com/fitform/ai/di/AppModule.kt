package com.fitform.ai.di

import com.fitform.ai.data.repository.ExerciseRepository
import com.fitform.ai.data.repository.ProgramRepository
import com.fitform.ai.data.repository.WorkoutRepository
import com.fitform.ai.domain.repository.IExerciseRepository
import com.fitform.ai.domain.repository.IProgramRepository
import com.fitform.ai.domain.repository.IWorkoutRepository
import com.fitform.ai.domain.usecase.GetAllExercisesUseCase
import com.fitform.ai.domain.usecase.GetAllProgramsUseCase
import com.fitform.ai.domain.usecase.GetExerciseByIdUseCase
import com.fitform.ai.domain.usecase.GetWorkoutStatsUseCase
import com.fitform.ai.domain.usecase.SaveWorkoutSessionUseCase
import com.fitform.ai.domain.usecase.SearchExercisesUseCase
import com.fitform.ai.ui.screens.calendar.CalendarViewModel
import com.fitform.ai.ui.screens.exercise.ExerciseListViewModel
import com.fitform.ai.ui.screens.home.HomeViewModel
import com.fitform.ai.ui.screens.programs.ProgramDetailViewModel
import com.fitform.ai.ui.screens.programs.ProgramsViewModel
import com.fitform.ai.ui.screens.workout.WorkoutViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<IExerciseRepository> { ExerciseRepository(androidContext()) }
    single<IProgramRepository> { ProgramRepository(androidContext()) }
    single<IWorkoutRepository> { WorkoutRepository() }
    
    single { GetAllExercisesUseCase(get()) }
    single { GetExerciseByIdUseCase(get()) }
    single { SearchExercisesUseCase(get()) }
    single { GetAllProgramsUseCase(get()) }
    single { GetWorkoutStatsUseCase(get()) }
    single { SaveWorkoutSessionUseCase(get()) }
    
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ExerciseListViewModel(get()) }
    viewModel { ProgramsViewModel(get()) }
    viewModel { params -> ProgramDetailViewModel(params.get(), get(), get()) }
    viewModel { CalendarViewModel(get()) }
    viewModel { params -> WorkoutViewModel(params.get(), get(), get()) }
}

