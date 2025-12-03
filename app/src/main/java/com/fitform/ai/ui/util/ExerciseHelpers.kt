package com.fitform.ai.ui.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.fitform.ai.domain.model.Difficulty
import com.fitform.ai.domain.model.ExerciseCategory
import com.fitform.ai.domain.model.MuscleGroup
import com.fitform.ai.ui.theme.*

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



