package com.example.sum200_androidhardware

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.sum200_androidhardware.ui.theme.SUM200AndroidHardwareTheme

class GpsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SUM200AndroidHardwareTheme {
                GpsScreen()
            }
        }
    }
}

@Composable
fun GpsScreen() {
    val context = LocalContext.current

    // Get the system LocationManager service to request GPS updates.
    val locationManager = remember {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    // Track whether the app currently has permission to read the device location.
    var hasLocationPermission by remember {
        mutableStateOf(hasLocationPermission(context))
    }

    // Store the most recent location so the screen can update automatically.
    var currentLocation by remember { mutableStateOf<Location?>(null) }

    // Ask the user for location permission and update the screen based on the answer.
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        hasLocationPermission = hasLocationPermission(context)
    }

    // Request permission automatically the first time this screen opens.
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Start GPS updates only while the screen is visible, then stop listening on cleanup.
    DisposableEffect(hasLocationPermission) {
        if (!hasLocationPermission) {
            currentLocation = null
            onDispose { }
        } else {
            val listener = startLocationUpdates(
                locationManager = locationManager,
                onLocationChanged = { location ->
                    currentLocation = location
                },
                onProviderDisabled = { currentLocation = null }
            )

            onDispose {
                locationManager.removeUpdates(listener)
            }
        }
    }

    // Calculate the distance to the Trollhättan Railway Station if we have a current location.
    val distanceToStation = currentLocation?.distanceTo(trollhattanRailwayStationLocation())
    val statusText = when {
        !hasLocationPermission -> "Location permission is required to read GPS coordinates"
        currentLocation == null -> "Looking for your current position"
        else -> "Current GPS position"
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("GPS Demo")
            Text(statusText)

            if (currentLocation == null) {
                Text("Latitude: --")
                Text("Longitude: --")
                Text("Distance to Trollhättan Railway Station: --")
            } else {
                val location = currentLocation!!
                Text("Latitude: ${location.latitude}")
                Text("Longitude: ${location.longitude}")
                Text("Distance to Trollhättan Railway Station: ${distanceToStation?.toInt()} meters")
            }
        }
    }
}

// Permission is verified above via hasFineLocationPermission before requesting updates,
// but lint cannot trace that check across function calls, so we suppress its warning here.
@SuppressLint("MissingPermission")
private fun startLocationUpdates(
    locationManager: LocationManager,
    onLocationChanged: (Location) -> Unit,
    onProviderDisabled: () -> Unit
): LocationListener {
    // Create a listener that forwards GPS updates back to the composable.
    val listener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            onLocationChanged(location)
        }

        override fun onProviderDisabled(provider: String) {
            onProviderDisabled()
        }
    }

    // Show a previously known location first so the screen can show data immediately
    // while waiting for a fresh location update.
    val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    if (lastKnownLocation != null) {
        onLocationChanged(lastKnownLocation)
    }

    // Ask Android to send new GPS updates about once per second or when the phone moves
    // at least one meter.
    locationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        1000L,
        1f,
        listener
    )

    // Return the listener so the caller can remove updates later in onDispose.
    return listener
}

/**
 * Check whether the app has permission to read the device location.
 */
private fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Return a Location object representing the Trollhättan Railway Station.
 */
private fun trollhattanRailwayStationLocation(): Location {
    return Location("TrollhattanRailwayStation").apply {
        latitude = 58.28755279381991
        longitude = 12.298600728835762
    }
}

@Preview(showBackground = true)
@Composable
fun GpsScreenPreview() {
    SUM200AndroidHardwareTheme {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("GPS Demo")
                Text("Looking for your current position")
                Text("Latitude: 58.2838")
                Text("Longitude: 12.2886")
                Text("Distance to Trollhättan Railway Station: 950 meters")
            }
        }
    }
}