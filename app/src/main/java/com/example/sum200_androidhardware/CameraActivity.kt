package com.example.sum200_androidhardware

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.sum200_androidhardware.ui.theme.SUM200AndroidHardwareTheme

class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request camera permission
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        }

        enableEdgeToEdge()
        setContent {
            SUM200AndroidHardwareTheme {
                CameraScreen()
            }
        }
    }
}


@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val picture = remember { mutableStateOf<Bitmap?>(null) }
    val onPictureTaken = { bitmap: Bitmap ->
        picture.value = bitmap
    }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setCameraSelector(CameraSelector.DEFAULT_BACK_CAMERA)
            bindToLifecycle(lifecycleOwner)
        }
    }

    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val pictureValue = picture.value
            if (pictureValue != null) {
                CameraState(picture)
            } else {
                PictureDisplayState(context, cameraController, onPictureTaken)
            }
        }
    }
}

@Composable
fun PictureDisplayState(
    context: Context,
    cameraController: LifecycleCameraController,
    onPictureTaken: (Bitmap) -> Unit
) {
    Button(
        onClick = {
            takePicture(context, cameraController, onPictureTaken)
        }
    ) {
        Text("Take Picture")
    }
    CameraPreview(
        modifier = Modifier.fillMaxSize(),
        cameraController = cameraController
    )
}

@Composable
fun CameraState(
    picture: MutableState<Bitmap?>
) {
    Button(
        onClick = {
            picture.value = null
        }
    ) {
        Text("New Picture")
    }
    Image(
        bitmap = picture.value!!.asImageBitmap(),
        "Picture taken"
    )
}

@Composable
fun CameraPreview(modifier: Modifier = Modifier, cameraController: CameraController) {
    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                controller = cameraController
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = modifier
    )
}

fun takePicture(context: Context, cameraController: CameraController, onPictureTaken: (Bitmap) -> Unit ) {
    val executor = ContextCompat.getMainExecutor(context)
    cameraController.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            super.onCaptureSuccess(image)
            val bitmap = image.toBitmap()
            onPictureTaken(bitmap)
        }
    })
}
