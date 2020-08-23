package com.zksolution.cet.sample.utils

import androidx.core.os.LocaleListCompat
import java.text.DecimalFormatSymbols

class LocaleHelper(
    private val defaultGroupingSeparator: String,
    private val defaultDecimalSeparator: String
) {

    private val currentLocale = LocaleListCompat.getDefault()[0]
    private val decimalFormatSymbols = DecimalFormatSymbols.getInstance(currentLocale)
    private val groupingSeparator = decimalFormatSymbols.groupingSeparator.toString()
    private val decimalSeparator = decimalFormatSymbols.decimalSeparator.toString()
    private var currencySymbol = decimalFormatSymbols.currencySymbol

    fun applyToCurrency(value: String, currency: String = currencySymbol): String {
        val valueArray = value.splitByDecimalSeparator()
        valueArray[0] =
            "$currency " + valueArray[0].replaceDefaultGroupingSeparator()
        return valueArray.joinToString(decimalSeparator)
    }

    fun applyToNumeric(value: String): String {
        val valueArray = value.splitByDecimalSeparator()
        valueArray[0] = valueArray[0].replaceDefaultGroupingSeparator()
        return valueArray.joinToString(decimalSeparator)
    }

    private fun String.splitByDecimalSeparator() = this
        .split(defaultDecimalSeparator)
        .toMutableList()

    private fun String.replaceDefaultGroupingSeparator() = this
        .replace(defaultGroupingSeparator, groupingSeparator)
}
