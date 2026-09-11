package com.example.sum200_androidhardware

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sum200_androidhardware.ui.theme.SUM200AndroidHardwareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SUM200AndroidHardwareTheme {
                MainScreen(
                    onStartCameraActivity = { startCameraActivity() },
                    onStartShakeActivity = { startShakeActivity() },
                    onStartGpsActivity = { startGpsActivity() }
                )
            }
        }
    }

    private fun startCameraActivity() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    private fun startShakeActivity() {
        val intent = Intent(this, ShakeActivity::class.java)
        startActivity(intent)
    }

    private fun startGpsActivity() {
        val intent = Intent(this, GpsActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun MainScreen(
    onStartCameraActivity: () -> Unit,
    onStartShakeActivity: () -> Unit,
    onStartGpsActivity: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = onStartCameraActivity
            ) {
                Text("Camera Demo")
            }

            Button(
                onClick = onStartShakeActivity
            ) {
                Text("Shake Demo")
            }

            Button(
                onClick = onStartGpsActivity
            ) {
                Text("GPS Demo")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SUM200AndroidHardwareTheme {
        MainScreen(
            onStartCameraActivity = {},
            onStartShakeActivity = {},
            onStartGpsActivity = {}
        )
    }
}