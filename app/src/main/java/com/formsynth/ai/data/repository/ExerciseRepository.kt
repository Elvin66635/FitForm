package com.formsynth.ai.data.repository

import android.content.Context
import com.formsynth.ai.domain.model.*
import com.formsynth.ai.domain.repository.IExerciseRepository
import com.formsynth.ai.ui.resources.ExerciseLocalizer

class ExerciseRepository(private val context: Context) : IExerciseRepository {
    
    override fun getAllExercises(): List<Exercise> = 
        ExerciseLocalizer.localizeExercises(context, defaultExercises)
    
    override fun getExerciseById(id: String): Exercise? = 
        defaultExercises.find { it.id == id }?.let { 
            ExerciseLocalizer.localizeExercise(context, it) 
        }
    
    override fun getExercisesByCategory(category: ExerciseCategory): List<Exercise> =
        ExerciseLocalizer.localizeExercises(
            context, 
            defaultExercises.filter { it.category == category }
        )
    
    override fun getExercisesByMuscle(muscle: MuscleGroup): List<Exercise> =
        ExerciseLocalizer.localizeExercises(
            context,
            defaultExercises.filter { muscle in it.muscleGroups }
        )
    
    override fun getExercisesByDifficulty(difficulty: Difficulty): List<Exercise> =
        ExerciseLocalizer.localizeExercises(
            context,
            defaultExercises.filter { it.difficulty == difficulty }
        )
    
