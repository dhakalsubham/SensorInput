package com.example.readingsensorinfo

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor

/**
//   ReadingSensorInfo
//   Created by Subham Dhakal on 7/18/23.
//   Copyright © 2023. All rights reserved.
 */


class AccelerometerSensor(context: Context) :
    AndroidSensor(
        context = context,
        sensorFeature = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
        sensorType = Sensor.TYPE_ACCELEROMETER
    )

class MagnetometerSensor(context: Context) :
    AndroidSensor(
        context = context,
        sensorFeature = PackageManager.FEATURE_SENSOR_GYROSCOPE,
        sensorType = Sensor.TYPE_MAGNETIC_FIELD
    )

