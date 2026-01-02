package com.formsynth.ai.ui.screens.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.formsynth.ai.domain.model.*
import com.formsynth.ai.domain.repository.IExerciseRepository
import com.formsynth.ai.domain.usecase.SaveWorkoutSessionUseCase
import com.formsynth.ai.ui.screens.workout.pose.PoseAnalyzer
import com.formsynth.ai.ui.screens.workout.pose.AnalysisResult
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
            runCountdown()
            startWorkout()
        }
    }
    
    private suspend fun runCountdown() {
        for (i in COUNTDOWN_SECONDS downTo 1) {
            _countdown.value = i
            delay(COUNTDOWN_DELAY_MS)
        }
    }
    
    private fun startWorkout() {
        _workoutState.value = WorkoutState.ACTIVE
        _countdown.value = 0
        startTime = System.currentTimeMillis()
        poseAnalyzer.reset()
        startTimer()
    }
    
    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(TIMER_INTERVAL_MS)
                updateElapsedTime()
            }
        }
    }
    
    private fun updateElapsedTime() {
        _elapsedTime.value = ((System.currentTimeMillis() - startTime) / MILLIS_PER_SECOND).toInt()
    }
    
    fun analyzePose(pose: Pose?) {
        if (!canAnalyzePose(pose)) {
            resetPoseDetection()
            return
        }
        
        val result = poseAnalyzer.analyzePose(pose!!)
        updatePoseAnalysis(result)
        
        if (result.repDetected) {
            handleRepDetected(result)
        }
        
        if (hasFormIssues(result)) {
            handleFormIssues(result)
        }
    }
    
    private fun canAnalyzePose(pose: Pose?): Boolean {
        return pose != null && _workoutState.value == WorkoutState.ACTIVE
    }
    
    private fun resetPoseDetection() {
        _isPoseDetected.value = false
        _poseFeedback.value = ""
    }
    
    private fun updatePoseAnalysis(result: AnalysisResult) {
        _isPoseDetected.value = true
        _currentAngle.value = result.currentAngle
        _poseFeedback.value = result.feedback
        _currentScore.value = result.formScore
    }
    
    private fun handleRepDetected(result: AnalysisResult) {
        incrementReps()
        updateCalories()
        showRepFeedback(result.formScore, result.feedback)
    }
    
    private fun incrementReps() {
        _currentReps.value++
    }
    
    private fun updateCalories() {
        val exercise = _exercise.value ?: return
        _calories.value = calculateCalories(_currentReps.value, exercise.calories)
    }
    
    private fun calculateCalories(reps: Int, caloriesPerRep: Int): Int {
        return (reps * caloriesPerRep) / CALORIES_DIVISOR
    }
    
    private fun showRepFeedback(formScore: Int, feedback: String) {
        val feedbackType = determineFeedbackType(formScore)
        showFeedback(feedbackType, feedback)
    }
    
    private fun determineFeedbackType(formScore: Int): FeedbackType {
        return when {
            formScore >= PERFECT_SCORE_THRESHOLD -> FeedbackType.PERFECT
            formScore >= GOOD_SCORE_THRESHOLD -> FeedbackType.GOOD
            else -> FeedbackType.CORRECT_FORM
        }
    }
    
    private fun hasFormIssues(result: AnalysisResult): Boolean {
        return result.issues.isNotEmpty() && result.formScore < GOOD_SCORE_THRESHOLD
    }
    
    private fun handleFormIssues(result: AnalysisResult) {
        val issue = result.issues.first()
        showFeedback(FeedbackType.GOOD, issue.description)
    }
    
    fun pauseWorkout() {
        timerJob?.cancel()
        _workoutState.value = WorkoutState.PAUSED
    }
    
    fun resumeWorkout() {
        _workoutState.value = WorkoutState.ACTIVE
        startTimer()
    }
    
    fun addRep() {
        incrementReps()
        updateCalories()
        showRandomFeedback()
    }
    
    private fun showRandomFeedback() {
        val feedback = getRandomFeedback()
        showFeedback(feedback.first, feedback.second)
    }
    
    private fun getRandomFeedback(): Pair<FeedbackType, String> {
        val feedbackTypes = listOf(
            FeedbackType.PERFECT to "Отлично! Идеальная техника! 💪",
            FeedbackType.GOOD to "Хорошо! Продолжай! 👍",
            FeedbackType.CORRECT_FORM to "Правильная форма! ✅"
        )
        return feedbackTypes.random()
    }
    
    fun adjustScore(amount: Int) {
        _currentScore.value = (_currentScore.value + amount).coerceIn(MIN_SCORE, MAX_SCORE)
    }
    
    fun showFeedback(type: FeedbackType, message: String) {
        _feedback.value = type
        _feedbackMessage.value = message
        
        viewModelScope.launch {
            delay(FEEDBACK_DURATION_MS)
            clearFeedback()
        }
    }
    
    private fun clearFeedback() {
        _feedback.value = FeedbackType.NONE
        _feedbackMessage.value = ""
    }
    
    fun finishWorkout(): WorkoutResult {
        stopTimer()
        _workoutState.value = WorkoutState.FINISHED
        
        val result = createWorkoutResult()
        saveWorkoutSession(result)
        
        return result
    }
    
    private fun stopTimer() {
        timerJob?.cancel()
    }
    
    private fun createWorkoutResult(): WorkoutResult {
        return WorkoutResult(
            score = _currentScore.value,
            reps = _currentReps.value,
            calories = _calories.value,
            duration = _elapsedTime.value,
            exerciseId = exerciseId
        )
    }
    
    private fun saveWorkoutSession(result: WorkoutResult) {
        val session = createWorkoutSession(result)
        saveWorkoutSessionUseCase(session)
    }
    
    private fun createWorkoutSession(result: WorkoutResult): WorkoutSession {
        return WorkoutSession(
            programId = null,
            workoutId = null,
            exerciseId = exerciseId,
            startTime = startTime,
            endTime = System.currentTimeMillis(),
            totalScore = result.score,
            caloriesBurned = result.calories,
            reps = result.reps,
            duration = result.duration
        )
    }
    
    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
    
    companion object {
        private const val COUNTDOWN_SECONDS = 3
        private const val COUNTDOWN_DELAY_MS = 1000L
        private const val TIMER_INTERVAL_MS = 1000L
        private const val MILLIS_PER_SECOND = 1000
        private const val CALORIES_DIVISOR = 10
        private const val PERFECT_SCORE_THRESHOLD = 90
        private const val GOOD_SCORE_THRESHOLD = 70
        private const val MIN_SCORE = 0
        private const val MAX_SCORE = 100
        private const val FEEDBACK_DURATION_MS = 2000L
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
