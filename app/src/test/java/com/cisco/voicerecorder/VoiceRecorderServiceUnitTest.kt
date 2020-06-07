package com.cisco.voicerecorder

import android.media.MediaRecorder
import com.cisco.voicerecorder.service.VoiceRecorderService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class VoiceRecorderServiceUnitTest {

    private val path: String = "/some/path"
    lateinit var mediaRecorder: MediaRecorder
    lateinit var voiceRecorderService: VoiceRecorderService

    @Before
    fun before() {
        mediaRecorder = mock()
    }

    @Test
    fun startMediaRecord_StartRecording_Successfully() {
        //before start recording
        voiceRecorderService = VoiceRecorderService(path)
        assertEquals(true, voiceRecorderService.getMediaRecorder() == null)


        voiceRecorderService = VoiceRecorderService(path, mediaRecorder)
        voiceRecorderService.startMediaRecord()

        //after start recording
        assertEquals(true, voiceRecorderService.getMediaRecorder() != null)
    }


    @Test
    fun startMediaRecord_StartRecording_ThrowException() {
        voiceRecorderService = VoiceRecorderService(path, mediaRecorder)
        whenever(
            voiceRecorderService.getMediaRecorder()?.start()
        ).thenThrow(IllegalStateException())

        //exception was handle by try-catch block
        voiceRecorderService.startMediaRecord()
    }

    @Test
    fun stopMediaRecord_StopRecording_Successfully() {
        voiceRecorderService = VoiceRecorderService(path, mediaRecorder)
        voiceRecorderService.startMediaRecord()
        assertEquals(true, voiceRecorderService.getMediaRecorder() != null)

        voiceRecorderService.stopMediaRecord()
        assertEquals(true, voiceRecorderService.getMediaRecorder() == null)
    }

    @Test
    fun stopMediaRecord_stopRecording_ThrowException() {
        voiceRecorderService = VoiceRecorderService(path, mediaRecorder)
        whenever(
            voiceRecorderService.getMediaRecorder()?.stop()
        ).thenThrow(IllegalStateException())

        //exception was handle by try-catch block
        voiceRecorderService.stopMediaRecord()
    }
}