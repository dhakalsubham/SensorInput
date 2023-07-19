package com.example.readingsensorinfo

import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
//   ReadingSensorInfo
//   Created by Subham Dhakal on 7/18/23.
//   Copyright © 2023. All rights reserved.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    @SensorOne private val accelerometerSensor: MeasureableSensor,
    @SensorTwo private val magnetometerSensor: MeasureableSensor
) : ViewModel() {
    private var _aValues: MutableStateFlow<List<Float>> = MutableStateFlow(listOf())
    var aValues: StateFlow<List<Float>> = _aValues
    private var _mValues: MutableStateFlow<List<Float>> = MutableStateFlow(listOf())
    var mValues: StateFlow<List<Float>> = _mValues

    private var _orientation: MutableStateFlow<List<Float>> = MutableStateFlow(listOf(0.0f, 0.0f, 0.0f))
    var orientation: StateFlow<List<Float>> = _orientation

    private var _blendFactor: MutableStateFlow<List<Float>> = MutableStateFlow(listOf(0.0f, 0.0f, 0.0f))
    var blendFactor: StateFlow<List<Float>> = _blendFactor

    private var _dataHashMap: MutableStateFlow<HashMap<String, MutableList<Float>>> = MutableStateFlow(hashMapOf())
    var dataHashMap: MutableStateFlow<HashMap<String, MutableList<Float>>> = _dataHashMap

    private val rotationMatrix = FloatArray(9)
    private val orientationValues = FloatArray(3)


    init {
        accelerometerSensor.startListening()
        accelerometerSensor.setOnSensorValuesChangedListener { values ->
            _aValues.value = values
            computeOrientation()
        }
        magnetometerSensor.startListening()
        magnetometerSensor.setOnSensorValuesChangedListener { values ->
            _mValues.value = values
            computeOrientation()
        }
    }

    private fun computeOrientation() {
        if (aValues.value.isEmpty() || mValues.value.isEmpty()) {
            return
        }

        SensorManager.getRotationMatrix(
            rotationMatrix,
            null, aValues.value.toFloatArray(),
            mValues.value.toFloatArray()
        )

        // Compute the orientation values in radians
        SensorManager.getOrientation(rotationMatrix, orientationValues)
        val blendFactor1 = (Math.toDegrees(orientationValues[0].toDouble()).toFloat() / 10).coerceIn(0f, 1f)
        val blendFactor2 = (Math.toDegrees(orientationValues[1].toDouble()).toFloat() / 10).coerceIn(0f, 1f)
        val blendFactor3 = (Math.toDegrees(orientationValues[2].toDouble()).toFloat() / 10).coerceIn(0f, 1f)

        _blendFactor.value = listOf(blendFactor1, blendFactor2, blendFactor3)


        //  Convert orientation values from radians to degrees
        val azimuthDegrees = ((Math.toDegrees(orientationValues[0].toDouble()).toFloat() + 180) / 1.8).toFloat()
        val pitchDegrees = ((Math.toDegrees(orientationValues[1].toDouble()).toFloat() + 180) / 1.8).toFloat()
        val rollDegrees = ((Math.toDegrees(orientationValues[2].toDouble()).toFloat() + 180) / 1.8).toFloat()

        createListForGraph(azimuthDegrees, pitchDegrees, rollDegrees)

        _orientation.value = listOf(azimuthDegrees, pitchDegrees, rollDegrees)
    }

    private fun createListForGraph(azimuthDegrees: Float, pitchDegrees: Float, rollDegrees: Float) {
        _dataHashMap.value["x-axis"] = _dataHashMap.value.getOrDefault("x-axis", mutableListOf()).also {
            it.add(azimuthDegrees)
            if (it.size == 1000) {
                _dataHashMap.value.clear()
            }
        }
        _dataHashMap.value["y-axis"] = _dataHashMap.value.getOrDefault("y-axis", mutableListOf()).also {
            it.add(pitchDegrees)
        }
        _dataHashMap.value["z-axis"] = _dataHashMap.value.getOrDefault("z-axis", mutableListOf()).also {
            it.add(rollDegrees)
        }
    }


}