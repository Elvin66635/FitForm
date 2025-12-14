package com.fitform.ai.ui.resources

import android.content.Context
import com.fitform.ai.domain.model.Exercise
import java.util.Locale

object ExerciseLocalizer {

    private val isRussian: Boolean
        get() = Locale.getDefault().language == "ru"

    fun getLocalizedName(context: Context, exercise: Exercise): String {
        val resourceName = "exercise_${exercise.id}_name"
        val resourceId = context.resources.getIdentifier(resourceName, "string", context.packageName)
        if (resourceId != 0) {
            return context.getString(resourceId)
        }
        
        return if (isRussian) {
            exercise.name
        } else {
            getEnglishName(exercise.id, exercise.name)
        }
    }

    fun getLocalizedDescription(context: Context, exercise: Exercise): String {
        val resourceName = "exercise_${exercise.id}_description"
        val resourceId = context.resources.getIdentifier(resourceName, "string", context.packageName)
        if (resourceId != 0) {
            return context.getString(resourceId)
        }
        
        return if (isRussian) {
            exercise.description
        } else {
            getEnglishDescription(exercise.id, exercise.description)
        }
    }

    fun localizeExercise(context: Context, exercise: Exercise): Exercise {
        return exercise.copy(
            name = getLocalizedName(context, exercise),
            description = getLocalizedDescription(context, exercise)
        )
    }

    fun localizeExercises(context: Context, exercises: List<Exercise>): List<Exercise> {
        return exercises.map { localizeExercise(context, it) }
    }

    private fun getEnglishName(exerciseId: String, russianName: String): String {
        val englishNames = mapOf(
            "barbell_bench_press" to "Barbell Bench Press",
            "dumbbell_bench_press" to "Dumbbell Bench Press",
            "incline_dumbbell_press" to "Incline Dumbbell Press",
            "dumbbell_flyes" to "Dumbbell Flyes",
            "pec_deck" to "Pec Deck Machine",
            "dips_chest" to "Dips (Chest Focus)",
            "pushup" to "Push-ups",
            
            "pullup" to "Pull-ups (Wide Grip)",
            "lat_pulldown" to "Lat Pulldown",
            "barbell_row" to "Barbell Row",
            "dumbbell_row" to "One-Arm Dumbbell Row",
            "seated_row" to "Seated Cable Row",
            "hyperextension" to "Hyperextension",
            
            "barbell_squat" to "Barbell Squat",
            "squat" to "Squats",
            "leg_press" to "Leg Press",
            "lunge" to "Dumbbell Lunges",
            "leg_curl" to "Leg Curl",
            "leg_extension" to "Leg Extension",
            "calf_raise" to "Calf Raise",
            
            "dumbbell_shoulder_press" to "Dumbbell Shoulder Press",
            "military_press" to "Military Press",
            "lateral_raise" to "Lateral Raise",
            "rear_delt_fly" to "Rear Delt Fly",
            "upright_row" to "Upright Row",
            
            "barbell_curl" to "Barbell Curl",
            "dumbbell_curl" to "Dumbbell Curl",
            "hammer_curl" to "Hammer Curl",
            "scott_curl" to "Scott Curl",
            "cable_curl" to "Cable Curl",
            
            "close_grip_bench_press" to "Close Grip Bench Press",
            "tricep_extension" to "French Press",
            "tricep_pushdown" to "Tricep Pushdown",
            "dips_tricep" to "Dips (Tricep Focus)",
            "overhead_tricep_extension" to "Overhead Tricep Extension",
            "tricep_dip" to "Tricep Dips",
            
            "crunch" to "Crunches",
            "hanging_leg_raise" to "Hanging Leg Raise",
            "plank" to "Plank",
            "russian_twist" to "Russian Twist",
            "ball_crunch" to "Ball Crunch",
            
            "treadmill" to "Treadmill",
            "bike" to "Exercise Bike",
            "rowing_machine" to "Rowing Machine",
            "jump_rope" to "Jump Rope",
            "burpee" to "Burpees",
            "running" to "Running"
        )
        
        return englishNames[exerciseId] ?: formatExerciseId(exerciseId)
    }

    private fun getEnglishDescription(exerciseId: String, russianDescription: String): String {
        val englishDescriptions = mapOf(
            "barbell_bench_press" to "Classic basic chest exercise",
            "dumbbell_bench_press" to "Chest development with dumbbells",
            "incline_dumbbell_press" to "Press for upper chest",
            "dumbbell_flyes" to "Isolation exercise for chest",
            "pec_deck" to "Isolation chest exercise on machine",
            "dips_chest" to "Dips with forward lean for chest",
            "pushup" to "Classic exercise for chest and triceps",
            
            "pullup" to "Basic back exercise",
            "lat_pulldown" to "Exercise for latissimus dorsi",
            "barbell_row" to "Basic back exercise",
            "dumbbell_row" to "Unilateral row for back",
            "seated_row" to "Back exercise on machine",
            "hyperextension" to "Exercise for lower back",
            
            "barbell_squat" to "Basic leg exercise",
            "squat" to "Basic leg exercise",
            "leg_press" to "Leg exercise on machine",
            "lunge" to "Exercise for legs and balance",
            "leg_curl" to "Isolation exercise for hamstrings",
            "leg_extension" to "Isolation exercise for quadriceps",
            "calf_raise" to "Isolated exercise for calves",
            
            "dumbbell_shoulder_press" to "Basic shoulder exercise",
            "military_press" to "Classic shoulder exercise",
            "lateral_raise" to "Isolation exercise for medial delts",
            "rear_delt_fly" to "Exercise for rear delts",
            "upright_row" to "Exercise for shoulders and traps",
            
            "barbell_curl" to "Classic bicep exercise",
            "dumbbell_curl" to "Bicep exercise",
            "hammer_curl" to "Curls with neutral grip for biceps and forearms",
            "scott_curl" to "Isolation bicep exercise",
            "cable_curl" to "Bicep curls on cable machine",
            
            "close_grip_bench_press" to "Close grip bench press for triceps",
            "tricep_extension" to "Arm extension for triceps",
            "tricep_pushdown" to "Tricep extension on cable machine",
            "dips_tricep" to "Dips with vertical torso",
            "overhead_tricep_extension" to "Extension for triceps",
            "tricep_dip" to "Dips from support for triceps",
            
            "crunch" to "Basic ab exercise",
            "hanging_leg_raise" to "Advanced ab exercise",
            "plank" to "Static core exercise",
            "russian_twist" to "Exercise for obliques",
            "ball_crunch" to "Crunches for abs on exercise ball",
            
            "treadmill" to "Cardio exercise for endurance",
            "bike" to "Cardio exercise on exercise bike",
            "rowing_machine" to "Cardio exercise on rowing machine",
            "jump_rope" to "Cardio exercise for coordination",
            "burpee" to "Full body compound exercise",
            "running" to "Cardio exercise for endurance"
        )
        
        return englishDescriptions[exerciseId] ?: ""
    }

    private fun formatExerciseId(exerciseId: String): String {
        return exerciseId.split("_")
            .joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
    }
}


