package com.zksolution.cet.sample

import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.DisplayName

class SampleActivityTest {

    @get:Rule
    val rule = ActivityTestRule(SampleActivity::class.java)

    private val localeHelper = LocaleHelper(defaultGroupingSeparator = ",", defaultDecimalSeparator = ".")

    private val currencySpinnerRobot = CurrencySpinnerRobot()
    private val noDecimalCurrencyRobot = BaseCurrencyRobot(R.id.noDecimalCurrencyEditText)
    private val decimalCurrencyRobot = BaseCurrencyRobot(R.id.decimalCurrencyEditText)
    private val insideTextInputLayoutCurrencyRobot = BaseCurrencyRobot(R.id.insideTextInputLayoutCurrencyEditText)

    @Test
    @DisplayName("Given valid input within decimalCurrencyText then assert expected value")
    fun testNoDecimalCurrencyValidInput() {
        noDecimalCurrencyRobot.apply {
            type("123456789")
            checkText(localeHelper.applyTo("123,456,789"))
        }
    }

    @Test
    @DisplayName("Given decimals within decimalCurrencyText then ignore decimal separator")
    fun testNoDecimalCurrencyInvalidInput() {
        noDecimalCurrencyRobot.apply {
            type("123456789.45")
            checkText(localeHelper.applyTo("12,345,678,945"))
        }
    }

    @Test
    @DisplayName("When backspace is clicked within noDecimalCurrencyText then assert expected value")
    fun testNoDecimalCurrencyBackspaceBehaviour() {
        noDecimalCurrencyRobot.apply {
            type("123456789")
            pressDeleteKey(4)
            type("00")
            checkText(localeHelper.applyTo("1,234,500"))
        }
    }

    @Test
    @DisplayName("Given valid input within decimalCurrencyText then assert expected value")
    fun testDecimalCurrencyValidInput() {
        decimalCurrencyRobot.apply {
            type("123456789.45")
            checkText(localeHelper.applyTo("123,456,789.45"))
        }
    }

    @Test
    @DisplayName("Given more than 2 decimals within decimalCurrencyText then ignore them")
    fun testDecimalCurrencyInvalidInput() {
        decimalCurrencyRobot.apply {
            type("123456789.4533")
            checkText(localeHelper.applyTo("123,456,789.45"))
        }
    }

    @Test
    @DisplayName("When backspace is clicked within decimalCurrencyText then assert expected value")
    fun testDecimalCurrencyBackspaceBehaviour() {
        decimalCurrencyRobot.apply {
            type("123456789.4533")
            pressDeleteKey(5)
            type(".00")
            checkText(localeHelper.applyTo("1,234,567.00"))
        }
    }

    @Test
    @DisplayName("Given valid input within insideTextInputLayoutCurrency then assert expected value")
    fun testInsideTextInputLayoutCurrencyValidInput() {
        insideTextInputLayoutCurrencyRobot.apply {
            type("20000.45")
            checkText(localeHelper.applyTo("20,000.45"))
        }
    }

    @Test
    @DisplayName("Given multiple zero values then render only the first one")
    fun testInsideTextInputLayoutCurrencyInvalidInput() {
        insideTextInputLayoutCurrencyRobot.apply {
            type("000000000000")
            checkText(localeHelper.applyTo("0"))
        }
    }

    @Test
    @DisplayName("Given multiple decimal separators within insideTextInputLayoutCurrencyText then only render the first one")
    fun testInsideTextInputLayoutCurrencyMultipleDecimalSeparatorsInput() {
        insideTextInputLayoutCurrencyRobot.apply {
            type("2.5.0")
            checkText(localeHelper.applyTo("2.50"))
        }
    }

    @Test
    @DisplayName("Given groping separators when typing within insideTextInputLayoutCurrencyText then ignore them")
    fun testInsideTextInputLayoutCurrencyIgnoringGroupingSeparatorsInput() {
        insideTextInputLayoutCurrencyRobot.apply {
            type("4,5,0,0,0,0,0")
            checkText(localeHelper.applyTo("4,500,000"))
        }
    }

    @Test
    @DisplayName("When backspace is clicked within insideTextInputLayoutCurrencyText then assert expected value")
    fun testInsideTextInputLayoutCurrencyBackspaceBehaviour() {
        insideTextInputLayoutCurrencyRobot.apply {
            type("00000.4533")
            pressDeleteKey(3)
            type("00")
            checkText(localeHelper.applyTo("0"))
        }
    }

    @Test
    @DisplayName("When dollar is selected then update currency within all CurrencyEditTexts")
    fun testDollarUpdated() {
        testUpdatingCurrencySymbol(rule.activity.getString(R.string.usd_symbol))
    }

    @Test
    @DisplayName("When euro is selected then update currency within all CurrencyEditTexts")
    fun testEuroUpdated() {
        testUpdatingCurrencySymbol(rule.activity.getString(R.string.euro_symbol))
    }

    @Test
    @DisplayName("When yuan is selected then update currency within all CurrencyEditTexts")
    fun testYuanUpdated() {
        testUpdatingCurrencySymbol(rule.activity.getString(R.string.yuan_symbol))
    }

    @Test
    @DisplayName("When pound is selected then update currency within all CurrencyEditTexts")
    fun testPoundUpdated() {
        testUpdatingCurrencySymbol(rule.activity.getString(R.string.pound_symbol))
    }

    @Test
    @DisplayName("When cents is selected then update currency within all CurrencyEditTexts")
    fun testCentsUpdated() {
        testUpdatingCurrencySymbol(rule.activity.getString(R.string.cents_symbol))
    }

    @Test
    @DisplayName("When bitcoins symbol is selected then update currency within all CurrencyEditTexts")
    fun testBitcoinsSymbolUpdated() {
        testUpdatingCurrencySymbol(rule.activity.getString(R.string.bitcoins_symbol))
    }

    @Test
    @DisplayName("When bitcoins text is selected then update currency within all CurrencyEditTexts")
    fun testBitcoinsTextUpdated() {
        testUpdatingCurrencySymbol(rule.activity.getString(R.string.bitcoins_text))
    }

    @Test
    @DisplayName("When whatever text is selected then update currency within all CurrencyEditTexts")
    fun testWhateverTextUpdated() {
        testUpdatingCurrencySymbol(rule.activity.getString(R.string.whatever_text))
    }

    private fun testUpdatingCurrencySymbol(symbol: String) {
        noDecimalCurrencyRobot.type("100000")
        decimalCurrencyRobot.type("200000")
        insideTextInputLayoutCurrencyRobot.type("300000")

        currencySpinnerRobot.selectItem(symbol)

        noDecimalCurrencyRobot.checkText((localeHelper.applyTo("100,000", symbol)))
        decimalCurrencyRobot.checkText((localeHelper.applyTo("200,000", symbol)))
        insideTextInputLayoutCurrencyRobot.checkText((localeHelper.applyTo("300,000", symbol)))
    }
}
