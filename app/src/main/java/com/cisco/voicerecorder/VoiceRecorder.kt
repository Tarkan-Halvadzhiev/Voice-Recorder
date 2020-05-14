package com.cisco.voicerecorder

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity

class VoiceRecorder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        pressedButton()
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
        button.text = getString(R.string.button_name_stop_recording)
        chronometer.start()
    }

    private fun stopRecording(button: Button, chronometer: Chronometer) {
        button.text = getString(R.string.button_name)
        chronometer.stop()
        chronometer.base = SystemClock.elapsedRealtime()
    }
}