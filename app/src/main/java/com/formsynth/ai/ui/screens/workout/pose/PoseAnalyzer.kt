package com.formsynth.ai.ui.screens.workout.pose

import com.google.mlkit.vision.common.PointF3D
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.*

data class AnalysisResult(
    val repDetected: Boolean,
    val currentAngle: Float,
    val feedback: String,
    val isCorrectForm: Boolean,
    val formScore: Int = 100,
    val issues: List<FormIssue> = emptyList()
)

data class FormIssue(
    val type: IssueType,
    val severity: IssueSeverity,
    val description: String
)

enum class IssueType {
    KNEE_ALIGNMENT,
    BACK_POSITION,
    DEPTH,
    SYMMETRY,
    ELBOW_FLARE,
    HIP_POSITION,
    STABILITY
}

enum class IssueSeverity {
    MINOR,
    MODERATE,
    MAJOR
}

enum class RepState {
    NONE,
    DOWN,
    UP
}

class PoseAnalyzer(private val exerciseId: String) {
    
    private var repState = RepState.NONE
    private var repCount = 0
    private var lastAngle = 0f
    
    fun reset() {
        repState = RepState.NONE
        repCount = 0
        lastAngle = 0f
    }
    
    fun analyzePose(pose: Pose): AnalysisResult {
        return try {
            when (exerciseId) {
                "barbell_bench_press", "dumbbell_bench_press", "incline_dumbbell_press", 
                "close_grip_bench_press" -> analyzeBenchPress(pose)
                "pushup", "dips_chest", "dumbbell_flyes", "pec_deck" -> analyzePushup(pose)
                
                "pullup" -> analyzePullup(pose)
                "lat_pulldown", "seated_row", "dumbbell_row", "barbell_row", 
                "hyperextension" -> analyzeRow(pose)
                
                "squat", "barbell_squat", "leg_press" -> analyzeSquat(pose)
                "lunge" -> analyzeLunge(pose)
                "leg_curl", "leg_extension" -> analyzeGeneric(pose)
                "calf_raise" -> analyzeCalfRaise(pose)
                
                "dumbbell_shoulder_press", "military_press", "lateral_raise", 
                "rear_delt_fly", "upright_row" -> analyzeGeneric(pose)
                
                "barbell_curl", "dumbbell_curl", "hammer_curl", "scott_curl", 
                "cable_curl" -> analyzeGeneric(pose)
                
                "tricep_extension", "tricep_pushdown", "dips_tricep", 
                "tricep_dip", "overhead_tricep_extension" -> analyzeTricepDip(pose)
                
                "plank" -> analyzePlank(pose)
                "crunch", "ball_crunch" -> analyzeCrunch(pose)
                "hanging_leg_raise" -> analyzeLegRaise(pose)
                "russian_twist" -> analyzeBicycleCrunch(pose)
                
                "running", "treadmill" -> analyzeCardio(pose)
                "burpee" -> analyzeBurpee(pose)
                "jump_rope" -> analyzeJumpRope(pose)
                "bike", "rowing_machine" -> analyzeGeneric(pose)
                
                else -> analyzeGeneric(pose)
            }
        } catch (e: Exception) {
            AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
    }
    
    private fun analyzeSquat(pose: Pose): AnalysisResult {
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        
        if (leftHip == null || leftKnee == null || leftAnkle == null || leftShoulder == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val kneeAngle = calculateAngle(leftHip.position3D, leftKnee.position3D, leftAnkle.position3D)
        val backAngle = calculateAngle(leftShoulder.position3D, leftHip.position3D, leftKnee.position3D)
        
        val issues = mutableListOf<FormIssue>()
        var formScore = 100
        
        if (backAngle < 140) {
            issues.add(FormIssue(IssueType.BACK_POSITION, IssueSeverity.MAJOR, "Держите спину прямой!"))
            formScore -= 30
        } else if (backAngle < 150) {
            issues.add(FormIssue(IssueType.BACK_POSITION, IssueSeverity.MODERATE, "Выпрямите спину"))
            formScore -= 15
        }
        
        if (kneeAngle > 120) {
            issues.add(FormIssue(IssueType.DEPTH, IssueSeverity.MODERATE, "Опуститесь ниже"))
            formScore -= 15
        } else if (kneeAngle < 70) {
            issues.add(FormIssue(IssueType.DEPTH, IssueSeverity.MINOR, "Слишком глубоко"))
            formScore -= 10
        }
        
        val repDetected = detectSquatRep(kneeAngle)
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = kneeAngle,
            feedback = if (issues.isEmpty()) "Отлично!" else issues.first().description,
            isCorrectForm = formScore >= 70,
            formScore = formScore.coerceIn(0, 100),
            issues = issues
        )
    }
    
    private fun analyzePushup(pose: Pose): AnalysisResult {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        
        if (leftShoulder == null || leftElbow == null || leftWrist == null || leftHip == null || leftKnee == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val elbowAngle = calculateAngle(leftShoulder.position3D, leftElbow.position3D, leftWrist.position3D)
        val bodyAngle = calculateAngle(leftShoulder.position3D, leftHip.position3D, leftKnee.position3D)
        
        val issues = mutableListOf<FormIssue>()
        var formScore = 100
        
        if (bodyAngle < 160) {
            issues.add(FormIssue(IssueType.BACK_POSITION, IssueSeverity.MAJOR, "Держите тело прямым!"))
            formScore -= 25
        }
        
        if (elbowAngle > 140) {
            issues.add(FormIssue(IssueType.DEPTH, IssueSeverity.MODERATE, "Опуститесь ниже"))
            formScore -= 15
        }
        
        val repDetected = detectPushupRep(elbowAngle)
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = elbowAngle,
            feedback = if (issues.isEmpty()) "Отлично!" else issues.first().description,
            isCorrectForm = formScore >= 70,
            formScore = formScore.coerceIn(0, 100),
            issues = issues
        )
    }
    
    private fun analyzePlank(pose: Pose): AnalysisResult {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        
        if (leftShoulder == null || leftHip == null || leftKnee == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val bodyAngle = calculateAngle(leftShoulder.position3D, leftHip.position3D, leftKnee.position3D)
        val issues = mutableListOf<FormIssue>()
        var formScore = 100
        
        if (bodyAngle < 160) {
            issues.add(FormIssue(IssueType.HIP_POSITION, IssueSeverity.MAJOR, "Поднимите таз!"))
            formScore -= 30
        } else if (bodyAngle > 185) {
            issues.add(FormIssue(IssueType.HIP_POSITION, IssueSeverity.MODERATE, "Опустите таз"))
            formScore -= 15
        }
        
        return AnalysisResult(
            repDetected = false,
            currentAngle = bodyAngle,
            feedback = if (issues.isEmpty()) "Держите планку" else issues.first().description,
            isCorrectForm = formScore >= 70,
            formScore = formScore.coerceIn(0, 100),
            issues = issues
        )
    }
    
    private fun analyzeLunge(pose: Pose): AnalysisResult {
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        
        if (leftHip == null || leftKnee == null || leftAnkle == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val angle = calculateAngle(leftHip.position3D, leftKnee.position3D, leftAnkle.position3D)
        val repDetected = detectSquatRep(angle)
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = angle,
            feedback = "Выполняйте выпады",
            isCorrectForm = true,
            formScore = 85
        )
    }
    
    private fun analyzeDeadlift(pose: Pose): AnalysisResult {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        
        if (leftShoulder == null || leftHip == null || leftKnee == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val hipAngle = calculateAngle(leftShoulder.position3D, leftHip.position3D, leftKnee.position3D)
        val repDetected = detectDeadliftRep(hipAngle)
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = hipAngle,
            feedback = "Держите спину прямой",
            isCorrectForm = true,
            formScore = 80
        )
    }
    
    private fun analyzeBenchPress(pose: Pose): AnalysisResult {
        return analyzePushup(pose)
    }
    
    private fun analyzeCardio(pose: Pose): AnalysisResult {
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        
        if (leftHip == null || leftKnee == null || rightKnee == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val kneeHeightDiff = abs(leftKnee.position.y - rightKnee.position.y)
        val repDetected = kneeHeightDiff > 30
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = kneeHeightDiff,
            feedback = "Двигайтесь активно!",
            isCorrectForm = true,
            formScore = 85
        )
    }
    
    private fun analyzeJumpingJacks(pose: Pose): AnalysisResult {
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        
        if (leftWrist == null || rightWrist == null || leftShoulder == null || rightShoulder == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val armWidth = abs(leftWrist.position.x - rightWrist.position.x)
        val shoulderWidth = abs(leftShoulder.position.x - rightShoulder.position.x)
        val repDetected = armWidth > shoulderWidth * 1.5f
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = armWidth,
            feedback = "Разводите руки и ноги",
            isCorrectForm = true,
            formScore = 80
        )
    }
    
    private fun analyzeBurpee(pose: Pose): AnalysisResult {
        return analyzeSquat(pose)
    }
    
    private fun analyzeMountainClimber(pose: Pose): AnalysisResult {
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        
        if (leftKnee == null || rightKnee == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val kneeHeightDiff = abs(leftKnee.position.y - rightKnee.position.y)
        val repDetected = kneeHeightDiff > 40
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = kneeHeightDiff,
            feedback = "Чередуйте ноги быстро",
            isCorrectForm = true,
            formScore = 80
        )
    }
    
    private fun analyzeJumpRope(pose: Pose): AnalysisResult {
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        
        if (leftAnkle == null || rightAnkle == null || leftKnee == null || rightKnee == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val ankleHeight = (leftAnkle.position.y + rightAnkle.position.y) / 2
        val kneeHeight = (leftKnee.position.y + rightKnee.position.y) / 2
        val repDetected = ankleHeight < kneeHeight - 20
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = ankleHeight,
            feedback = "Прыгайте на носках",
            isCorrectForm = true,
            formScore = 85
        )
    }
    
    private fun analyzePullup(pose: Pose): AnalysisResult {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        
        if (leftShoulder == null || leftElbow == null || leftWrist == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте боком к камере",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val angle = calculateAngle(leftShoulder.position3D, leftElbow.position3D, leftWrist.position3D)
        val repDetected = detectPullRep(angle)
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = angle,
            feedback = "Тяните к подбородку",
            isCorrectForm = true,
            formScore = 80
        )
    }
    
    private fun analyzeRow(pose: Pose): AnalysisResult {
        return analyzePullup(pose)
    }
    
    private fun analyzeCrunch(pose: Pose): AnalysisResult {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        
        if (leftShoulder == null || leftHip == null || leftKnee == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val angle = calculateAngle(leftShoulder.position3D, leftHip.position3D, leftKnee.position3D)
        val repDetected = detectSquatRep(angle)
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = angle,
            feedback = "Скручивайтесь",
            isCorrectForm = true,
            formScore = 80
        )
    }
    
    private fun analyzeLegRaise(pose: Pose): AnalysisResult {
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        
        if (leftHip == null || leftKnee == null || leftAnkle == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val angle = calculateAngle(leftHip.position3D, leftKnee.position3D, leftAnkle.position3D)
        val repDetected = detectSquatRep(angle)
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = angle,
            feedback = "Поднимайте ноги",
            isCorrectForm = true,
            formScore = 80
        )
    }
    
    private fun analyzeBicycleCrunch(pose: Pose): AnalysisResult {
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        
        if (leftKnee == null || rightKnee == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val kneeDistance = abs(leftKnee.position.y - rightKnee.position.y)
        val repDetected = kneeDistance > 50
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = kneeDistance,
            feedback = "Чередуйте ноги",
            isCorrectForm = true,
            formScore = 80
        )
    }
    
    private fun analyzeJumpExercise(pose: Pose): AnalysisResult {
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        
        if (leftHip == null || leftKnee == null || leftAnkle == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val angle = calculateAngle(leftHip.position3D, leftKnee.position3D, leftAnkle.position3D)
        val ankleHeight = leftAnkle.position.y
        val hipHeight = leftHip.position.y
        val isJumping = ankleHeight < hipHeight - 50
        
        val repDetected = isJumping && detectSquatRep(angle)
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = angle,
            feedback = "Прыгайте взрывно!",
            isCorrectForm = true,
            formScore = 80
        )
    }
    
    private fun analyzeHipThrust(pose: Pose): AnalysisResult {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        
        if (leftShoulder == null || leftHip == null || leftKnee == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val angle = calculateAngle(leftShoulder.position3D, leftHip.position3D, leftKnee.position3D)
        val repDetected = detectSquatRep(angle)
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = angle,
            feedback = "Поднимайте таз",
            isCorrectForm = true,
            formScore = 80
        )
    }
    
    private fun analyzeCalfRaise(pose: Pose): AnalysisResult {
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        
        if (leftAnkle == null || leftKnee == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val ankleHeight = leftAnkle.position.y
        val repDetected = detectCalfRaiseRep(ankleHeight)
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = ankleHeight,
            feedback = "Поднимайтесь на носки",
            isCorrectForm = true,
            formScore = 85
        )
    }
    
    private fun analyzeTricepDip(pose: Pose): AnalysisResult {
        return analyzePushup(pose)
    }
    
    private fun analyzeShoulderTap(pose: Pose): AnalysisResult {
        return analyzePlank(pose)
    }
    
    private fun analyzeFullBody(pose: Pose): AnalysisResult {
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        
        if (leftHip == null || leftKnee == null || leftAnkle == null || 
            leftShoulder == null || leftElbow == null || leftWrist == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val kneeAngle = calculateAngle(leftHip.position3D, leftKnee.position3D, leftAnkle.position3D)
        val elbowAngle = calculateAngle(leftShoulder.position3D, leftElbow.position3D, leftWrist.position3D)
        val bodyAngle = calculateAngle(leftShoulder.position3D, leftHip.position3D, leftKnee.position3D)
        
        val hasMovement = abs(kneeAngle - lastAngle) > 10 || abs(elbowAngle - lastAngle) > 10
        val repDetected = hasMovement && (kneeAngle < 120 || elbowAngle < 120)
        
        lastAngle = (kneeAngle + elbowAngle) / 2
        
        val issues = mutableListOf<FormIssue>()
        var formScore = 100
        
        if (bodyAngle < 150) {
            issues.add(FormIssue(IssueType.BACK_POSITION, IssueSeverity.MODERATE, "Держите тело прямым"))
            formScore -= 15
        }
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = (kneeAngle + elbowAngle) / 2,
            feedback = if (issues.isEmpty()) "Отлично! Продолжайте!" else issues.first().description,
            isCorrectForm = formScore >= 70,
            formScore = formScore.coerceIn(0, 100),
            issues = issues
        )
    }
    
    private fun analyzeYogaPose(pose: Pose): AnalysisResult {
        return AnalysisResult(
            repDetected = false,
            currentAngle = 0f,
            feedback = "Держите позу",
            isCorrectForm = true,
            formScore = 90
        )
    }
    
    private fun analyzeGeneric(pose: Pose): AnalysisResult {
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
        
        if (leftHip == null || leftKnee == null || leftAnkle == null) {
            return AnalysisResult(
                repDetected = false,
                currentAngle = 0f,
                feedback = "Встаньте в кадр",
                isCorrectForm = false,
                formScore = 0
            )
        }
        
        val angle = calculateAngle(leftHip.position3D, leftKnee.position3D, leftAnkle.position3D)
        val repDetected = detectSquatRep(angle)
        
        return AnalysisResult(
            repDetected = repDetected,
            currentAngle = angle,
            feedback = "Выполняйте упражнение",
            isCorrectForm = true,
            formScore = 70
        )
    }
    
    private fun detectSquatRep(angle: Float): Boolean {
        when (repState) {
            RepState.NONE -> {
                if (angle < 120) {
                    repState = RepState.DOWN
                }
            }
            RepState.DOWN -> {
                if (angle > 150) {
                    repState = RepState.UP
                    repCount++
                    repState = RepState.NONE
                    return true
                }
            }
            RepState.UP -> {
                repState = RepState.NONE
            }
        }
        return false
    }
    
    private fun detectPushupRep(angle: Float): Boolean {
        when (repState) {
            RepState.NONE -> {
                if (angle < 120) {
                    repState = RepState.DOWN
                }
            }
            RepState.DOWN -> {
                if (angle > 150) {
                    repState = RepState.UP
                    repCount++
                    repState = RepState.NONE
                    return true
                }
            }
            RepState.UP -> {
                repState = RepState.NONE
            }
        }
        return false
    }
    
    private fun detectDeadliftRep(angle: Float): Boolean {
        return detectSquatRep(angle)
    }
    
    private fun detectPullRep(angle: Float): Boolean {
        when (repState) {
            RepState.NONE -> {
                if (angle > 150) {
                    repState = RepState.DOWN
                }
            }
            RepState.DOWN -> {
                if (angle < 120) {
                    repState = RepState.UP
                    repCount++
                    repState = RepState.NONE
                    return true
                }
            }
            RepState.UP -> {
                repState = RepState.NONE
            }
        }
        return false
    }
    
    private fun detectCalfRaiseRep(ankleHeight: Float): Boolean {
        val threshold = 20f
        when (repState) {
            RepState.NONE -> {
                if (ankleHeight < lastAngle - threshold) {
                    repState = RepState.DOWN
                }
            }
            RepState.DOWN -> {
                if (ankleHeight > lastAngle + threshold) {
                    repState = RepState.UP
                    repCount++
                    repState = RepState.NONE
                    lastAngle = ankleHeight
                    return true
                }
            }
            RepState.UP -> {
                repState = RepState.NONE
            }
        }
        lastAngle = ankleHeight
        return false
    }
    
    private fun calculateAngle(
        point1: PointF3D,
        point2: PointF3D,
        point3: PointF3D
    ): Float {
        val v1x = point1.x - point2.x
        val v1y = point1.y - point2.y
        val v1z = point1.z - point2.z
        
        val v2x = point3.x - point2.x
        val v2y = point3.y - point2.y
        val v2z = point3.z - point2.z
        
        val dot = v1x * v2x + v1y * v2y + v1z * v2z
        
        val mag1 = sqrt(v1x * v1x + v1y * v1y + v1z * v1z)
        val mag2 = sqrt(v2x * v2x + v2y * v2y + v2z * v2z)
        
        if (mag1 == 0f || mag2 == 0f) return 0f
        
        val cosAngle = dot / (mag1 * mag2)
        return Math.toDegrees(acos(cosAngle.coerceIn(-1f, 1f).toDouble())).toFloat()
    }
    
    private fun calculateDistance(point1: PointF3D, point2: PointF3D): Float {
        val dx = point1.x - point2.x
        val dy = point1.y - point2.y
        val dz = point1.z - point2.z
        return sqrt(dx * dx + dy * dy + dz * dz)
    }
}
