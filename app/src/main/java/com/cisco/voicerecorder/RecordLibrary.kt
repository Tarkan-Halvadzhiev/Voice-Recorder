package com.cisco.voicerecorder

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.cisco.voicerecorder.custom.adapter.CustomRecordAdapter
import com.cisco.voicerecorder.utils.ExternalStorageDestination
import com.cisco.voicerecorder.utils.RecordedFiles
import java.io.File
import java.io.IOException


class RecordLibrary : AppCompatActivity() {

    private val recordFiles: List<File>? = RecordedFiles.getAllRecords()
    private var mediaPlayer: MediaPlayer? = null
    private var lastImageViewStartButton: ImageView? = null
    private var lastImageViewStopButton: ImageView? = null
    private var mediaSource: String = ExternalStorageDestination.getPath()
    private var playTime: Int = 0
    private var endTime: Int = 0
    private var seekBar: SeekBar? = null
    private var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.library_record_activity)
        val listView: ListView = findViewById(R.id.audio_record)

        listView.adapter = CustomRecordAdapter(this, recordFiles)
    }

    fun deleteMediaFileEventListener(
        picture: ImageView,
        fileName: String?
    ) {
        try {
            picture.setOnLongClickListener {
                val path: String = ExternalStorageDestination.getPath() + "/$fileName"
                val myFile = File(path)

                myFile.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startAudio(
        lastImageViewActionStart: ImageView,
        lastImageViewActionStop: ImageView,
        fileName: String?,
        seekBar: SeekBar
    ) {
        try {
            setViabilityForPressedButtons(lastImageViewActionStart, lastImageViewActionStop)

            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )

            mediaPlayer?.setDataSource("$mediaSource/$fileName")
            mediaPlayer?.prepare()
            mediaPlayer?.start()

            seekBarInitialization(seekBar)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }

        finishAudioFile()
    }

    private fun seekBarInitialization(currentSeekBar: SeekBar) {
        seekBar = currentSeekBar
        seekBar?.visibility = View.VISIBLE
        endTime = mediaPlayer?.duration!!
        playTime = mediaPlayer?.currentPosition!!
        seekBar?.max = endTime
        seekBar?.progress = playTime
        handler.postDelayed(updateSeekBarPosition, 100)
    }

    private fun finishAudioFile() {
        mediaPlayer?.setOnCompletionListener {
            lastImageViewStartButton?.visibility = View.INVISIBLE
            lastImageViewStopButton?.visibility = View.VISIBLE
            seekBar?.visibility = View.INVISIBLE
            releaseMediaPlayerRecourse()
        }
    }

    fun stopAudio() {
        if (lastImageViewStartButton != null && lastImageViewStopButton != null) {
            setLastListenedRecordButtons(null, null)
        }

        try {
            stopMediaPlayer()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
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
            seekBar?.visibility = View.INVISIBLE
            setLastListenedRecordButtons(lastImageViewActionStart, lastImageViewActionStop)
            stopMediaPlayer()
        }
    }

    private val updateSeekBarPosition = object : Runnable {
        override fun run() {
            if (mediaPlayer != null) {
                playTime = mediaPlayer?.currentPosition!!
                seekBar?.progress = playTime
                handler.postDelayed(this, 100)
            } else {
                seekBar?.progress = 0
                seekBar?.visibility = View.INVISIBLE
            }
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