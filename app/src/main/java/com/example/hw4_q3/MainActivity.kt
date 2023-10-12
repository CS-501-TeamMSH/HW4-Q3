package com.example.hw4_q3

import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Switch
import com.example.hw4_q3.R

class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null
    private lateinit var flashlightSwitch: Switch
    private lateinit var actionEditText: EditText
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
}