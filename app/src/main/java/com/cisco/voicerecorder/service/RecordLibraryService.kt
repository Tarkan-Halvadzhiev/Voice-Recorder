package com.cisco.voicerecorder.service

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.cisco.voicerecorder.utils.ExternalStorageDestination
import java.io.File
import java.io.IOException

class RecordLibraryService {

    private var mediaPlayer: MediaPlayer? = null
    private var mediaSource: String = ExternalStorageDestination.getPath()

    fun getMediaPlayer() = mediaPlayer

    fun deleteMediaFile(
        fileName: String?
    ) {
        try {
            val path: String = ExternalStorageDestination.getPath() + "/$fileName"
            val myFile = File(path)
            myFile.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startAudio(
        fileName: String?
    ) {
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )

            mediaPlayer?.setDataSource("$mediaSource/$fileName")
            mediaPlayer?.prepare()
            mediaPlayer?.start()

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: RuntimeException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer?.stop()
                releaseMediaPlayerRecourse()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun releaseMediaPlayerRecourse() {
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}