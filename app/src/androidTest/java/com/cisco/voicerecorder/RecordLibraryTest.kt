package com.cisco.voicerecorder

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.cisco.voicerecorder.utils.RecordedFiles
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecordLibraryTest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<RecordLibrary> = ActivityTestRule(RecordLibrary::class.java)

    @Test
    fun displayAllRecord_Successfully() {
        onView(withId(R.id.audio_record)).check(matches(isDisplayed()))
    }

    @Test
    fun userOpenDialogForDeletingFunctionality_Successfully() {
        openDialogForDeleting()

        onView(withText("Alert"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun userCloseDialogByClickButton_Successfully() {
        openDialogForDeleting()

        onView(withText("No"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText("Alert"))
            .check(doesNotExist())
        onView(withText("Yes"))
            .check(doesNotExist())
        onView(withText("No"))
            .check(doesNotExist())
    }

    @Test
    fun userDeleteRecord_Successfully() {
        openDialogForDeleting()
        val beforeDeletingFilesSize: Int = RecordedFiles.getAllRecords()?.size ?: 0

        onView(withText("Yes"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        val afterDeletingFilesSize: Int = RecordedFiles.getAllRecords()?.size ?: 0

        assertEquals(beforeDeletingFilesSize - 1, afterDeletingFilesSize)
    }

    @Test
    fun userRejectDeleteRecord_Successfully() {
        openDialogForDeleting()
        val beforeDeletingFilesSize: Int = RecordedFiles.getAllRecords()?.size ?: 0

        onView(withText("No"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        val afterDeletingFilesSize: Int = RecordedFiles.getAllRecords()?.size ?: 0

        assertEquals(beforeDeletingFilesSize, afterDeletingFilesSize)
    }

    @Test
    fun userPressedPlayButton_Successfully() {
        //before pressing
        beforePressingState(0)

        //after pressing
        onData(anything()).inAdapterView(withId(R.id.audio_record)).atPosition(0)
            .onChildView(withId(R.id.play_button)).perform(click())
            .check(matches(not(isDisplayed())))
    }

    @Test
    fun userPressedPlayButtonAndThenStopRecord_Successfully() {
        //before pressing
        beforePressingState(0)

        //after pressing play button
        afterPressingState(0)

        //after pressing stop button
        onData(anything()).inAdapterView(withId(R.id.audio_record)).atPosition(0)
            .onChildView(withId(R.id.stop_button)).perform(click())
            .check(matches(not(isDisplayed())))
        onData(anything()).inAdapterView(withId(R.id.audio_record)).atPosition(0)
            .onChildView(withId(R.id.stop_button)).check(matches(not(isDisplayed())))
    }

    @Test
    fun userPressedPlayButtonToOtherRecordWhenIsAlreadyPlayingRecord_Successfully() {
        //before pressing
        beforePressingState(0)

        //before pressing play button at position 0
        beforePressingState(1)

        //pressed play button at position 0
        afterPressingState(0)

        //pressed play button at position 1
        afterPressingState(1)

        //checking view state for record at position 0
        beforePressingState(0)
    }

    private fun openDialogForDeleting() {
        onData(anything()).inAdapterView(withId(R.id.audio_record)).atPosition(0)
            .onChildView(withId(R.id.image_view)).perform(click())
    }

    private fun beforePressingState(position: Int) {
        onData(anything()).inAdapterView(withId(R.id.audio_record)).atPosition(position)
            .onChildView(withId(R.id.play_button)).check(matches(isDisplayed()))

        checkMatchesStateWhenIsNotDisplayed(position)
    }

    private fun afterPressingState(position: Int) {
        onData(anything()).inAdapterView(withId(R.id.audio_record)).atPosition(position)
            .onChildView(withId(R.id.play_button)).perform(click())
            .check(matches(not(isDisplayed())))

        checkMatchesStateWhenIsDisplayed(position)
    }

    private fun checkMatchesStateWhenIsDisplayed(position: Int) {
        onData(anything()).inAdapterView(withId(R.id.audio_record)).atPosition(position)
            .onChildView(withId(R.id.stop_button)).check(matches(isDisplayed()))
        onData(anything()).inAdapterView(withId(R.id.audio_record)).atPosition(position)
            .onChildView(withId(R.id.seek_bar)).check(matches(isDisplayed()))
    }

    private fun checkMatchesStateWhenIsNotDisplayed(position: Int) {
        onData(anything()).inAdapterView(withId(R.id.audio_record)).atPosition(position)
            .onChildView(withId(R.id.stop_button)).check(matches(not(isDisplayed())))
        onData(anything()).inAdapterView(withId(R.id.audio_record)).atPosition(position)
            .onChildView(withId(R.id.seek_bar)).check(matches(not(isDisplayed())))
    }
}