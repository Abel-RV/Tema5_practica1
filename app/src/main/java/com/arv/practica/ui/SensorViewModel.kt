package com.arv.practica.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SensorViewModel : ViewModel(){
    private val _aceleracionX = MutableStateFlow(0f)

    val aceleracionX: StateFlow<Float> = _aceleracionX

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private val sensorEventListener = object : SensorEventListener{
        override fun onSensorChanged(event: SensorEvent?) {
            if(event?.sensor?.type== Sensor.TYPE_ACCELEROMETER){
                _aceleracionX.value=event.values[0]
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }

    }

    fun iniciarSensor(context: Context){
        sensorManager= context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        accelerometer?.let { sensor ->
            sensorManager?.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }
}