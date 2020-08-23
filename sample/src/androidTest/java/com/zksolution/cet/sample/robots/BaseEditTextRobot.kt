package com.zksolution.cet.sample.robots

import com.zksolution.cet.sample.utils.*

class BaseEditTextRobot(private val editTextId: Int) {

    fun type(text: String) {
        with(editTextId) {
            scrollToId()
            clickById()
            typeText(text)
        }
    }

    fun checkText(text: String) {
        editTextId.containsText(text)
    }

    fun pressDeleteKey(times: Int = 1) {
        for (i in 1..times) editTextId.pressBackspace()
    }
}
