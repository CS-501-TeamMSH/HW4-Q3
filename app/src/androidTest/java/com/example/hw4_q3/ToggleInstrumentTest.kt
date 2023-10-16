package com.example.hw4_q3

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry


import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ToggleInstrumentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.hw4_q3", appContext.packageName)
    }

    // Test 1: Check if flashlight toggle works
    @Test
    fun testSwitchToggle() {
        val toggleButton = Espresso.onView(ViewMatchers.withId(R.id.flashlightSwitch))

        toggleButton.check(ViewAssertions.matches(ViewMatchers.isNotChecked()))
        toggleButton.perform(ViewActions.click())
        toggleButton.check(ViewAssertions.matches(ViewMatchers.isChecked()))
    }

    // Test 2: Check if the edit text toggles the flashlight button
    @Test
    fun testEditText() {
        val testText = "ON"
        val editText = Espresso.onView(ViewMatchers.withId(R.id.editText))
        val toggleButton = Espresso.onView(ViewMatchers.withId(R.id.flashlightSwitch))

        editText.perform(ViewActions.typeText(testText))
        editText.check(ViewAssertions.matches(ViewMatchers.withText(testText)))
        toggleButton.check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }



}