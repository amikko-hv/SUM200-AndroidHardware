package com.example.sum200_androidhardware

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sum200_androidhardware.ui.theme.SUM200AndroidHardwareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SUM200AndroidHardwareTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        onStartCameraActivity = { startCameraActivity() }
                    )
                }
            }
        }
    }

    private fun startCameraActivity() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onStartCameraActivity: () -> Unit
) {
    Button(
        onClick = onStartCameraActivity
    ) {
        Text("Camera Demo")
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SUM200AndroidHardwareTheme {
        MainScreen(
            modifier = Modifier,
            onStartCameraActivity = {}
        )
    }
}