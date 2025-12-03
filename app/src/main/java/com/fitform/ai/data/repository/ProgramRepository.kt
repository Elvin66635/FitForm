package com.fitform.ai.data.repository

import com.fitform.ai.domain.model.*
import com.fitform.ai.domain.repository.IProgramRepository

class ProgramRepository : IProgramRepository {
    
    override fun getAllPrograms(): List<WorkoutProgram> = defaultPrograms
    
    override fun getProgramById(id: String): WorkoutProgram? = defaultPrograms.find { it.id == id }
    
    override fun getProgramsByCategory(category: ExerciseCategory): List<WorkoutProgram> =
        defaultPrograms.filter { it.category == category }
    
    override fun getFreePrograms(): List<WorkoutProgram> = defaultPrograms.filter { !it.isPremium }
    
    companion object {
        val defaultPrograms = listOf(
            WorkoutProgram(
                id = "beginner_full_body",
                name = "Начинающий: Всё тело",
                description = "Идеальная программа для новичков. 3 тренировки в неделю на всё тело.",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                durationWeeks = 4,
                workoutsPerWeek = 3,
                workouts = listOf(
                    Workout(
                        id = "bfb_day1",
                        name = "День A",
                        dayOfWeek = 1,
                        estimatedDuration = 30,
                        restBetweenExercises = 90,
                        exercises = listOf(
                            WorkoutExercise("squat", sets = 3, reps = 12, restBetweenSets = 60),
                            WorkoutExercise("pushup", sets = 3, reps = 10, restBetweenSets = 60),
                            WorkoutExercise("lunge", sets = 3, reps = 10, restBetweenSets = 60),
                            WorkoutExercise("plank", sets = 3, duration = 30, restBetweenSets = 45)
                        )
                    ),
                    Workout(
                        id = "bfb_day2",
                        name = "День B",
                        dayOfWeek = 3,
                        estimatedDuration = 30,
                        restBetweenExercises = 90,
                        exercises = listOf(
                            WorkoutExercise("squat", sets = 3, reps = 15, restBetweenSets = 60),
                            WorkoutExercise("crunch", sets = 3, reps = 15, restBetweenSets = 45),
                            WorkoutExercise("jumping_jacks", sets = 3, reps = 20, restBetweenSets = 45),
                            WorkoutExercise("pushup", sets = 3, reps = 8, restBetweenSets = 60)
                        )
                    ),
                    Workout(
                        id = "bfb_day3",
                        name = "День C",
                        dayOfWeek = 5,
                        estimatedDuration = 30,
                        restBetweenExercises = 90,
                        exercises = listOf(
                            WorkoutExercise("lunge", sets = 3, reps = 12, restBetweenSets = 60),
                            WorkoutExercise("plank", sets = 3, duration = 40, restBetweenSets = 45),
                            WorkoutExercise("leg_raise", sets = 3, reps = 12, restBetweenSets = 45),
                            WorkoutExercise("high_knees", sets = 3, duration = 30, restBetweenSets = 60)
                        )
                    )
                ),
                isPremium = false
            ),
            WorkoutProgram(
                id = "strength_basics",
                name = "Силовая база",
                description = "Классическая силовая программа для набора мышечной массы",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                durationWeeks = 8,
                workoutsPerWeek = 4,
                workouts = listOf(
                    Workout(
                        id = "sb_push",
                        name = "Толкающие",
                        dayOfWeek = 1,
                        estimatedDuration = 45,
                        restBetweenExercises = 120,
                        exercises = listOf(
                            WorkoutExercise("pushup", sets = 4, reps = 12, restBetweenSets = 90),
                            WorkoutExercise("pike_pushup", sets = 3, reps = 10, restBetweenSets = 90),
                            WorkoutExercise("diamond_pushup", sets = 3, reps = 10, restBetweenSets = 90),
                            WorkoutExercise("dip", sets = 3, reps = 8, restBetweenSets = 90)
                        )
                    ),
                    Workout(
                        id = "sb_pull",
                        name = "Тянущие",
                        dayOfWeek = 2,
                        estimatedDuration = 45,
                        restBetweenExercises = 120,
                        exercises = listOf(
                            WorkoutExercise("pullup", sets = 4, reps = 8, restBetweenSets = 120),
                            WorkoutExercise("chin_up", sets = 3, reps = 8, restBetweenSets = 90),
                            WorkoutExercise("inverted_row", sets = 3, reps = 12, restBetweenSets = 90)
                        )
                    ),
                    Workout(
                        id = "sb_legs",
                        name = "Ноги",
                        dayOfWeek = 4,
                        estimatedDuration = 45,
                        restBetweenExercises = 120,
                        exercises = listOf(
                            WorkoutExercise("squat", sets = 4, reps = 12, restBetweenSets = 90),
                            WorkoutExercise("lunge", sets = 3, reps = 12, restBetweenSets = 90),
                            WorkoutExercise("squat_jump", sets = 3, reps = 10, restBetweenSets = 60),
                            WorkoutExercise("glute_bridge", sets = 3, reps = 15, restBetweenSets = 60)
                        )
                    ),
                    Workout(
                        id = "sb_core",
                        name = "Кор + Кардио",
                        dayOfWeek = 5,
                        estimatedDuration = 35,
                        restBetweenExercises = 60,
                        exercises = listOf(
                            WorkoutExercise("plank", sets = 3, duration = 60, restBetweenSets = 45),
                            WorkoutExercise("russian_twist", sets = 3, reps = 20, restBetweenSets = 45),
                            WorkoutExercise("leg_raise", sets = 3, reps = 15, restBetweenSets = 45),
                            WorkoutExercise("mountain_climber", sets = 3, duration = 30, restBetweenSets = 45)
                        )
                    )
                ),
                isPremium = false
            ),
            WorkoutProgram(
                id = "hiit_fat_burn",
                name = "HIIT: Сжигание жира",
                description = "Интенсивные интервальные тренировки для максимального жиросжигания",
                category = ExerciseCategory.HIIT,
                difficulty = Difficulty.INTERMEDIATE,
                durationWeeks = 6,
                workoutsPerWeek = 3,
                workouts = listOf(
                    Workout(
                        id = "hiit_a",
                        name = "HIIT Микс",
                        estimatedDuration = 25,
                        restBetweenExercises = 30,
                        exercises = listOf(
                            WorkoutExercise("burpee", sets = 4, reps = 10, restBetweenSets = 30),
                            WorkoutExercise("squat_jump", sets = 4, reps = 15, restBetweenSets = 30),
                            WorkoutExercise("mountain_climber", sets = 4, duration = 30, restBetweenSets = 30),
                            WorkoutExercise("high_knees", sets = 4, duration = 30, restBetweenSets = 30)
                        )
                    ),
                    Workout(
                        id = "hiit_b",
                        name = "Табата",
                        estimatedDuration = 20,
                        restBetweenExercises = 10,
                        exercises = listOf(
                            WorkoutExercise("jumping_jacks", sets = 8, duration = 20, restBetweenSets = 10),
                            WorkoutExercise("pushup", sets = 8, duration = 20, restBetweenSets = 10),
                            WorkoutExercise("squat", sets = 8, duration = 20, restBetweenSets = 10),
                            WorkoutExercise("plank", sets = 8, duration = 20, restBetweenSets = 10)
                        )
                    )
                ),
                isPremium = false
            ),
            WorkoutProgram(
                id = "calisthenics_starter",
                name = "Калистеника: Старт",
                description = "Тренировки с собственным весом для развития силы и контроля тела",
                category = ExerciseCategory.CALISTHENICS,
                difficulty = Difficulty.INTERMEDIATE,
                durationWeeks = 8,
                workoutsPerWeek = 3,
                workouts = listOf(
                    Workout(
                        id = "cal_upper",
                        name = "Верх тела",
                        dayOfWeek = 1,
                        estimatedDuration = 40,
                        restBetweenExercises = 90,
                        exercises = listOf(
                            WorkoutExercise("pullup", sets = 4, reps = 6, restBetweenSets = 120),
                            WorkoutExercise("pushup", sets = 4, reps = 15, restBetweenSets = 60),
                            WorkoutExercise("dip", sets = 3, reps = 10, restBetweenSets = 90),
                            WorkoutExercise("pike_pushup", sets = 3, reps = 10, restBetweenSets = 60)
                        )
                    ),
                    Workout(
                        id = "cal_lower",
                        name = "Низ тела + Кор",
                        dayOfWeek = 3,
                        estimatedDuration = 35,
                        restBetweenExercises = 60,
                        exercises = listOf(
                            WorkoutExercise("squat", sets = 4, reps = 20, restBetweenSets = 60),
                            WorkoutExercise("lunge", sets = 3, reps = 15, restBetweenSets = 60),
                            WorkoutExercise("plank", sets = 3, duration = 60, restBetweenSets = 45),
                            WorkoutExercise("leg_raise", sets = 3, reps = 15, restBetweenSets = 45)
                        )
                    ),
                    Workout(
                        id = "cal_full",
                        name = "Всё тело",
                        dayOfWeek = 5,
                        estimatedDuration = 45,
                        restBetweenExercises = 90,
                        exercises = listOf(
                            WorkoutExercise("burpee", sets = 3, reps = 8, restBetweenSets = 60),
                            WorkoutExercise("pullup", sets = 3, reps = 5, restBetweenSets = 90),
                            WorkoutExercise("squat_jump", sets = 3, reps = 12, restBetweenSets = 60),
                            WorkoutExercise("pushup", sets = 3, reps = 12, restBetweenSets = 60),
                            WorkoutExercise("russian_twist", sets = 3, reps = 20, restBetweenSets = 45)
                        )
                    )
                ),
                isPremium = false
            ),
            WorkoutProgram(
                id = "premium_shred",
                name = "PRO: 12-недельная трансформация",
                description = "Комплексная программа для полного преображения тела",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.ADVANCED,
                durationWeeks = 12,
                workoutsPerWeek = 5,
                workouts = emptyList(),
                isPremium = true
            ),
            WorkoutProgram(
                id = "premium_athlete",
                name = "PRO: Атлет",
                description = "Тренировки как у профессиональных спортсменов",
                category = ExerciseCategory.CROSSFIT,
                difficulty = Difficulty.ADVANCED,
                durationWeeks = 8,
                workoutsPerWeek = 6,
                workouts = emptyList(),
                isPremium = true
            )
        )
    }
}

