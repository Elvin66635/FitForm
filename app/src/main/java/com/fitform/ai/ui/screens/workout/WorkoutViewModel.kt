package com.fitform.ai.ui.screens.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitform.ai.domain.model.*
import com.fitform.ai.domain.repository.IExerciseRepository
import com.fitform.ai.domain.usecase.SaveWorkoutSessionUseCase
import com.fitform.ai.ui.screens.workout.pose.PoseAnalyzer
import com.google.mlkit.vision.pose.Pose
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val exerciseId: String,
    private val exerciseRepository: IExerciseRepository,
    private val saveWorkoutSessionUseCase: SaveWorkoutSessionUseCase
) : ViewModel() {
    
    private val _exercise = MutableStateFlow<Exercise?>(null)
    val exercise: StateFlow<Exercise?> = _exercise.asStateFlow()
    
    private val _workoutState = MutableStateFlow(WorkoutState.READY)
    val workoutState: StateFlow<WorkoutState> = _workoutState.asStateFlow()
    
    private val _currentReps = MutableStateFlow(0)
    val currentReps: StateFlow<Int> = _currentReps.asStateFlow()
    
    private val _currentScore = MutableStateFlow(100)
    val currentScore: StateFlow<Int> = _currentScore.asStateFlow()
    
    private val _elapsedTime = MutableStateFlow(0)
    val elapsedTime: StateFlow<Int> = _elapsedTime.asStateFlow()
    
    private val _calories = MutableStateFlow(0)
    val calories: StateFlow<Int> = _calories.asStateFlow()
    
    private val _feedback = MutableStateFlow<FeedbackType>(FeedbackType.NONE)
    val feedback: StateFlow<FeedbackType> = _feedback.asStateFlow()
    
    private val _feedbackMessage = MutableStateFlow("")
    val feedbackMessage: StateFlow<String> = _feedbackMessage.asStateFlow()
    
    private val _countdown = MutableStateFlow(0)
    val countdown: StateFlow<Int> = _countdown.asStateFlow()
    
    private val _currentAngle = MutableStateFlow(0f)
    val currentAngle: StateFlow<Float> = _currentAngle.asStateFlow()
    
    private val _poseFeedback = MutableStateFlow("")
    val poseFeedback: StateFlow<String> = _poseFeedback.asStateFlow()
    
    private val _isPoseDetected = MutableStateFlow(false)
    val isPoseDetected: StateFlow<Boolean> = _isPoseDetected.asStateFlow()
    
    private var timerJob: Job? = null
    private var startTime: Long = 0
    private val poseAnalyzer = PoseAnalyzer(exerciseId)
    
    init {
        loadExercise()
    }
    
    private fun loadExercise() {
        _exercise.value = exerciseRepository.getExerciseById(exerciseId)
    }
    
    fun startCountdown() {
        _workoutState.value = WorkoutState.COUNTDOWN
        viewModelScope.launch {
            for (i in 3 downTo 1) {
                _countdown.value = i
                delay(1000)
            }
            startWorkout()
        }
    }
    
    private fun startWorkout() {
        _workoutState.value = WorkoutState.ACTIVE
        _countdown.value = 0
        startTime = System.currentTimeMillis()
        poseAnalyzer.reset()
        
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _elapsedTime.value = ((System.currentTimeMillis() - startTime) / 1000).toInt()
            }
        }
    }
    
    fun analyzePose(pose: Pose?) {
        if (pose == null || _workoutState.value != WorkoutState.ACTIVE) {
            _isPoseDetected.value = false
            _poseFeedback.value = "" // Will be localized in UI
            return
        }
        
        val result = poseAnalyzer.analyzePose(pose)
        
        _isPoseDetected.value = true
        _currentAngle.value = result.currentAngle
        _poseFeedback.value = result.feedback
        _currentScore.value = result.formScore
        
        if (result.repDetected) {
            _currentReps.value++
            val exercise = _exercise.value ?: return
            _calories.value = (_currentReps.value * exercise.calories) / 10
            
            val feedbackType = when {
                result.formScore >= 90 -> FeedbackType.PERFECT
                result.formScore >= 70 -> FeedbackType.GOOD
                else -> FeedbackType.CORRECT_FORM
            }
            showFeedback(feedbackType, result.feedback)
        }
        
        if (result.issues.isNotEmpty() && result.formScore < 70) {
            val issue = result.issues.first()
            showFeedback(FeedbackType.GOOD, issue.description)
        }
    }
    
    fun pauseWorkout() {
        timerJob?.cancel()
        _workoutState.value = WorkoutState.PAUSED
    }
    
    fun resumeWorkout() {
        _workoutState.value = WorkoutState.ACTIVE
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _elapsedTime.value = ((System.currentTimeMillis() - startTime) / 1000).toInt()
            }
        }
    }
    
    fun addRep() {
        _currentReps.value++
        val exercise = _exercise.value ?: return
        _calories.value = (_currentReps.value * exercise.calories) / 10
        
        val feedbackTypes = listOf(
            FeedbackType.PERFECT to "Отлично! Идеальная техника! 💪",
            FeedbackType.GOOD to "Хорошо! Продолжай! 👍",
            FeedbackType.CORRECT_FORM to "Правильная форма! ✅"
        )
        val (type, message) = feedbackTypes.random()
        showFeedback(type, message)
    }
    
    fun adjustScore(amount: Int) {
        _currentScore.value = (_currentScore.value + amount).coerceIn(0, 100)
    }
    
    fun showFeedback(type: FeedbackType, message: String) {
        _feedback.value = type
        _feedbackMessage.value = message
        
        viewModelScope.launch {
            delay(2000)
            _feedback.value = FeedbackType.NONE
            _feedbackMessage.value = ""
        }
    }
    
    fun finishWorkout(): WorkoutResult {
        timerJob?.cancel()
        _workoutState.value = WorkoutState.FINISHED
        
        val result = WorkoutResult(
            score = _currentScore.value,
            reps = _currentReps.value,
            calories = _calories.value,
            duration = _elapsedTime.value,
            exerciseId = exerciseId
        )
        
        val session = WorkoutSession(
            programId = null,
            workoutId = null,
            startTime = startTime,
            endTime = System.currentTimeMillis(),
            totalScore = result.score,
            caloriesBurned = result.calories
        )
        saveWorkoutSessionUseCase(session)
        
        return result
    }
    
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

enum class WorkoutState {
    READY,
    COUNTDOWN,
    ACTIVE,
    PAUSED,
    FINISHED
}

data class WorkoutResult(
    val score: Int,
    val reps: Int,
    val calories: Int,
    val duration: Int,
    val exerciseId: String
)


