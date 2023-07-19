package com.example.readingsensorinfo

/**
//   ReadingSensorInfo
//   Created by Subham Dhakal on 7/18/23.
//   Copyright © 2023. All rights reserved.
 */
abstract class MeasureableSensor(
    protected val sensorType: Int
) {
    abstract val doesSensorExist: Boolean
    protected var onSensorValueChanged: ((List<Float>) -> Unit)? = null
    abstract fun startListening()
    abstract fun stopListening()

    fun setOnSensorValuesChangedListener(listener: (List<Float>) -> Unit) {
        onSensorValueChanged = listener
    }


}