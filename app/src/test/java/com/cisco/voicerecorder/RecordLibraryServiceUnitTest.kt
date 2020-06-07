package com.cisco.voicerecorder

import android.media.MediaPlayer
import com.cisco.voicerecorder.service.RecordLibraryService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecordLibraryServiceUnitTest {

    private val path: String = "/some/path"
    lateinit var mediaPlayer: MediaPlayer
    lateinit var recordLibraryService: RecordLibraryService

    @Before
    fun before() {
        mediaPlayer = mock()
    }

    @Test
    fun getMediaPlayer_returnNullValueWhenAudioIsNotStarted_Successfully() {
        recordLibraryService = RecordLibraryService(path)
        assertEquals(null, recordLibraryService.getMediaPlayer())
    }

    @Test
    fun pauseMediaPlayer_PauseMediaPlayer_Successfully() {
        recordLibraryService = RecordLibraryService(path, mediaPlayer)

        recordLibraryService.pauseMediaPlayer()

        verify(recordLibraryService.getMediaPlayer(), times(1))?.pause()
    }

    @Test
    fun pauseMediaPlayer_PauseMediaPlayer_ThrowRuntimeException() {
        recordLibraryService = RecordLibraryService(path, mediaPlayer)
        whenever(
            recordLibraryService.getMediaPlayer()?.start()
        ).thenThrow(IllegalStateException())

        //exception was handle by try-catch block
        recordLibraryService.pauseMediaPlayer()
    }

    @Test
    fun resumeMediaPlayer_resumeMediaPlayerFromGivenPosition_Successfully() {
        recordLibraryService = RecordLibraryService(path, mediaPlayer)

        recordLibraryService.resumeMediaPlayer(0)

        verify(recordLibraryService.getMediaPlayer(), times(1))?.start()
        verify(recordLibraryService.getMediaPlayer(), times(1))?.seekTo(0)
    }

    @Test
    fun resumeMediaPlayer_resumeMediaPlayerFromGivenPosition_ThrowRuntimeException() {
        recordLibraryService = RecordLibraryService(path, mediaPlayer)
        whenever(
            recordLibraryService.getMediaPlayer()?.start()
        ).thenThrow(IllegalStateException())

        //exception was handle by try-catch block
        recordLibraryService.resumeMediaPlayer(0)
    }

    @Test
    fun startAudio_startsAudioFile_ThrowRuntimeException() {
        recordLibraryService = RecordLibraryService(path, mediaPlayer)
        recordLibraryService.stopMediaPlayer()
        whenever(
            recordLibraryService.getMediaPlayer()?.start()
        ).thenThrow(IllegalStateException())

        //exception was handle by try-catch block
        recordLibraryService.startAudio(path)
    }


    @Test
    fun startAudio_startsAudioFile_Successfully() {
        recordLibraryService = RecordLibraryService("", mediaPlayer)
        recordLibraryService.startAudio(path)
        val playedMediaPlayer: MediaPlayer? = recordLibraryService.getMediaPlayer()

        assertEquals(true, playedMediaPlayer != null)
    }

    @Test
    fun stopMediaPlayer_whenAudioIsNotStarted_Successfully() {
        recordLibraryService = RecordLibraryService(path)
        assertEquals(null, recordLibraryService.getMediaPlayer())

        recordLibraryService.stopMediaPlayer()

        assertEquals(null, recordLibraryService.getMediaPlayer())
    }

    @Test
    fun stopMediaPlayer_whenAudioIsStarted_Successfully() {
        recordLibraryService = RecordLibraryService(path)
        recordLibraryService.startAudio(path)

        recordLibraryService.stopMediaPlayer()

        assertEquals(null, recordLibraryService.getMediaPlayer())
    }

    @Test
    fun stopMediaPlayer_fail_ThrowRuntimeException() {
        recordLibraryService = RecordLibraryService(path, mediaPlayer)
        whenever(
            recordLibraryService.getMediaPlayer()?.prepare()
        ).thenThrow(IllegalStateException())

        //exception was handle by try-catch block
        recordLibraryService.stopMediaPlayer()
    }

    @Test
    fun releaseMediaPlayerResources_releaseResources_Successfully() {
        recordLibraryService = RecordLibraryService(path)
        recordLibraryService.startAudio(path)
        val mediaPlayerWhilePlaying: MediaPlayer? = recordLibraryService.getMediaPlayer()

        assertEquals(true, mediaPlayerWhilePlaying != null)

        recordLibraryService.releaseMediaPlayerResources()

        val mediaPlayerAfterReleaseResources: MediaPlayer? = recordLibraryService.getMediaPlayer()

        assertEquals(null, mediaPlayerAfterReleaseResources)
    }

}
