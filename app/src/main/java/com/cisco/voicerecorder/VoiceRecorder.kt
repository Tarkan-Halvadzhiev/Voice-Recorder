package com.cisco.voicerecorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cisco.voicerecorder.service.VoiceRecorderService
import com.cisco.voicerecorder.utils.PermissionChecker

class VoiceRecorder : AppCompatActivity() {

    private var isRecording = false
    private val voiceRecorderService: VoiceRecorderService = VoiceRecorderService()
    private val recordAudio = Manifest.permission.RECORD_AUDIO
    private val writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        checkPermissions()
        startApplicationFunctionality()
    }

    private fun startApplicationFunctionality() {
        val button: Button = findViewById(R.id.button_record)
        button.isClickable = false
        val redirect: Button = findViewById(R.id.record_library_view)
        redirect.isClickable = false

        if (ContextCompat.checkSelfPermission(this, recordAudio)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, writeExternalStorage)
            != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, readExternalStorage)
            != PackageManager.PERMISSION_GRANTED
        ) {
            PermissionChecker.permissionMessage(this)
        } else {
            button.isClickable = true
            redirect.isClickable = true
            pressedButton()
            redirectToRecordLibrary()
        }
    }

    private fun isPermissionsGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(this, recordAudio)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, writeExternalStorage)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, readExternalStorage)
                != PackageManager.PERMISSION_GRANTED)
    }

    private fun redirectToRecordLibrary() {
        val button: Button = findViewById(R.id.record_library_view)
        button.setOnClickListener {
            if (isRecording) {
                val startButton: Button = findViewById(R.id.button_record)
                val chronometer: Chronometer = findViewById(R.id.chronometer)
                stopRecording(startButton, chronometer)
            }

            val intent = Intent(this, RecordLibrary::class.java)
            startActivity(intent)
        }
    }

    private fun pressedButton() {
        val button: Button = findViewById(R.id.button_record)
        val chronometer: Chronometer = findViewById(R.id.chronometer)

        button.setOnClickListener {
            when (button.text) {
                "Recording" -> startRecording(button, chronometer)
                "Stop Recording" -> stopRecording(button, chronometer)
            }
        }
    }

    private fun startRecording(button: Button, chronometer: Chronometer) {
        isRecording = true
        voiceRecorderService.initializeMediaRecord()
        button.text = getString(R.string.button_name_stop_recording)
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        voiceRecorderService.startMediaRecord()
    }

    private fun checkPermissions() {
        if (isPermissionsGranted()) {
            requestPermission()
        }
    }

    private fun requestPermission() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(this, permissions, 1)
    }

    private fun stopRecording(button: Button, chronometer: Chronometer) {
        isRecording = false
        button.text = getString(R.string.button_name)
        chronometer.stop()
        chronometer.base = SystemClock.elapsedRealtime()

        voiceRecorderService.stopMediaRecord()
    }
}