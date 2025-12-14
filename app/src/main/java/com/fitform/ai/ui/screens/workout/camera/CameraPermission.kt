package com.fitform.ai.ui.screens.workout.camera

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberCameraPermissionState(): Pair<Boolean, () -> Unit> {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(checkCameraPermission(context)) }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }
    
    val requestPermission: () -> Unit = {
        launcher.launch(Manifest.permission.CAMERA)
    }
    
    return Pair(hasPermission, requestPermission)
}

fun checkCameraPermission(context: Context): Boolean {
    return android.content.pm.PackageManager.PERMISSION_GRANTED ==
            androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            )
}