    override fun searchExercises(query: String): List<Exercise> {
        val localized = ExerciseLocalizer.localizeExercises(context, defaultExercises)
        return localized.filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.description.contains(query, ignoreCase = true)
        }
    }
    
    override fun getFreeExercises(): List<Exercise> = 
        ExerciseLocalizer.localizeExercises(
            context,
            defaultExercises.filter { !it.isPremium }
        )
    
    companion object {
        val defaultExercises = listOf(
            Exercise(
                id = "barbell_bench_press",
                name = "Жим штанги лёжа",
                description = "Классическое базовое упражнение для груди",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                calories = 12,
                equipment = Equipment.BARBELL,
                isGym = true
            ),
            Exercise(
                id = "dumbbell_bench_press",
                name = "Жим гантелей лёжа",
                description = "Жим гантелей для развития груди",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                calories = 11,
                equipment = Equipment.DUMBBELLS,
                isGym = true
            ),
            Exercise(
                id = "incline_dumbbell_press",
                name = "Жим гантелей на наклонной скамье",
                description = "Жим для верхней части груди",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                calories = 11,
                equipment = Equipment.DUMBBELLS,
                isGym = true
            ),
            Exercise(
                id = "dumbbell_flyes",
                name = "Разведения гантелей лёжа",
                description = "Изолирующее упражнение для груди",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.CHEST),
                calories = 8,
                equipment = Equipment.DUMBBELLS,
                isGym = true
            ),
            Exercise(
                id = "pec_deck",
                name = "Сведение рук в тренажёре",
                description = "Изолирующее упражнение для груди в тренажёре",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.CHEST),
                calories = 7,
                equipment = Equipment.MACHINE,
                isGym = true
            ),
            Exercise(
                id = "dips_chest",
                name = "Отжимания на брусьях (акцент на грудь)",
                description = "Отжимания на брусьях с наклоном вперёд для груди",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                calories = 10,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            Exercise(
                id = "pushup",
                name = "Отжимания",
                description = "Классическое упражнение для груди и трицепсов",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                calories = 7,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            
            Exercise(
                id = "pullup",
                name = "Подтягивания широким хватом",
                description = "Базовое упражнение для спины",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
                calories = 10,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            Exercise(
                id = "lat_pulldown",
                name = "Тяга верхнего блока к груди",
                description = "Упражнение для широчайших мышц спины",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
                calories = 9,
                equipment = Equipment.MACHINE,
                isGym = true
            ),
            Exercise(
                id = "barbell_row",
                name = "Тяга штанги в наклоне",
                description = "Базовое упражнение для спины",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
                calories = 10,
                equipment = Equipment.BARBELL,
                isGym = true
            ),
            Exercise(
                id = "dumbbell_row",
                name = "Тяга гантели одной рукой",
                description = "Односторонняя тяга для спины",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
                calories = 9,
                equipment = Equipment.DUMBBELLS,
                isGym = true
            ),
            Exercise(
                id = "seated_row",
                name = "Горизонтальная тяга в тренажёре",
                description = "Упражнение для спины в тренажёре",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
                calories = 8,
                equipment = Equipment.MACHINE,
                isGym = true
            ),
            Exercise(
                id = "hyperextension",
                name = "Гиперэкстензия",
                description = "Упражнение для нижней части спины",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.GLUTES),
                calories = 5,
                equipment = Equipment.MACHINE,
                isGym = true
            ),
            
            Exercise(
                id = "barbell_squat",
                name = "Приседания со штангой",
                description = "Базовое упражнение для ног",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
                calories = 13,
                equipment = Equipment.BARBELL,
                isGym = true
            ),
            Exercise(
                id = "squat",
                name = "Приседания",
                description = "Базовое упражнение для ног",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
                calories = 9,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            Exercise(
                id = "leg_press",
                name = "Жим ногами в тренажёре",
                description = "Упражнение для ног в тренажёре",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.GLUTES),
                calories = 11,
                equipment = Equipment.MACHINE,
                isGym = true
            ),
            Exercise(
                id = "lunge",
                name = "Выпады с гантелями",
                description = "Упражнение для ног и баланса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
                calories = 8,
                equipment = Equipment.DUMBBELLS,
                isGym = true
            ),
            Exercise(
                id = "leg_curl",
                name = "Сгибание ног лёжа",
                description = "Изолирующее упражнение для задней поверхности бедра",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.HAMSTRINGS),
                calories = 6,
                equipment = Equipment.MACHINE,
                isGym = true
            ),
            Exercise(
                id = "leg_extension",
                name = "Разгибание ног сидя",
                description = "Изолирующее упражнение для квадрицепсов",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS),
                calories = 6,
                equipment = Equipment.MACHINE,
                isGym = true
            ),
            Exercise(
                id = "calf_raise",
                name = "Подъёмы на носки (икры)",
                description = "Изолированное упражнение для икр",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.CALVES),
                calories = 4,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            
            Exercise(
                id = "dumbbell_shoulder_press",
                name = "Жим гантелей сидя",
                description = "Базовое упражнение для плеч",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
                calories = 10,
                equipment = Equipment.DUMBBELLS,
                isGym = true
            ),
            Exercise(
                id = "military_press",
                name = "Жим штанги стоя",
                description = "Классическое упражнение для плеч",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
                calories = 11,
                equipment = Equipment.BARBELL,
                isGym = true
            ),
            Exercise(
                id = "lateral_raise",
                name = "Разведения гантелей в стороны",
                description = "Изолирующее упражнение для средних дельт",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.SHOULDERS),
                calories = 6,
                equipment = Equipment.DUMBBELLS,
                isGym = true
            ),
            Exercise(
                id = "rear_delt_fly",
                name = "Разведения гантелей в наклоне",
                description = "Упражнение для задней дельты",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.SHOULDERS),
                calories = 6,
                equipment = Equipment.DUMBBELLS,
                isGym = true
            ),
            Exercise(
                id = "upright_row",
                name = "Тяга штанги к подбородку",
                description = "Упражнение для плеч и трапеций",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.BACK),
                calories = 8,
                equipment = Equipment.BARBELL,
                isGym = true
            ),
            
            Exercise(
                id = "barbell_curl",
                name = "Подъём штанги на бицепс",
                description = "Классическое упражнение для бицепса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.BICEPS),
                calories = 6,
                equipment = Equipment.BARBELL,
                isGym = true
            ),
            Exercise(
                id = "dumbbell_curl",
                name = "Подъём гантелей на бицепс",
                description = "Упражнение для бицепса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.BICEPS),
                calories = 6,
                equipment = Equipment.DUMBBELLS,
                isGym = true
            ),
            Exercise(
                id = "hammer_curl",
                name = "Молотковые сгибания",
                description = "Сгибания с нейтральным хватом для бицепса и предплечий",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.BICEPS, MuscleGroup.FOREARMS),
                calories = 6,
                equipment = Equipment.DUMBBELLS,
                isGym = true
            ),
            Exercise(
                id = "scott_curl",
                name = "Сгибания рук на скамье Скотта",
                description = "Изолирующее упражнение для бицепса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.BICEPS),
                calories = 6,
                equipment = Equipment.BARBELL,
                isGym = true
            ),
            Exercise(
                id = "cable_curl",
                name = "Сгибания рук в кроссовере",
                description = "Сгибания на бицепс в кроссовере",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.BICEPS),
                calories = 6,
                equipment = Equipment.CABLE,
                isGym = true
            ),
            
            Exercise(
                id = "close_grip_bench_press",
                name = "Жим узким хватом",
                description = "Жим лёжа узким хватом для трицепса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST),
                calories = 10,
                equipment = Equipment.BARBELL,
                isGym = true
            ),
            Exercise(
                id = "tricep_extension",
                name = "Французский жим",
                description = "Разгибание рук для трицепса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.TRICEPS),
                calories = 7,
                equipment = Equipment.BARBELL,
                isGym = true
            ),
            Exercise(
                id = "tricep_pushdown",
                name = "Разгибание рук на блоке",
                description = "Разгибание на трицепс в кроссовере",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.TRICEPS),
                calories = 6,
                equipment = Equipment.CABLE,
                isGym = true
            ),
            Exercise(
                id = "dips_tricep",
                name = "Отжимания на брусьях (акцент на трицепс)",
                description = "Отжимания на брусьях с вертикальным корпусом",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                calories = 9,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            Exercise(
                id = "overhead_tricep_extension",
                name = "Разгибание гантели из-за головы",
                description = "Разгибание для трицепса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.TRICEPS),
                calories = 7,
                equipment = Equipment.DUMBBELLS,
                isGym = true
            ),
            Exercise(
                id = "tricep_dip",
                name = "Обратные отжимания",
                description = "Отжимания от опоры для трицепсов",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                calories = 6,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            
            Exercise(
                id = "crunch",
                name = "Скручивания",
                description = "Базовое упражнение для пресса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.ABS),
                calories = 5,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            Exercise(
                id = "hanging_leg_raise",
                name = "Подъёмы ног в висе",
                description = "Продвинутое упражнение для пресса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.ADVANCED,
                muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.CORE),
                calories = 9,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            Exercise(
                id = "plank",
                name = "Планка",
                description = "Статическое упражнение для кора",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.CORE),
                calories = 4,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            Exercise(
                id = "russian_twist",
                name = "Косые скручивания",
                description = "Упражнение для косых мышц",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.OBLIQUES, MuscleGroup.ABS),
                calories = 6,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            Exercise(
                id = "ball_crunch",
                name = "Скручивания на фитболе",
                description = "Скручивания для пресса на фитболе",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.ABS),
                calories = 5,
                equipment = Equipment.OTHER,
                isGym = true
            ),
            
            Exercise(
                id = "treadmill",
                name = "Беговая дорожка",
                description = "Кардио упражнение для выносливости",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.QUADRICEPS, MuscleGroup.CALVES),
                calories = 12,
                equipment = Equipment.MACHINE,
                isGym = true
            ),
            Exercise(
                id = "bike",
                name = "Велотренажёр",
                description = "Кардио упражнение на велотренажёре",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.CALVES),
                calories = 10,
                equipment = Equipment.MACHINE,
                isGym = true
            ),
            Exercise(
                id = "rowing_machine",
                name = "Гребной тренажёр",
                description = "Кардио упражнение на гребном тренажёре",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.BACK, MuscleGroup.QUADRICEPS, MuscleGroup.HAMSTRINGS),
                calories = 14,
                equipment = Equipment.MACHINE,
                isGym = true
            ),
            Exercise(
                id = "jump_rope",
                name = "Скакалка",
                description = "Кардио упражнение для координации",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.CALVES),
                calories = 11,
                equipment = Equipment.OTHER,
                isGym = false
            ),
            Exercise(
                id = "burpee",
                name = "Бёрпи",
                description = "Комплексное упражнение на всё тело",
                category = ExerciseCategory.HIIT,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.FULL_BODY),
                calories = 15,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            ),
            Exercise(
                id = "running",
                name = "Бег",
                description = "Кардио упражнение для выносливости",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.FULL_BODY, MuscleGroup.QUADRICEPS, MuscleGroup.CALVES),
                calories = 12,
                equipment = Equipment.BODYWEIGHT,
                isGym = false
            )
        )
    }
}
