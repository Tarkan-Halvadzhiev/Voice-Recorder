package com.cisco.voicerecorder.service

import android.media.MediaRecorder
import com.cisco.voicerecorder.utils.ExternalStorageDestination
import com.cisco.voicerecorder.utils.RecordedFiles
import java.io.IOException

class VoiceRecorderService {

    private var fileCounting: Int = RecordedFiles.getAllRecords()?.size ?: 0
    private var mediaRecorder: MediaRecorder? = null
    private var output: String = ExternalStorageDestination.getPath()

    fun startMediaRecord() {
        mediaRecorder?.prepare()
        mediaRecorder?.start()
    }

    fun initializeMediaRecord() {
        try {
            val outputFileDestination =
                "$output/record-${RecordedFiles.generateFileCounting()}-voice.mp3"
            fileCounting++
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(outputFileDestination)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun stopMediaRecord() {
        try {
            mediaRecorder?.stop()
            mediaRecorder?.reset()
            mediaRecorder?.release()
            mediaRecorder = null
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}