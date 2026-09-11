package com.example.sum200_androidhardware

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.getSystemService
import com.example.sum200_androidhardware.ui.theme.SUM200AndroidHardwareTheme
import kotlin.math.sqrt
import kotlin.random.Random

class ShakeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SUM200AndroidHardwareTheme {
                ShakeScreen()
            }
        }
    }
}

@Composable
fun ShakeScreen() {
    // Get the current context to access system services.
    val context = LocalContext.current

    // Set up state variables to track the background color and shake count.
    var backgroundColor by remember { mutableStateOf(androidx.compose.ui.graphics.Color(0xFF6495ED)) }
    var shakeCount by remember { mutableStateOf(0) }

    // Get the accelerometer sensor if it exists.
    val sensorManager = context.getSystemService<SensorManager>()
    val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Keep the last shake time so repeated sensor events do not trigger too quickly.
    var lastShakeTime by remember { mutableLongStateOf(0L) }

    // Register the sensor listener while this screen is visible and clean it up when it leaves.
    DisposableEffect(accelerometer) {
        if (sensorManager == null || accelerometer == null) {
            onDispose { }
        }

        // Create a listener that will respond to accelerometer events.
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                // Ignore sensor events that arrive too close together.
                val now = System.currentTimeMillis()
                if (now - lastShakeTime < 500) {
                    return
                }

                // Grab the acceleration values from the sensor.
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                // gForce is the magnitude of the acceleration vector divided by the Earth's gravity.
                val gForce = sqrt((x * x + y * y + z * z).toDouble()) / SensorManager.GRAVITY_EARTH

                // Trigger a shake if the gForce exceeds a threshold value.
                if (gForce > 2.5) {
                    lastShakeTime = now
                    shakeCount += 1
                    backgroundColor = randomColor()
                }
            }

            // We don't care about accuracy changes for this demo.
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        // Register the listener with the sensor manager.
        sensorManager?.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)

        // Clean up when this screen is no longer visible.
        onDispose {
            sensorManager?.unregisterListener(listener)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .fillMaxSize()
                .background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Shake me!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.Black
            )
            Text(
                text = "Shakes detected: $shakeCount",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.Black
            )
        }
    }
}

/**
 * Generates a random color with RGB values between 80 and 255 to avoid very dark colors.
 */
private fun randomColor(): androidx.compose.ui.graphics.Color {
    return androidx.compose.ui.graphics.Color(
        red = Random.nextInt(80, 256),
        green = Random.nextInt(80, 256),
        blue = Random.nextInt(80, 256)
    )
}
