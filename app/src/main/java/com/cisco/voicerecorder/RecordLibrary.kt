package com.cisco.voicerecorder

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.cisco.voicerecorder.custom.adapter.CustomRecordAdapter
import com.cisco.voicerecorder.utils.ExternalStorageDestination
import java.io.File
import java.io.IOException
import com.cisco.voicerecorder.utils.RecordedFiles


class RecordLibrary : AppCompatActivity() {

    private val recordFiles: List<File>? = RecordedFiles.getAllRecords()
    private var mediaPlayer: MediaPlayer? = null
    private var lastImageViewStartButton: ImageView? = null
    private var lastImageViewStopButton: ImageView? = null
    private var mediaSource: String = ExternalStorageDestination.getPath()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.library_record_activity)
        val listView: ListView = findViewById(R.id.audio_record)

        listView.adapter = CustomRecordAdapter(this, this.recordFiles)
    }

    fun startAudio(
        lastImageViewActionStart: ImageView,
        lastImageViewActionStop: ImageView,
        fileName: String?
    ) {
        setViabilityForPressedButtons(lastImageViewActionStart, lastImageViewActionStop)

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        try {
            mediaPlayer?.setDataSource("$mediaSource/$fileName")
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mediaPlayer?.setOnCompletionListener {
            lastImageViewStartButton?.visibility = View.INVISIBLE
            lastImageViewStopButton?.visibility = View.VISIBLE
            releaseMediaPlayerRecourse()
        }
    }

    fun stopAudio() {
        if (lastImageViewStartButton != null && lastImageViewStopButton != null) {
            setLastListenedRecordButtons(null, null)
        }

        stopMediaPlayer()
    }

    private fun stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            releaseMediaPlayerRecourse()
        }
    }

    private fun releaseMediaPlayerRecourse() {
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun setViabilityForPressedButtons(
        lastImageViewActionStart: ImageView,
        lastImageViewActionStop: ImageView
    ) {
        if (lastImageViewStartButton == null && lastImageViewStopButton == null) {
            setLastListenedRecordButtons(lastImageViewActionStart, lastImageViewActionStop)
        } else {
            lastImageViewStartButton?.visibility = View.INVISIBLE
            lastImageViewStopButton?.visibility = View.VISIBLE
            setLastListenedRecordButtons(lastImageViewActionStart, lastImageViewActionStop)
            stopMediaPlayer()
        }
    }

    private fun setLastListenedRecordButtons(
        lastImageViewActionStart: ImageView?,
        lastImageViewActionStop: ImageView?
    ) {
        lastImageViewStartButton = lastImageViewActionStart
        lastImageViewStopButton = lastImageViewActionStop
    }
}