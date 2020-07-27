package com.zksolution.cet.sample

class BaseCurrencyRobot(private val currencyEditTextId: Int) {

    fun type(text: String) {
        currencyEditTextId.typeText(text)
    }

    fun checkText(text: String) {
        currencyEditTextId.containsText(text)
    }

    fun pressDeleteKey(times: Int = 1) {
        for (i in 1..times) currencyEditTextId.pressBackspace()
    }
}
