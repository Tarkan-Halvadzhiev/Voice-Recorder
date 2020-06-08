package com.cisco.voicerecorder

import android.media.AudioAttributes
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.cisco.voicerecorder.custom.adapter.CustomRecordAdapter
import com.cisco.voicerecorder.service.RecordLibraryService
import com.cisco.voicerecorder.utils.DialogMessageBuilder
import com.cisco.voicerecorder.utils.ExternalStorageDestination
import com.cisco.voicerecorder.utils.RecordedFiles
import java.io.File


class RecordLibrary : AppCompatActivity() {

    private var recordFiles: MutableList<File>? = RecordedFiles.getAllRecords()
    private var lastImageViewStartButton: ImageView? = null
    private var lastImageViewStopButton: ImageView? = null
    private val recordLibraryService: RecordLibraryService = RecordLibraryService(
        ExternalStorageDestination.getPath(), null, AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
    )
    private var playTime: Int = 0
    private var endTime: Int = 0
    private var seekBar: SeekBar? = null
    private var handler: Handler = Handler()
    private var listView: ListView? = null
    private var adapter: CustomRecordAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.library_record_activity)
        DialogMessageBuilder.messageBuilding(this, R.string.info_message, R.string.info_message_title)

        listView = findViewById(R.id.audio_record)
        adapter = CustomRecordAdapter(this, recordFiles)
        listView?.adapter = adapter
    }

    fun deleteMediaFileEventListener(
        fileName: String?
    ) {
        recordLibraryService.deleteMediaFile(fileName)
    }

    fun startAudio(
        lastImageViewActionStart: ImageView,
        lastImageViewActionStop: ImageView,
        fileName: String?,
        seekBar: SeekBar
    ) {
        setViabilityForPressedButtons(lastImageViewActionStart, lastImageViewActionStop)

        recordLibraryService.startAudio(fileName)

        seekBarInitialization(seekBar)
        finishAudioFile()
    }

    fun changePlayTimePosition(position: Int) {
        val mediaPlayer = recordLibraryService.getMediaPlayer()
        mediaPlayer?.seekTo(position)
    }

    fun pauseMediaPlayer() {
        recordLibraryService.pauseMediaPlayer()
    }

    fun resumeMediaPlayer(position: Int) {
        recordLibraryService.resumeMediaPlayer(position)
    }

    private fun seekBarInitialization(currentSeekBar: SeekBar) {
        val mediaPlayer = recordLibraryService.getMediaPlayer()
        seekBar = currentSeekBar
        seekBar?.visibility = View.VISIBLE
        endTime = mediaPlayer?.duration!!
        playTime = mediaPlayer.currentPosition
        seekBar?.max = endTime
        seekBar?.progress = playTime
        handler.postDelayed(updateSeekBarPosition, 100)
    }

    private fun finishAudioFile() {
        val mediaPlayer = recordLibraryService.getMediaPlayer()
        mediaPlayer?.setOnCompletionListener {
            lastImageViewStartButton?.visibility = View.INVISIBLE
            lastImageViewStopButton?.visibility = View.VISIBLE
            seekBar?.visibility = View.INVISIBLE
            recordLibraryService.releaseMediaPlayerResources()
        }
    }

    fun stopAudio() {
        if (lastImageViewStartButton != null && lastImageViewStopButton != null) {
            setLastListenedRecordButtons(null, null)
        }

        recordLibraryService.stopMediaPlayer()
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
            recordLibraryService.stopMediaPlayer()
        }
    }

    private val updateSeekBarPosition = object : Runnable {
        override fun run() {
            val mediaPlayer = recordLibraryService.getMediaPlayer()
            if (mediaPlayer != null) {
                playTime = mediaPlayer.currentPosition
                seekBar?.progress = playTime
                handler.postDelayed(this, 1000)
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