package com.fitform.ai.ui.screens.workout.pose

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import java.util.concurrent.atomic.AtomicBoolean

class PoseDetector(
    private val onPoseDetected: (Pose?) -> Unit
) {
    private val detector: com.google.mlkit.vision.pose.PoseDetector = PoseDetection.getClient(
        AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
            .build()
    )
    
    private val isProcessing = AtomicBoolean(false)
    private val executor = java.util.concurrent.Executors.newSingleThreadExecutor()
    
    fun createImageAnalyzer(): ImageAnalysis {
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
            .build()
        
        imageAnalysis.setAnalyzer(executor) { imageProxy ->
            processImage(imageProxy)
        }
        
        return imageAnalysis
    }
    
    private fun processImage(imageProxy: ImageProxy) {
        if (isProcessing.get()) {
            imageProxy.close()
            return
        }
        
        isProcessing.set(true)
        
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )
            
            detector.process(image)
                .addOnSuccessListener { pose ->
                    onPoseDetected(pose)
                    isProcessing.set(false)
                    imageProxy.close()
                }
                .addOnFailureListener { e ->
                    android.util.Log.e("PoseDetector", "Error detecting pose", e)
                    onPoseDetected(null)
                    isProcessing.set(false)
                    imageProxy.close()
                }
        } else {
            isProcessing.set(false)
            imageProxy.close()
        }
    }
    
    fun close() {
        detector.close()
        executor.shutdown()
    }
}
