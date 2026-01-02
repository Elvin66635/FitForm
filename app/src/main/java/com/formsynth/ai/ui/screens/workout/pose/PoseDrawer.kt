package com.formsynth.ai.ui.screens.workout.pose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

/**
 * Draws pose skeleton overlay on camera preview
 */
@Composable
fun PoseOverlay(
    pose: Pose?,
    modifier: Modifier = Modifier,
    imageWidth: Float,
    imageHeight: Float,
    canvasWidth: Float,
    canvasHeight: Float,
    mirrorHorizontally: Boolean = true // Mirror for front camera
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (pose == null) return@Canvas
        
        val scaleX = canvasWidth / imageWidth
        val scaleY = canvasHeight / imageHeight
        
        // Helper function to mirror X coordinate if needed
        fun mirrorX(x: Float): Float {
            return if (mirrorHorizontally) {
                canvasWidth - x
            } else {
                x
            }
        }
        
        // Draw connections between landmarks
        val connections = listOf(
            // Face
            Pair(PoseLandmark.LEFT_EAR, PoseLandmark.LEFT_EYE),
            Pair(PoseLandmark.LEFT_EYE, PoseLandmark.NOSE),
            Pair(PoseLandmark.NOSE, PoseLandmark.RIGHT_EYE),
            Pair(PoseLandmark.RIGHT_EYE, PoseLandmark.RIGHT_EAR),
            
            // Upper body
            Pair(PoseLandmark.LEFT_SHOULDER, PoseLandmark.RIGHT_SHOULDER),
            Pair(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_ELBOW),
            Pair(PoseLandmark.LEFT_ELBOW, PoseLandmark.LEFT_WRIST),
            Pair(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_ELBOW),
            Pair(PoseLandmark.RIGHT_ELBOW, PoseLandmark.RIGHT_WRIST),
            
            // Torso
            Pair(PoseLandmark.LEFT_SHOULDER, PoseLandmark.LEFT_HIP),
            Pair(PoseLandmark.RIGHT_SHOULDER, PoseLandmark.RIGHT_HIP),
            Pair(PoseLandmark.LEFT_HIP, PoseLandmark.RIGHT_HIP),
            
            // Lower body
            Pair(PoseLandmark.LEFT_HIP, PoseLandmark.LEFT_KNEE),
            Pair(PoseLandmark.LEFT_KNEE, PoseLandmark.LEFT_ANKLE),
            Pair(PoseLandmark.RIGHT_HIP, PoseLandmark.RIGHT_KNEE),
            Pair(PoseLandmark.RIGHT_KNEE, PoseLandmark.RIGHT_ANKLE),
            
            // Feet
            Pair(PoseLandmark.LEFT_ANKLE, PoseLandmark.LEFT_HEEL),
            Pair(PoseLandmark.LEFT_HEEL, PoseLandmark.LEFT_FOOT_INDEX),
            Pair(PoseLandmark.RIGHT_ANKLE, PoseLandmark.RIGHT_HEEL),
            Pair(PoseLandmark.RIGHT_HEEL, PoseLandmark.RIGHT_FOOT_INDEX)
        )
        
        // Draw lines
        connections.forEach { (start, end) ->
            val startLandmark = pose.getPoseLandmark(start)
            val endLandmark = pose.getPoseLandmark(end)
            
            if (startLandmark != null && endLandmark != null) {
                val startX = mirrorX(startLandmark.position.x * scaleX)
                val startY = startLandmark.position.y * scaleY
                val endX = mirrorX(endLandmark.position.x * scaleX)
                val endY = endLandmark.position.y * scaleY
                
                // Color based on confidence
                val avgConfidence = (startLandmark.inFrameLikelihood + endLandmark.inFrameLikelihood) / 2
                val lineColor = when {
                    avgConfidence > 0.7f -> Color(0xFF00FF88) // Green
                    avgConfidence > 0.5f -> Color(0xFFFFAA00) // Orange
                    else -> Color(0xFFFF4444) // Red
                }
                
                drawLine(
                    color = lineColor,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )
            }
        }
        
        // Draw landmark points
        pose.allPoseLandmarks.forEach { landmark ->
            val x = mirrorX(landmark.position.x * scaleX)
            val y = landmark.position.y * scaleY
            
            // Outer circle (white)
            drawCircle(
                color = Color.White,
                radius = 12f,
                center = Offset(x, y)
            )
            
            // Inner circle (colored based on confidence)
            val pointColor = when {
                landmark.inFrameLikelihood > 0.7f -> Color(0xFF00FF88)
                landmark.inFrameLikelihood > 0.5f -> Color(0xFFFFAA00)
                else -> Color(0xFFFF4444)
            }
            
            drawCircle(
                color = pointColor,
                radius = 8f,
                center = Offset(x, y)
            )
        }
        
        // Highlight key points
        val keyPoints = listOf(
            PoseLandmark.LEFT_SHOULDER,
            PoseLandmark.RIGHT_SHOULDER,
            PoseLandmark.LEFT_ELBOW,
            PoseLandmark.RIGHT_ELBOW,
            PoseLandmark.LEFT_HIP,
            PoseLandmark.RIGHT_HIP,
            PoseLandmark.LEFT_KNEE,
            PoseLandmark.RIGHT_KNEE
        )
        
        keyPoints.forEach { landmarkType ->
            val landmark = pose.getPoseLandmark(landmarkType)
            if (landmark != null) {
                val x = mirrorX(landmark.position.x * scaleX)
                val y = landmark.position.y * scaleY
                
                // Draw outer ring for key points
                drawCircle(
                    color = Color(0xFF00D9FF),
                    radius = 16f,
                    center = Offset(x, y),
                    style = Stroke(width = 3f)
                )
            }
        }
    }
}

