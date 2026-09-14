# SUM200-AndroidHardware

Demonstration of accessing hardware features in an Android application.

## Camera

[CameraActivity](https://github.com/amikko-hv/SUM200-AndroidHardware/blob/master/app/src/main/java/com/example/sum200_androidhardware/CameraActivity.kt) 
requests user permission to access the camera. The [CameraX library](https://developer.android.com/media/camera/camerax) to access the camera to 
take pictures and display a preview.

The dependencies and permissions have been added to [AndroidManifest.xml](https://github.com/amikko-hv/SUM200-AndroidHardware/blob/master/app/src/main/AndroidManifest.xml) and [build.gradle.kts](https://github.com/amikko-hv/SUM200-AndroidHardware/blob/master/app/build.gradle.kts) 
to make use of the camera.

## Accelerometer

[ShakeActivity](https://github.com/amikko-hv/SUM200-AndroidHardware/blob/master/app/src/main/java/com/example/sum200_androidhardware/ShakeActivity.kt) 
reads motion sensor data through the accelerometer to detect device movement. The data is used to detect 
when the user shakes the device.

See the official docs for more details on [motion sensors](https://developer.android.com/develop/sensors-and-location/sensors/sensors_motion)

## GPS

[GpsActivity](https://github.com/amikko-hv/SUM200-AndroidHardware/blob/master/app/src/main/java/com/example/sum200_androidhardware/GpsActivity.kt) 
uses Android location services to access the device location. GPS provides a high-accuracy location 
source that provides information about the user's current coordinates. In this example, the location 
data is used to calculate the user's distance to another location.

See the official docs for more details on [location](https://developer.android.com/develop/sensors-and-location/location)
