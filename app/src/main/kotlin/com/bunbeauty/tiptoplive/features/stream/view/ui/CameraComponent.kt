package com.bunbeauty.tiptoplive.features.stream.view.ui

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bunbeauty.tiptoplive.common.ui.components.ImageSource
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveStreamTheme

@Composable
fun CameraComponent(
    isFront: Boolean,
    isEnabled: Boolean,
    image: ImageSource<*>,
    onCameraError: (Exception) -> Unit,
    modifier: Modifier = Modifier,
) {
    val localContext = LocalContext.current
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    CameraProvider(
        context = localContext,
        onCameraError = onCameraError
    ) { provider ->
        cameraProvider = provider
    }

    if (isEnabled && cameraProvider != null) {
        val lifecycleOwner = LocalLifecycleOwner.current
        AndroidView(
            modifier = modifier,
            factory = { context ->
                PreviewView(context).apply {
                    update(
                        isFront = isFront,
                        lifecycleOwner = lifecycleOwner,
                        cameraProvider = cameraProvider,
                        onCameraError = onCameraError,
                    )
                }
            },
            update = { previewView ->
                previewView.update(
                    isFront = isFront,
                    lifecycleOwner = lifecycleOwner,
                    cameraProvider = cameraProvider,
                    onCameraError = onCameraError,
                )
            }
        )
    } else {
        Box(
            modifier = modifier
                .background(FakeLiveStreamTheme.colors.surface),
            contentAlignment = Alignment.Center
        ) {
            AvatarImage(
                image = image,
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
private fun CameraProvider(
    context: Context,
    onCameraError: (Exception) -> Unit,
    provide: (ProcessCameraProvider) -> Unit,
) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context = context) }

    LaunchedEffect(cameraProviderFuture) {
        cameraProviderFuture.addListener(
            {
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val backCameraAvailable = cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
                    val frontCameraAvailable = cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)

                    if (backCameraAvailable || frontCameraAvailable) {
                        provide(cameraProviderFuture.get())
                    } else {
                        error("No camera available")
                    }
                } catch (exception: Exception) {
                    onCameraError(exception)
                }
            },
            ContextCompat.getMainExecutor(context)
        )
    }
}

private fun PreviewView.update(
    isFront: Boolean,
    lifecycleOwner: LifecycleOwner,
    cameraProvider: ProcessCameraProvider?,
    onCameraError: (Exception) -> Unit,
) {
    val cameraSelector = cameraProvider?.getCameraSelector(isFront = isFront) ?: return
    val preview = Preview.Builder().build().apply {
        surfaceProvider = this@update.surfaceProvider
    }
    try {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview
        )
    } catch (exception: Exception) {
        onCameraError(exception)
    }
}

fun ProcessCameraProvider.getCameraSelector(isFront: Boolean): CameraSelector? {
    val lensFacing = if (isFront) {
        if (hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            if (hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                CameraSelector.LENS_FACING_BACK
            } else {
                null
            }
        }
    } else {
        if (hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
            CameraSelector.LENS_FACING_BACK
        } else {
            if (hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                null
            }
        }
    }

    return lensFacing?.let {
        CameraSelector.Builder()
            .requireLensFacing(it)
            .build()
    }
}