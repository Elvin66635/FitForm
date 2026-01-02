package com.formsynth.ai.ui.screens.workout

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.formsynth.ai.R
import com.formsynth.ai.ui.theme.*

@Composable
fun ExerciseInstructionsDialog(
    onDismiss: () -> Unit,
    onStart: () -> Unit,
    onSkip: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = androidx.compose.ui.platform.LocalContext.current.getString(R.string.instructions_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Step Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepIndicator(step = 1, currentStep = currentStep)
                    Spacer(modifier = Modifier.width(16.dp))
                    StepIndicator(step = 2, currentStep = currentStep)
                    Spacer(modifier = Modifier.width(16.dp))
                    StepIndicator(step = 3, currentStep = currentStep)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Step Content
                when (currentStep) {
                    1 -> StepContent(
                        icon = Icons.Default.CameraAlt,
                        title = androidx.compose.ui.platform.LocalContext.current.getString(R.string.instructions_step1_title),
                        description = androidx.compose.ui.platform.LocalContext.current.getString(R.string.instructions_step1_description)
                    )
                    2 -> StepContent(
                        icon = Icons.Default.Lightbulb,
                        title = androidx.compose.ui.platform.LocalContext.current.getString(R.string.instructions_step2_title),
                        description = androidx.compose.ui.platform.LocalContext.current.getString(R.string.instructions_step2_description)
                    )
                    3 -> StepContent(
                        icon = Icons.Default.CheckCircle,
                        title = androidx.compose.ui.platform.LocalContext.current.getString(R.string.instructions_step3_title),
                        description = androidx.compose.ui.platform.LocalContext.current.getString(R.string.instructions_step3_description)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(
                            onClick = { currentStep-- },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Back")
                        }
                    } else {
                        TextButton(
                            onClick = onSkip,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = androidx.compose.ui.platform.LocalContext.current.getString(R.string.instructions_skip),
                                color = TextSecondary
                            )
                        }
                    }
                    
                    Button(
                        onClick = {
                            if (currentStep < 3) {
                                currentStep++
                            } else {
                                onStart()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text(
                            text = if (currentStep < 3) {
                                androidx.compose.ui.platform.LocalContext.current.getString(R.string.instructions_next)
                            } else {
                                androidx.compose.ui.platform.LocalContext.current.getString(R.string.instructions_start)
                            },
                            fontWeight = FontWeight.Bold
                        )
                        if (currentStep < 3) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIndicator(step: Int, currentStep: Int) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                if (step <= currentStep) Primary else SurfaceLight
            ),
        contentAlignment = Alignment.Center
    ) {
        if (step < currentStep) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = step.toString(),
                color = if (step <= currentStep) Color.White else TextSecondary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StepContent(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(40.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = description,
            fontSize = 16.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

