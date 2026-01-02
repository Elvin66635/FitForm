package com.formsynth.ai.ui.util

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.formsynth.ai.R
import com.formsynth.ai.domain.model.Difficulty
import com.formsynth.ai.domain.model.ExerciseCategory
import com.formsynth.ai.domain.model.MuscleGroup
import com.formsynth.ai.ui.theme.*

fun getCategoryColor(category: ExerciseCategory): Color = when (category) {
    ExerciseCategory.STRENGTH -> CategoryStrength
    ExerciseCategory.CARDIO -> CategoryCardio
    ExerciseCategory.HIIT -> CategoryHIIT
    ExerciseCategory.YOGA -> CategoryYoga
    ExerciseCategory.CALISTHENICS -> CategoryCalisthenics
    ExerciseCategory.CROSSFIT -> CategoryCrossfit
    ExerciseCategory.FLEXIBILITY -> CategoryYoga
}

fun getCategoryIcon(category: ExerciseCategory): ImageVector = when (category) {
    ExerciseCategory.STRENGTH -> Icons.Default.FitnessCenter
    ExerciseCategory.CARDIO -> Icons.Default.DirectionsRun
    ExerciseCategory.HIIT -> Icons.Default.Whatshot
    ExerciseCategory.YOGA -> Icons.Default.SelfImprovement
    ExerciseCategory.CALISTHENICS -> Icons.Default.Sports
    ExerciseCategory.CROSSFIT -> Icons.Default.FlashOn
    ExerciseCategory.FLEXIBILITY -> Icons.Default.Accessibility
}

fun getCategoryName(category: ExerciseCategory): String = when (category) {
    ExerciseCategory.STRENGTH -> "Сила"
    ExerciseCategory.CARDIO -> "Кардио"
    ExerciseCategory.HIIT -> "HIIT"
    ExerciseCategory.YOGA -> "Йога"
    ExerciseCategory.CALISTHENICS -> "Калистеника"
    ExerciseCategory.CROSSFIT -> "Кроссфит"
    ExerciseCategory.FLEXIBILITY -> "Гибкость"
}

fun getCategoryName(context: Context, category: ExerciseCategory): String = when (category) {
    ExerciseCategory.STRENGTH -> context.getString(R.string.category_strength)
    ExerciseCategory.CARDIO -> context.getString(R.string.category_cardio)
    ExerciseCategory.HIIT -> context.getString(R.string.category_hiit)
    ExerciseCategory.YOGA -> context.getString(R.string.category_yoga)
    ExerciseCategory.CALISTHENICS -> context.getString(R.string.category_calisthenics)
    ExerciseCategory.CROSSFIT -> context.getString(R.string.category_crossfit)
    ExerciseCategory.FLEXIBILITY -> context.getString(R.string.category_flexibility)
}

fun getCategoryEmoji(category: ExerciseCategory): String = when (category) {
    ExerciseCategory.STRENGTH -> "💪"
    ExerciseCategory.CARDIO -> "🏃"
    ExerciseCategory.HIIT -> "🔥"
    ExerciseCategory.YOGA -> "🧘"
    ExerciseCategory.CALISTHENICS -> "🤸"
    ExerciseCategory.CROSSFIT -> "⚡"
    ExerciseCategory.FLEXIBILITY -> "🌊"
}

fun getDifficultyColor(difficulty: Difficulty): Color = when (difficulty) {
    Difficulty.BEGINNER -> DifficultyBeginner
    Difficulty.INTERMEDIATE -> DifficultyIntermediate
    Difficulty.ADVANCED -> DifficultyAdvanced
}

fun getDifficultyName(difficulty: Difficulty): String = when (difficulty) {
    Difficulty.BEGINNER -> "Начинающий"
    Difficulty.INTERMEDIATE -> "Средний"
    Difficulty.ADVANCED -> "Продвинутый"
}

fun getDifficultyName(context: Context, difficulty: Difficulty): String = when (difficulty) {
    Difficulty.BEGINNER -> context.getString(R.string.difficulty_beginner)
    Difficulty.INTERMEDIATE -> context.getString(R.string.difficulty_intermediate)
    Difficulty.ADVANCED -> context.getString(R.string.difficulty_advanced)
}

fun getMuscleGroupName(muscle: MuscleGroup): String = when (muscle) {
    MuscleGroup.CHEST -> "Грудь"
    MuscleGroup.BACK -> "Спина"
    MuscleGroup.SHOULDERS -> "Плечи"
    MuscleGroup.BICEPS -> "Бицепс"
    MuscleGroup.TRICEPS -> "Трицепс"
    MuscleGroup.FOREARMS -> "Предплечья"
    MuscleGroup.ABS -> "Пресс"
    MuscleGroup.OBLIQUES -> "Косые"
    MuscleGroup.QUADRICEPS -> "Квадрицепс"
    MuscleGroup.HAMSTRINGS -> "Бицепс бедра"
    MuscleGroup.GLUTES -> "Ягодицы"
    MuscleGroup.CALVES -> "Икры"
    MuscleGroup.FULL_BODY -> "Всё тело"
    MuscleGroup.CORE -> "Кор"
}

fun getMuscleGroupName(context: Context, muscle: MuscleGroup): String = when (muscle) {
    MuscleGroup.CHEST -> context.getString(R.string.muscle_group_chest)
    MuscleGroup.BACK -> context.getString(R.string.muscle_group_back)
    MuscleGroup.SHOULDERS -> context.getString(R.string.muscle_group_shoulders)
    MuscleGroup.BICEPS -> context.getString(R.string.muscle_group_biceps)
    MuscleGroup.TRICEPS -> context.getString(R.string.muscle_group_triceps)
    MuscleGroup.FOREARMS -> "Предплечья" // No string resource for this
    MuscleGroup.ABS -> context.getString(R.string.muscle_group_abs)
    MuscleGroup.OBLIQUES -> context.getString(R.string.muscle_group_obliques)
    MuscleGroup.QUADRICEPS -> context.getString(R.string.muscle_group_quadriceps)
    MuscleGroup.HAMSTRINGS -> context.getString(R.string.muscle_group_hamstrings)
    MuscleGroup.GLUTES -> context.getString(R.string.muscle_group_glutes)
    MuscleGroup.CALVES -> context.getString(R.string.muscle_group_calves)
    MuscleGroup.FULL_BODY -> context.getString(R.string.muscle_group_full_body)
    MuscleGroup.CORE -> context.getString(R.string.muscle_group_core)
}



