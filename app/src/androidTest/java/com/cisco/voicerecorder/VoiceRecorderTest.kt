package com.cisco.voicerecorder

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VoiceRecorderTest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<VoiceRecorder> = ActivityTestRule(VoiceRecorder::class.java)

    @Test
    fun userPressedButtonForStartRecording_Successfully() {
        pressingButtonState()
    }

    @Test
    fun userPressedButtonTwice_Successfully() {
        pressingButtonState()

        onView(withId(R.id.button_record)).perform(click()).check(matches(withText("Recording")))
    }

    @Test
    fun userPressedButtonForStartingNewActivity_Successfully() {
        Intents.init()
        activityRule.launchActivity(Intent())

        onView(withId(R.id.record_library_view)).perform(click())
        intended(hasComponent(RecordLibrary::class.java.name))
        onView(withId(R.id.audio_record)).check(matches(isDisplayed()))

        Intents.release()
    }

    private fun pressingButtonState() {
        onView(withId(R.id.button_record)).check(matches(withText("Recording")))
        onView(withId(R.id.button_record)).perform(click())
            .check(matches(withText("Stop Recording")))
    }

}