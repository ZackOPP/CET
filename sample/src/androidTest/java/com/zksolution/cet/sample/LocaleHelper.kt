package com.zksolution.cet.sample

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

    fun applyTo(value: String, currency: String = currencySymbol): String {
        val valueArray = value
            .split(defaultDecimalSeparator)
            .toMutableList()
        valueArray[0] =
            "$currency " + valueArray[0].replace(defaultGroupingSeparator, groupingSeparator)
        return valueArray.joinToString(decimalSeparator)
    }
}
