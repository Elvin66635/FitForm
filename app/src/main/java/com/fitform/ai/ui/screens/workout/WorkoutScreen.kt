package com.fitform.ai.ui.screens.workout

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitform.ai.domain.model.Exercise
import com.fitform.ai.domain.model.FeedbackType
import com.fitform.ai.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun WorkoutScreen(
    exerciseId: String,
    onFinish: (score: Int, reps: Int, calories: Int, duration: Int) -> Unit,
    onBack: () -> Unit,
    viewModel: WorkoutViewModel = koinViewModel { parametersOf(exerciseId) }
) {
    val exercise by viewModel.exercise.collectAsState()
    val workoutState by viewModel.workoutState.collectAsState()
    val currentReps by viewModel.currentReps.collectAsState()
    val currentScore by viewModel.currentScore.collectAsState()
    val elapsedTime by viewModel.elapsedTime.collectAsState()
    val calories by viewModel.calories.collectAsState()
    val feedback by viewModel.feedback.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()
    val countdown by viewModel.countdown.collectAsState()
    
    var showExitDialog by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            WorkoutTopBar(
                exercise = exercise,
                elapsedTime = elapsedTime,
                onBack = { showExitDialog = true }
            )
            
            // Main Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                when (workoutState) {
                    WorkoutState.READY -> {
                        ReadyState(
                            exercise = exercise,
                            onStart = { viewModel.startCountdown() }
                        )
                    }
                    WorkoutState.COUNTDOWN -> {
                        CountdownState(countdown = countdown)
                    }
                    WorkoutState.ACTIVE, WorkoutState.PAUSED -> {
                        ActiveWorkoutState(
                            currentReps = currentReps,
                            currentScore = currentScore,
                            calories = calories,
                            isPaused = workoutState == WorkoutState.PAUSED,
                            feedback = feedback,
                            feedbackMessage = feedbackMessage,
                            onAddRep = { viewModel.addRep() }
                        )
                    }
                    WorkoutState.FINISHED -> {
                        // Navigate to result
                    }
                }
            }
            
            // Bottom Controls
            if (workoutState == WorkoutState.ACTIVE || workoutState == WorkoutState.PAUSED) {
                WorkoutControls(
                    isPaused = workoutState == WorkoutState.PAUSED,
                    onPause = { viewModel.pauseWorkout() },
                    onResume = { viewModel.resumeWorkout() },
                    onFinish = {
                        val result = viewModel.finishWorkout()
                        onFinish(result.score, result.reps, result.calories, result.duration)
                    }
                )
            }
        }
        
        // Exit Dialog
        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { Text("Завершить тренировку?", color = TextPrimary) },
                text = { Text("Ваш прогресс будет потерян.", color = TextSecondary) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            onBack()
                        }
                    ) {
                        Text("Выйти", color = Error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("Продолжить", color = Primary)
                    }
                },
                containerColor = Surface
            )
        }
    }
}

@Composable
private fun WorkoutTopBar(
    exercise: Exercise?,
    elapsedTime: Int,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Закрыть",
                tint = TextPrimary
            )
        }
        
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = exercise?.name ?: "Тренировка",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = formatTime(elapsedTime),
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
        
        // Placeholder for symmetry
        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
private fun ReadyState(
    exercise: Exercise?,
    onStart: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
    ) {
        // Exercise icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(60.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = exercise?.name ?: "Упражнение",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = exercise?.description ?: "",
            fontSize = 16.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Start Button
        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Начать",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CountdownState(countdown: Int) {
    val scale by animateFloatAsState(
        targetValue = if (countdown > 0) 1f else 0.5f,
        animationSpec = tween(300),
        label = "countdown_scale"
    )
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = countdown.toString(),
            fontSize = 120.sp,
            fontWeight = FontWeight.Bold,
            color = Primary,
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
private fun ActiveWorkoutState(
    currentReps: Int,
    currentScore: Int,
    calories: Int,
    isPaused: Boolean,
    feedback: FeedbackType,
    feedbackMessage: String,
    onAddRep: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WorkoutStat(
                value = currentReps.toString(),
                label = "Повторения",
                color = Primary
            )
            WorkoutStat(
                value = "$currentScore%",
                label = "Точность",
                color = Secondary
            )
            WorkoutStat(
                value = calories.toString(),
                label = "Калории",
                color = Accent
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Camera preview placeholder / Rep counter button
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Surface)
                .clickable { onAddRep() },
            contentAlignment = Alignment.Center
        ) {
            if (isPaused) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Пауза",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Нажмите для добавления повторения",
                        fontSize = 14.sp,
                        color = TextHint,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "(Камера в разработке)",
                        fontSize = 12.sp,
                        color = TextHint
                    )
                }
            }
            
            // Feedback overlay
            if (feedback != FeedbackType.NONE) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(getFeedbackColor(feedback).copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = getFeedbackColor(feedback)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = feedbackMessage,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutStat(
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = TextSecondary
        )
    }
}

@Composable
private fun WorkoutControls(
    isPaused: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onFinish: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Finish button
        OutlinedButton(
            onClick = onFinish,
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Error
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = Brush.linearGradient(listOf(Error, Error))
            )
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "Завершить",
                modifier = Modifier.size(28.dp)
            )
        }
        
        // Pause/Resume button
        Button(
            onClick = { if (isPaused) onResume() else onPause() },
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPaused) Primary else Secondary
            )
        ) {
            Icon(
                imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                contentDescription = if (isPaused) "Продолжить" else "Пауза",
                modifier = Modifier.size(36.dp)
            )
        }
        
        // Placeholder for symmetry
        Spacer(modifier = Modifier.size(64.dp))
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(minutes, secs)
}

private fun getFeedbackColor(feedback: FeedbackType): Color = when (feedback) {
    FeedbackType.PERFECT -> PoseCorrect
    FeedbackType.GOOD -> Secondary
    FeedbackType.CORRECT_FORM -> PoseCorrect
    FeedbackType.WARNING -> PoseWarning
    FeedbackType.INCORRECT -> PoseIncorrect
    FeedbackType.NONE -> Color.Transparent
}

