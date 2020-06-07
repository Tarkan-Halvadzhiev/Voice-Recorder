package com.cisco.voicerecorder.service

import android.media.AudioAttributes
import android.media.MediaPlayer
import java.io.File

class RecordLibraryService(
    private var mediaSource: String,
    private var mediaPlayer: MediaPlayer? = null,
    private var attributes: AudioAttributes? = null
) {

    fun getMediaPlayer() = mediaPlayer

    fun pauseMediaPlayer() {
        try {
            mediaPlayer?.pause()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun resumeMediaPlayer(position: Int) {
        try {
            mediaPlayer?.start()
            mediaPlayer?.seekTo(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteMediaFile(
        fileName: String?
    ) {
        try {
            val path: String = "$mediaSource/$fileName"
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
            mediaPlayer?.setAudioAttributes(attributes)
            mediaPlayer?.setDataSource("$mediaSource/$fileName")
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer?.stop()
                releaseMediaPlayerResources()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun releaseMediaPlayerResources() {
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}