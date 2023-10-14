package com.example.hw4_q3

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Switch
import com.example.hw4_q3.R

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null
    private lateinit var flashlightSwitch: Switch
    private lateinit var actionEditText: EditText
    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        flashlightSwitch = findViewById(R.id.flashlightSwitch)
        actionEditText = findViewById(R.id.editText)
        actionEditText.setText("")

        try {
            val cameraIds = cameraManager.cameraIdList
            for (id in cameraIds) {
                val characteristics = cameraManager.getCameraCharacteristics(id)
                val flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true

                if (flashAvailable) {
                    cameraId = id
                    break
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        setUpSensors()

        flashlightSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                turnOnFlashlight()
            } else {
                turnOffFlashlight()
            }
        }

        actionEditText.setOnEditorActionListener { _, _, _ ->
            val action = actionEditText.text.toString().trim()
            if (action.equals("ON", ignoreCase = true)) {
                turnOnFlashlight()
                flashlightSwitch.isChecked = true
            } else if (action.equals("OFF", ignoreCase = true)) {
                turnOffFlashlight()
                flashlightSwitch.isChecked = false
            }
            true
        }
    }

    private fun setUpSensors() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    private fun turnOnFlashlight() {
        try {
            if (cameraId != null) {
                cameraManager.setTorchMode(cameraId!!, true)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun turnOffFlashlight() {
        try {
            if (cameraId != null) {
                cameraManager.setTorchMode(cameraId!!, false)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val movement = event.values[1]
            val flingThreshold = 10

            if (movement > flingThreshold) {
                turnOnFlashlight()
                flashlightSwitch.isChecked = true
            } else if (movement < -flingThreshold) {
                turnOffFlashlight()
                flashlightSwitch.isChecked = false
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}