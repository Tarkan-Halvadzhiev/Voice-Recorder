package com.cisco.voicerecorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cisco.voicerecorder.utils.RecordFiles
import java.io.IOException

class VoiceRecorder : AppCompatActivity() {

    private val recordFiles : RecordFiles = RecordFiles()
    private var fileCounting: Int = recordFiles.getAllRecords()?.size ?: 0
    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private val recordAudio = Manifest.permission.RECORD_AUDIO
    private val writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private val readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        checkPermissions()
        pressedButton()
        redirectToRecordLibrary()
    }

    private fun redirectToRecordLibrary() {
        val button: Button = findViewById(R.id.record_library_view)
        button.setOnClickListener {
            val intent = Intent(this, RecordLibrary::class.java)
            startActivity(intent)
        }
    }

    private fun pressedButton() {
        val button: Button = findViewById(R.id.button_record)
        val chronometer: Chronometer = findViewById(R.id.chronometer)

        button.setOnClickListener {
            when (button.text) {
                "Record" -> startRecording(button, chronometer)
                "Stop Recording" -> stopRecording(button, chronometer)
            }
        }
    }

    private fun startRecording(button: Button, chronometer: Chronometer) {
        initializeMediaRecord()
        button.text = getString(R.string.button_name_stop_recording)
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        startMediaRecord()
    }

    private fun startMediaRecord() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, recordAudio)
            != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, writeExternalStorage)
            != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                recordAudio,
                writeExternalStorage,
                readExternalStorage
            )
            ActivityCompat.requestPermissions(this, permissions, 0)
        }
    }

    private fun initializeMediaRecord() {
        output =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).path + "/record-${fileCounting + 1}.mp3"
        fileCounting++
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)
    }

    private fun stopRecording(button: Button, chronometer: Chronometer) {
        button.text = getString(R.string.button_name)
        chronometer.stop()
        chronometer.base = SystemClock.elapsedRealtime()

        stopMediaRecord()
    }

    private fun stopMediaRecord() {
        mediaRecorder?.stop()
        mediaRecorder?.reset()
        mediaRecorder?.release()
        mediaRecorder = null
    }
}