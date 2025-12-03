package com.fitform.ai.data.repository

import com.fitform.ai.domain.model.*
import com.fitform.ai.domain.repository.IExerciseRepository

class ExerciseRepository : IExerciseRepository {
    
    override fun getAllExercises(): List<Exercise> = defaultExercises
    
    override fun getExerciseById(id: String): Exercise? = defaultExercises.find { it.id == id }
    
    override fun getExercisesByCategory(category: ExerciseCategory): List<Exercise> =
        defaultExercises.filter { it.category == category }
    
    override fun getExercisesByMuscle(muscle: MuscleGroup): List<Exercise> =
        defaultExercises.filter { muscle in it.muscleGroups }
    
    override fun getExercisesByDifficulty(difficulty: Difficulty): List<Exercise> =
        defaultExercises.filter { it.difficulty == difficulty }
    
    override fun searchExercises(query: String): List<Exercise> =
        defaultExercises.filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.description.contains(query, ignoreCase = true)
        }
    
    override fun getFreeExercises(): List<Exercise> = defaultExercises.filter { !it.isPremium }
    
    companion object {
        val defaultExercises = listOf(
            Exercise(
                id = "pushup",
                name = "Отжимания",
                description = "Классическое упражнение для груди и трицепсов",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                calories = 7,
                instructions = listOf(
                    "Примите упор лёжа, руки на ширине плеч",
                    "Держите тело прямым от головы до пяток",
                    "Опуститесь, сгибая локти",
                    "Поднимитесь, выпрямляя руки"
                ),
                tips = listOf("Не прогибайте поясницу", "Локти под углом 45°")
            ),
            Exercise(
                id = "diamond_pushup",
                name = "Алмазные отжимания",
                description = "Отжимания с узкой постановкой рук для трицепсов",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST),
                calories = 8
            ),
            Exercise(
                id = "wide_pushup",
                name = "Широкие отжимания",
                description = "Отжимания с широкой постановкой рук для груди",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
                calories = 7
            ),
            Exercise(
                id = "pike_pushup",
                name = "Отжимания уголком",
                description = "Отжимания для плеч с поднятым тазом",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
                calories = 8
            ),
            Exercise(
                id = "decline_pushup",
                name = "Отжимания с ногами на возвышении",
                description = "Усложнённые отжимания для верхней части груди",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
                calories = 9
            ),
            Exercise(
                id = "pullup",
                name = "Подтягивания",
                description = "Базовое упражнение для спины и бицепсов",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
                calories = 10,
                instructions = listOf(
                    "Возьмитесь за перекладину хватом шире плеч",
                    "Подтянитесь, пока подбородок не окажется выше перекладины",
                    "Медленно опуститесь в исходное положение"
                )
            ),
            Exercise(
                id = "chin_up",
                name = "Подтягивания обратным хватом",
                description = "Подтягивания с акцентом на бицепс",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.BICEPS, MuscleGroup.BACK),
                calories = 10
            ),
            Exercise(
                id = "inverted_row",
                name = "Австралийские подтягивания",
                description = "Горизонтальные подтягивания для начинающих",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
                calories = 6
            ),
            Exercise(
                id = "superman",
                name = "Супермен",
                description = "Упражнение для нижней части спины",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.GLUTES),
                calories = 4
            ),
            Exercise(
                id = "squat",
                name = "Приседания",
                description = "Базовое упражнение для ног",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
                calories = 8,
                instructions = listOf(
                    "Встаньте, ноги на ширине плеч",
                    "Присядьте, отводя таз назад",
                    "Колени не выходят за носки",
                    "Вернитесь в исходное положение"
                )
            ),
            Exercise(
                id = "squat_jump",
                name = "Приседания с прыжком",
                description = "Взрывное упражнение для ног",
                category = ExerciseCategory.HIIT,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.GLUTES, MuscleGroup.CALVES),
                calories = 12
            ),
            Exercise(
                id = "lunge",
                name = "Выпады",
                description = "Упражнение для ног и баланса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
                calories = 7
            ),
            Exercise(
                id = "walking_lunge",
                name = "Шагающие выпады",
                description = "Динамичные выпады с продвижением",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.GLUTES),
                calories = 9
            ),
            Exercise(
                id = "bulgarian_split_squat",
                name = "Болгарские выпады",
                description = "Выпады с задней ногой на возвышении",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.GLUTES),
                calories = 8
            ),
            Exercise(
                id = "calf_raise",
                name = "Подъём на носки",
                description = "Изолированное упражнение для икр",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.CALVES),
                calories = 3
            ),
            Exercise(
                id = "wall_sit",
                name = "Стульчик у стены",
                description = "Статическое упражнение для ног",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS),
                calories = 5
            ),
            Exercise(
                id = "glute_bridge",
                name = "Ягодичный мост",
                description = "Упражнение для ягодиц",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.GLUTES, MuscleGroup.HAMSTRINGS),
                calories = 5
            ),
            Exercise(
                id = "plank",
                name = "Планка",
                description = "Статическое упражнение для кора",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.CORE),
                calories = 4,
                instructions = listOf(
                    "Примите упор на предплечьях",
                    "Тело должно быть прямой линией",
                    "Напрягите пресс и ягодицы",
                    "Удерживайте положение"
                )
            ),
            Exercise(
                id = "side_plank",
                name = "Боковая планка",
                description = "Планка для косых мышц",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.OBLIQUES, MuscleGroup.CORE),
                calories = 4
            ),
            Exercise(
                id = "crunch",
                name = "Скручивания",
                description = "Базовое упражнение для пресса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.ABS),
                calories = 5
            ),
            Exercise(
                id = "bicycle_crunch",
                name = "Велосипед",
                description = "Скручивания с вращением для косых мышц",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.OBLIQUES),
                calories = 7
            ),
            Exercise(
                id = "leg_raise",
                name = "Подъём ног лёжа",
                description = "Упражнение для нижней части пресса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.ABS),
                calories = 6
            ),
            Exercise(
                id = "russian_twist",
                name = "Русские скручивания",
                description = "Упражнение для косых мышц",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.OBLIQUES, MuscleGroup.ABS),
                calories = 6
            ),
            Exercise(
                id = "mountain_climber",
                name = "Скалолаз",
                description = "Динамическая планка для кардио и пресса",
                category = ExerciseCategory.HIIT,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.CORE, MuscleGroup.FULL_BODY),
                calories = 10
            ),
            Exercise(
                id = "v_up",
                name = "V-складка",
                description = "Продвинутое упражнение для пресса",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.ADVANCED,
                muscleGroups = listOf(MuscleGroup.ABS),
                calories = 8
            ),
            Exercise(
                id = "dead_bug",
                name = "Мёртвый жук",
                description = "Упражнение для стабилизации кора",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.CORE),
                calories = 4
            ),
            Exercise(
                id = "burpee",
                name = "Бёрпи",
                description = "Комплексное упражнение на всё тело",
                category = ExerciseCategory.HIIT,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.FULL_BODY),
                calories = 15,
                instructions = listOf(
                    "Из положения стоя присядьте",
                    "Прыжком примите упор лёжа",
                    "Выполните отжимание (опционально)",
                    "Прыжком вернитесь в присед",
                    "Выпрыгните вверх с поднятыми руками"
                )
            ),
            Exercise(
                id = "jumping_jacks",
                name = "Прыжки с разведением",
                description = "Кардио упражнение для разминки",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.FULL_BODY),
                calories = 8
            ),
            Exercise(
                id = "high_knees",
                name = "Бег с высоким подъёмом колен",
                description = "Интенсивное кардио на месте",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.ABS),
                calories = 10
            ),
            Exercise(
                id = "butt_kicks",
                name = "Бег с захлёстом",
                description = "Бег на месте с касанием пятками ягодиц",
                category = ExerciseCategory.CARDIO,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.CALVES),
                calories = 9
            ),
            Exercise(
                id = "box_jump",
                name = "Запрыгивания на тумбу",
                description = "Плиометрическое упражнение для ног",
                category = ExerciseCategory.HIIT,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.GLUTES, MuscleGroup.CALVES),
                calories = 12
            ),
            Exercise(
                id = "skater_jump",
                name = "Конькобежец",
                description = "Прыжки в сторону для координации",
                category = ExerciseCategory.HIIT,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.GLUTES, MuscleGroup.QUADRICEPS),
                calories = 11
            ),
            Exercise(
                id = "tuck_jump",
                name = "Прыжки с подтягиванием колен",
                description = "Взрывные прыжки вверх",
                category = ExerciseCategory.HIIT,
                difficulty = Difficulty.ADVANCED,
                muscleGroups = listOf(MuscleGroup.QUADRICEPS, MuscleGroup.ABS),
                calories = 14
            ),
            Exercise(
                id = "tricep_dip",
                name = "Обратные отжимания",
                description = "Отжимания от опоры для трицепсов",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
                calories = 6
            ),
            Exercise(
                id = "dip",
                name = "Отжимания на брусьях",
                description = "Классическое упражнение для трицепсов и груди",
                category = ExerciseCategory.CALISTHENICS,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.TRICEPS, MuscleGroup.CHEST, MuscleGroup.SHOULDERS),
                calories = 9
            ),
            Exercise(
                id = "shoulder_tap",
                name = "Касания плеч в планке",
                description = "Планка с поочерёдным касанием плеч",
                category = ExerciseCategory.STRENGTH,
                difficulty = Difficulty.INTERMEDIATE,
                muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.CORE),
                calories = 6
            ),
            Exercise(
                id = "arm_circles",
                name = "Круговые вращения руками",
                description = "Разминочное упражнение для плеч",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.SHOULDERS),
                calories = 2
            ),
            Exercise(
                id = "cobra_stretch",
                name = "Поза кобры",
                description = "Растяжка для спины и пресса",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.ABS, MuscleGroup.BACK),
                calories = 2
            ),
            Exercise(
                id = "child_pose",
                name = "Поза ребёнка",
                description = "Расслабляющая растяжка для спины",
                category = ExerciseCategory.YOGA,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.BACK),
                calories = 1
            ),
            Exercise(
                id = "cat_cow",
                name = "Кошка-корова",
                description = "Мобильность позвоночника",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.ABS),
                calories = 2
            ),
            Exercise(
                id = "downward_dog",
                name = "Собака мордой вниз",
                description = "Классическая поза йоги для всего тела",
                category = ExerciseCategory.YOGA,
                difficulty = Difficulty.BEGINNER,
                muscleGroups = listOf(MuscleGroup.HAMSTRINGS, MuscleGroup.SHOULDERS, MuscleGroup.BACK),
                calories = 3
            )
        )
    }
}

