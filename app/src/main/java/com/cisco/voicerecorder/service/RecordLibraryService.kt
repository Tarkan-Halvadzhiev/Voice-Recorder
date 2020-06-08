package com.cisco.voicerecorder.service

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import java.io.File

class RecordLibraryService(
    private var mediaSource: String,
    private var mediaPlayer: MediaPlayer? = null,
    private var attributes: AudioAttributes? = null
) {

    private val classNameTag: String = "RecordLibraryService"
    private val logMessage: String = "Media file was successfully"

    fun getMediaPlayer() = mediaPlayer

    fun pauseMediaPlayer() {
        try {
            mediaPlayer?.pause()
        } catch (e: Exception) {
            Log.e(classNameTag, e.message)
            e.printStackTrace()
        }
    }

    fun resumeMediaPlayer(position: Int) {
        try {
            mediaPlayer?.start()
            mediaPlayer?.seekTo(position)
        } catch (e: Exception) {
            Log.e(classNameTag, e.message)
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
            Log.i(classNameTag,"$logMessage deleted.")
        } catch (e: Exception) {
            Log.e(classNameTag, e.message)
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
            Log.i(classNameTag,"$logMessage started.")
        } catch (e: Exception) {
            Log.e(classNameTag, e.message)
            e.printStackTrace()
        }
    }

    fun stopMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer?.stop()
                releaseMediaPlayerResources()
            }
            Log.i(classNameTag,"$logMessage stopped.")
        } catch (e: Exception) {
            Log.e(classNameTag, e.message)
            e.printStackTrace()
        }
    }

    fun releaseMediaPlayerResources() {
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}