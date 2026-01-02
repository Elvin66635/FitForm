package com.formsynth.ai.ui.screens.workout.camera

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.formsynth.ai.ui.screens.workout.pose.PoseDetector
import java.util.concurrent.Executors

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onPoseDetected: (com.google.mlkit.vision.pose.Pose?) -> Unit,
    isActive: Boolean = true
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var cameraProvider: ProcessCameraProvider? by remember { mutableStateOf(null) }
    var previewView: PreviewView? by remember { mutableStateOf(null) }
    var poseDetector: PoseDetector? by remember { mutableStateOf(null) }
    var imageAnalyzer: androidx.camera.core.ImageAnalysis? by remember { mutableStateOf(null) }
    
    // Initialize camera when previewView is available and active
    LaunchedEffect(previewView, isActive) {
        val currentPreviewView = previewView
        if (currentPreviewView == null || !isActive) return@LaunchedEffect
        
        try {
            val provider = ProcessCameraProvider.getInstance(context).get()
            cameraProvider = provider
            
            val detector = PoseDetector(onPoseDetected)
            poseDetector = detector
            
            val analyzer = detector.createImageAnalyzer()
            imageAnalyzer = analyzer
            
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(currentPreviewView.surfaceProvider)
            }
            
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            
            provider.unbindAll()
            provider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                analyzer
            )
        } catch (e: Exception) {
            android.util.Log.e("CameraPreview", "Camera initialization failed", e)
        }
    }
    
    // Handle active state changes
    LaunchedEffect(isActive, cameraProvider) {
        if (!isActive) {
            cameraProvider?.unbindAll()
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            cameraProvider?.unbindAll()
            poseDetector?.close()
            imageAnalyzer?.clearAnalyzer()
        }
    }
    
    Box(modifier = modifier) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).also { view ->
                    previewView = view
                    view.scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
