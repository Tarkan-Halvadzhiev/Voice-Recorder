package com.cisco.voicerecorder.service

import android.media.MediaRecorder
import android.util.Log
import com.cisco.voicerecorder.utils.RecordedFiles

class VoiceRecorderService(
    mediaOutput: String,
    mediaRecorder: MediaRecorder? = null,
    recordedFiles: Int = 0
) {

    private var fileCounting: Int = recordedFiles
    private var mediaRecorder: MediaRecorder? = mediaRecorder
    private var output: String = mediaOutput
    private val classNameTag: String = "VoiceRecorderService"
    private val logMessage: String = "Record was successfully"

    fun getMediaRecorder() = mediaRecorder

    fun startMediaRecord() {
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            Log.i(classNameTag, "$logMessage started.")
        } catch (e: Exception) {
            Log.e(classNameTag, e.message)
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
            Log.i(classNameTag, "$logMessage initialized.")
        } catch (e: Exception) {
            Log.e(classNameTag, e.message)
            e.printStackTrace()
        }
    }

    fun stopMediaRecord() {
        try {
            mediaRecorder?.stop()
            mediaRecorder?.reset()
            mediaRecorder?.release()
            mediaRecorder = null
            Log.i(classNameTag, "$logMessage stopped.")
        } catch (e: Exception) {
            Log.e(classNameTag, e.message)
            e.printStackTrace()
        }
    }
}