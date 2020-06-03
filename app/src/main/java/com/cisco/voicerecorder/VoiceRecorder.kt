package com.cisco.voicerecorder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cisco.voicerecorder.utils.ExternalStorageDestination
import com.cisco.voicerecorder.utils.RecordedFiles
import java.io.IOException

class VoiceRecorder : AppCompatActivity() {

    private var fileCounting: Int = RecordedFiles.getAllRecords()?.size ?: 0
    private var output: String = ExternalStorageDestination.getPath()
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

    //    private fun permission

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

        // this try block catch all exception that can occur in this activity(MediaRecorder method exceptions)
        try {
            button.setOnClickListener {
                when (button.text) {
                    "Recording" -> startRecording(button, chronometer)
                    "Stop Recording" -> stopRecording(button, chronometer)
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
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
        mediaRecorder?.prepare()
        mediaRecorder?.start()
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

        val outputFileDestination = "$output/record-${fileCounting + 1}-voice.mp3"
        fileCounting++
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(outputFileDestination)
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