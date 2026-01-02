package com.formsynth.ai.ui.screens.result

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.formsynth.ai.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ResultScreen(
    score: Int,
    reps: Int,
    calories: Int,
    duration: Int,
    onDone: () -> Unit,
    onRepeat: (String) -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Background, getScoreGradientColor(score).copy(alpha = 0.2f))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            // Score Circle
            AnimatedVisibility(
                visible = showContent,
                enter = scaleIn(animationSpec = spring(dampingRatio = 0.6f)) + fadeIn()
            ) {
                ScoreCircle(score = score)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Congratulation text
            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = getScoreTitle(score),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = getScoreMessage(score),
                        fontSize = 16.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Stats Cards
            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ResultStatCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Repeat,
                            value = reps.toString(),
                            label = "Повторений",
                            color = Primary
                        )
                        ResultStatCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.LocalFireDepartment,
                            value = calories.toString(),
                            label = "Калорий",
                            color = Accent
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ResultStatCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Timer,
                            value = formatDuration(duration),
                            label = "Время",
                            color = Secondary
                        )
                        ResultStatCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.TrendingUp,
                            value = "$score%",
                            label = "Точность",
                            color = getScoreColor(score)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Buttons
            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Done button
                    Button(
                        onClick = onDone,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Готово",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    
                    // Repeat button
                    OutlinedButton(
                        onClick = { onRepeat("squat") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Primary
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Повторить",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ScoreCircle(score: Int) {
    val animatedScore by animateIntAsState(
        targetValue = score,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "score_animation"
    )
    
    Box(
        modifier = Modifier
            .size(180.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        getScoreColor(score).copy(alpha = 0.3f),
                        getScoreColor(score).copy(alpha = 0.1f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Surface),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$animatedScore",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = getScoreColor(score)
                )
                Text(
                    text = "баллов",
                    fontSize = 16.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun ResultStatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return if (minutes > 0) {
        "${minutes}м ${secs}с"
    } else {
        "${secs}с"
    }
}

private fun getScoreColor(score: Int): Color = when {
    score >= 90 -> PoseCorrect
    score >= 70 -> Secondary
    score >= 50 -> Warning
    else -> Error
}

private fun getScoreGradientColor(score: Int): Color = when {
    score >= 90 -> PoseCorrect
    score >= 70 -> Secondary
    score >= 50 -> Warning
    else -> Error
}

private fun getScoreTitle(score: Int): String = when {
    score >= 90 -> "Превосходно! 🏆"
    score >= 70 -> "Отлично! 💪"
    score >= 50 -> "Хорошо! 👍"
    else -> "Продолжайте! 💫"
}

private fun getScoreMessage(score: Int): String = when {
    score >= 90 -> "Ваша техника безупречна! Так держать!"
    score >= 70 -> "Отличный результат! Вы на правильном пути."
    score >= 50 -> "Неплохо! Продолжайте тренироваться."
    else -> "Не сдавайтесь! Практика делает мастера."
}




