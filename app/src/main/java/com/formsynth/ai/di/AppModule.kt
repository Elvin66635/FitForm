package com.formsynth.ai.di

import com.formsynth.ai.data.repository.AchievementRepository
import com.formsynth.ai.data.repository.AuthRepository
import com.formsynth.ai.data.repository.ExerciseRepository
import com.formsynth.ai.data.repository.ProgramRepository
import com.formsynth.ai.data.repository.UserRepository
import com.formsynth.ai.data.repository.WorkoutRepository
import com.formsynth.ai.domain.repository.IAchievementRepository
import com.formsynth.ai.domain.repository.IAuthRepository
import com.formsynth.ai.domain.repository.IExerciseRepository
import com.formsynth.ai.domain.repository.IProgramRepository
import com.formsynth.ai.domain.repository.IUserRepository
import com.formsynth.ai.domain.repository.IWorkoutRepository
import com.formsynth.ai.domain.usecase.CheckAchievementsUseCase
import com.formsynth.ai.domain.usecase.GetAllExercisesUseCase
import com.formsynth.ai.domain.usecase.GetAllProgramsUseCase
import com.formsynth.ai.domain.usecase.GetExerciseByIdUseCase
import com.formsynth.ai.domain.usecase.GetWorkoutStatsUseCase
import com.formsynth.ai.domain.usecase.SaveWorkoutSessionUseCase
import com.formsynth.ai.domain.usecase.SearchExercisesUseCase
import com.formsynth.ai.domain.usecase.SignInUseCase
import com.formsynth.ai.domain.usecase.SignUpUseCase
import com.formsynth.ai.ui.screens.auth.AuthViewModel
import com.formsynth.ai.ui.screens.calendar.CalendarViewModel
import com.formsynth.ai.ui.screens.exercise.ExerciseListViewModel
import com.formsynth.ai.ui.screens.home.HomeViewModel
import com.formsynth.ai.ui.screens.programs.ProgramDetailViewModel
import com.formsynth.ai.ui.screens.programs.ProgramsViewModel
import com.formsynth.ai.ui.screens.workout.WorkoutViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single<IExerciseRepository> { ExerciseRepository(androidContext()) }
    single<IProgramRepository> { ProgramRepository(androidContext()) }
    single<IWorkoutRepository> { WorkoutRepository() }
    single<IAuthRepository> { AuthRepository() }
    single<IUserRepository> { UserRepository() }
    single<IAchievementRepository> { AchievementRepository() }
    
    // Use Cases
    single { GetAllExercisesUseCase(get()) }
    single { GetExerciseByIdUseCase(get()) }
    single { SearchExercisesUseCase(get()) }
    single { GetAllProgramsUseCase(get()) }
    single { GetWorkoutStatsUseCase(get()) }
    single { SaveWorkoutSessionUseCase(get()) }
    single { SignInUseCase(get()) }
    single { SignUpUseCase(get()) }
    single { CheckAchievementsUseCase(get(), get()) }
    
    // ViewModels
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ExerciseListViewModel(get()) }
    viewModel { ProgramsViewModel(get()) }
    viewModel { params -> ProgramDetailViewModel(params.get(), get(), get()) }
    viewModel { CalendarViewModel(get()) }
    viewModel { params -> WorkoutViewModel(params.get(), get(), get()) }
    viewModel { AuthViewModel(get(), get(), get()) }
}

