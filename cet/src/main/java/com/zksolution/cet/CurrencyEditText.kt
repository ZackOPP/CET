package com.zksolution.cet

import android.content.Context
import android.util.AttributeSet

class CurrencyEditText(context: Context, attrs: AttributeSet?) : NumericEditText(context, attrs) {

    private var currencySymbol = decimalFormatSymbols.currencySymbol
    private val prefix: String
        get() = "$currencySymbol "

    override fun getIntegerPartFromUserValue(userValue: String): String {
        return super.getIntegerPartFromUserValue(userValue).removePrefix()
    }

    override fun getDataValue(): String {
        return super.getDataValue().removePrefix()
    }

    fun setCurrencySymbol(symbol: String) {
        val newText = text
            .toString()
            .replace(currencySymbol, symbol)
        currencySymbol = symbol
        setText(newText)
    }

    override fun formatToUserValue(value: String): String {
        return if (value.startsWith(currencySymbol) && value.length < prefix.length) {
            prefix
        } else {
            if (value.isEmpty()) return value
            val valueArray = removeGroupingSeparator(value)
                .split(decimalSeparator)
                .toMutableList()
            val integerPart = valueArray[0]
                .replace(currencySymbol, "")
                .trim()
            if (integerPart.isEmpty()) return value
            val integerUserValue =
                integerFormat.format(integerPart.toLongOrNull() ?: previousIntegerPartValue)
            valueArray[0] = prefix + integerUserValue
            return valueArray.joinToString(decimalSeparator)
        }
    }

    private fun String.removePrefix() = this
        .replace(currencySymbol, "")
        .trim()
}
