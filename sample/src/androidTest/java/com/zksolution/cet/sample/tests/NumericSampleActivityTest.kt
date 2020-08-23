package com.zksolution.cet.sample.tests

import androidx.test.rule.ActivityTestRule
import com.zksolution.cet.sample.R
import com.zksolution.cet.sample.SampleActivity
import com.zksolution.cet.sample.robots.BaseEditTextRobot
import com.zksolution.cet.sample.utils.LocaleHelper
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.DisplayName

class NumericSampleActivityTest {

    @get:Rule
    val rule = ActivityTestRule(SampleActivity::class.java)

    private val localeHelper = LocaleHelper(
        defaultGroupingSeparator = ",",
        defaultDecimalSeparator = "."
    )
    private val noDecimalNumericRobot =
        BaseEditTextRobot(R.id.noDecimalNumericEditText)
    private val decimalNumericRobot =
        BaseEditTextRobot(R.id.decimalNumericEditText)
    private val insideTextInputLayoutNumericRobot =
        BaseEditTextRobot(R.id.insideTextInputLayoutNumericEditText)

    @Test
    @DisplayName("Given valid input within decimalNumericText then assert expected value")
    fun testNoDecimalNumericValidInput() {
        noDecimalNumericRobot.apply {
            type("123456789")
            checkText(localeHelper.applyToNumeric("123,456,789"))
        }
    }

    @Test
    @DisplayName("Given decimals within decimalNumericText then ignore decimal separator")
    fun testNoDecimalNumericInvalidInput() {
        noDecimalNumericRobot.apply {
            type("123456789.45")
            checkText(localeHelper.applyToNumeric("12,345,678,945"))
        }
    }

    @Test
    @DisplayName("When backspace is clicked within noDecimalNumericText then assert expected value")
    fun testNoDecimalNumericBackspaceBehaviour() {
        noDecimalNumericRobot.apply {
            type("123456789")
            pressDeleteKey(4)
            type("00")
            checkText(localeHelper.applyToNumeric("1,234,500"))
        }
    }

    @Test
    @DisplayName("Given valid input within decimalNumericText then assert expected value")
    fun testDecimalNumericValidInput() {
        decimalNumericRobot.apply {
            type("123456789.45")
            checkText(localeHelper.applyToNumeric("123,456,789.45"))
        }
    }

    @Test
    @DisplayName("Given more than 2 decimals within decimalNumericText then ignore them")
    fun testDecimalNumericInvalidInput() {
        decimalNumericRobot.apply {
            type("123456789.4533")
            checkText(localeHelper.applyToNumeric("123,456,789.45"))
        }
    }

    @Test
    @DisplayName("When backspace is clicked within decimalNumericText then assert expected value")
    fun testDecimalNumericBackspaceBehaviour() {
        decimalNumericRobot.apply {
            type("123456789.4533")
            pressDeleteKey(5)
            type(".00")
            checkText(localeHelper.applyToNumeric("1,234,567.00"))
        }
    }

    @Test
    @DisplayName("Given valid input within insideTextInputLayoutNumeric then assert expected value")
    fun testInsideTextInputLayoutNumericValidInput() {
        insideTextInputLayoutNumericRobot.apply {
            type("20000.45")
            checkText(localeHelper.applyToNumeric("20,000.45"))
        }
    }

    @Test
    @DisplayName("Given multiple zero values then render only the first one")
    fun testInsideTextInputLayoutNumericInvalidInput() {
        insideTextInputLayoutNumericRobot.apply {
            type("000000000000")
            checkText(localeHelper.applyToNumeric("0"))
        }
    }

    @Test
    @DisplayName("Given multiple decimal separators within insideTextInputLayoutNumericText then only render the first one")
    fun testInsideTextInputLayoutNumericMultipleDecimalSeparatorsInput() {
        insideTextInputLayoutNumericRobot.apply {
            type("2.5.0")
            checkText(localeHelper.applyToNumeric("2.50"))
        }
    }

    @Test
    @DisplayName("Given groping separators when typing within insideTextInputLayoutNumericText then ignore them")
    fun testInsideTextInputLayoutNumericIgnoringGroupingSeparatorsInput() {
        insideTextInputLayoutNumericRobot.apply {
            type("4,5,0,0,0,0,0")
            checkText(localeHelper.applyToNumeric("4,500,000"))
        }
    }

    @Test
    @DisplayName("When backspace is clicked within insideTextInputLayoutNumericText then assert expected value")
    fun testInsideTextInputLayoutNumericBackspaceBehaviour() {
        insideTextInputLayoutNumericRobot.apply {
            type("00000.4533")
            pressDeleteKey(3)
            type("00")
            checkText(localeHelper.applyToNumeric("0"))
        }
    }
}
