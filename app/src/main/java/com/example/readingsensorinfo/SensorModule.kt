package com.example.readingsensorinfo

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
//   ReadingSensorInfo
//   Created by Subham Dhakal on 7/18/23.
//   Copyright © 2023. All rights reserved.
 */
@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Provides
    @Singleton
    @SensorOne
    fun provideAccelerometerSensor(app: Application): MeasureableSensor {
        return AccelerometerSensor(app)
    }

    @Provides
    @Singleton
    @SensorTwo
    fun provideMagnetometerSensor(app: Application): MeasureableSensor {
        return MagnetometerSensor(app)
    }
}