package com.cisco.voicerecorder.service

import android.media.MediaRecorder
import com.cisco.voicerecorder.utils.RecordedFiles

class VoiceRecorderService(
    mediaOutput: String,
    mediaRecorder: MediaRecorder? = null,
    recordedFiles: Int = 0
) {

    private var fileCounting: Int = recordedFiles
    private var mediaRecorder: MediaRecorder? = mediaRecorder
    private var output: String = mediaOutput

    fun getMediaRecorder() = mediaRecorder

    fun startMediaRecord() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun initializeMediaRecord() {
        try {
            val outputFileDestination =
                "${output}/record-${RecordedFiles.generateFileCounting()}-voice.mp3"
            fileCounting++
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(outputFileDestination)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopMediaRecord() {
        try {
            mediaRecorder?.stop()
            mediaRecorder?.reset()
            mediaRecorder?.release()
            mediaRecorder = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}