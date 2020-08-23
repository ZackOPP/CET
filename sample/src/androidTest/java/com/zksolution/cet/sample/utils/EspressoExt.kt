package com.zksolution.cet.sample.utils

import android.view.KeyEvent
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.CoreMatchers.*


fun Int.clickById() = onView(withId(this)).perform(click())

fun Int.typeText(text: String) {
    onView(withId(this)).perform(ViewActions.typeText(text))
    closeSoftKeyboard()
}

fun Int.pressBackspace() {
    onView(withId(this)).perform(pressKey(KeyEvent.KEYCODE_DEL))
}

fun Int.containsText(text: String): ViewInteraction = onView(withId(this)).check(
    ViewAssertions.matches(
        ViewMatchers.withText(text)
    )
)

fun Int.clickItemInSpinner(text: String) {
    onView(withId(this)).perform(click())
    onData(allOf(`is`(instanceOf(String::class.java)), `is`(text))).perform(click())
}

fun Int.scrollToId() {
    onView(withId(this)).perform(ViewActions.scrollTo())
}
