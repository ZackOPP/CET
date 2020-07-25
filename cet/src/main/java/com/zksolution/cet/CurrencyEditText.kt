package com.zksolution.cet

import android.content.Context
import android.text.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.os.LocaleListCompat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat

class CurrencyEditText(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    private var onDataValueChangedListener: ((String) -> Unit)? = null
    private var decimalPlaces = 0

    private val currentLocale = LocaleListCompat.getDefault()[0]
    private val integerFormat = NumberFormat.getIntegerInstance(currentLocale)
    private val decimalFormatSymbols = DecimalFormatSymbols.getInstance(currentLocale)
    private val groupingSeparator = decimalFormatSymbols.groupingSeparator.toString()
    private val decimalSeparator = decimalFormatSymbols.decimalSeparator.toString()
    private val dataValueDecimalSeparator = "."

    private var currencySymbol = decimalFormatSymbols.currencySymbol
    private val prefix: String
        get() = "$currencySymbol "

    /**
     * We need to store the previous integer part value so that we can recover in case of failure.
     * Specially when the user types a large number that can't be parsed
     */
    private var previousIntegerPartValue = 0L

    init {
        setAttributes(attrs)
    }

    init {
        integerFormat.maximumFractionDigits = 0

        filters = arrayOf(
            object : InputFilter {
                override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {

                    val destination = dest ?: ""
                    val destinationContainsDecimalSeparator = destination.contains(decimalSeparator)

                    return if ((destinationContainsDecimalSeparator && source == decimalSeparator) ||
                        (destinationContainsDecimalSeparator && destination.split(decimalSeparator).last().length >= decimalPlaces)) {
                        ""
                    } else {
                        null
                    }
                }
            })

        addTextChangedListener(
            object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {
                    // No-op
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    removeGroupingSeparator(s.toString())
                        .split(decimalSeparator)[0]
                        .replace(currencySymbol, "")
                        .trim()
                        .toLongOrNull()?.let { previousIntegerPartValue = it }
                }

                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    removeTextChangedListener(this)

                    val value = text.toString()

                    val userValue = if (value.startsWith(currencySymbol) && value.length < prefix.length) {
                        prefix
                    } else {
                        getUserValue(value)
                    }

                    setText(userValue)

                    addTextChangedListener(this)

                    onDataValueChangedListener?.invoke(getDataValue())
                }
            }
        )
    }

    fun setOnDataValueChangedListener(listener: (String) -> Unit) {
        onDataValueChangedListener = listener
    }

    fun setValueFromDataValue(dataValue: String) {
        setText(dataValue.replace(dataValueDecimalSeparator, decimalSeparator))
    }

    fun getDataValue(): String {
        return removeGroupingSeparator(text.toString())
            .replace(decimalSeparator, dataValueDecimalSeparator)
            .replace(currencySymbol, "")
            .trim()
    }

    fun setCurrencySymbol(symbol: String) {
        val newText = text
            .toString()
            .replace(currencySymbol, symbol)
        currencySymbol = symbol
        setText(newText)
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        try {
            setSelection(length())
        } catch (e: IndexOutOfBoundsException) {
            // This method will be called twice when the user paste the value from the clipboard.
            // The first call will throw an IndexOutOfBoundsException, the second one will succeed.
        }
    }

    private fun getUserValue(value: String): String {
        if (value.isEmpty()) return value

        val valueArray = removeGroupingSeparator(value)
            .split(decimalSeparator)
            .toMutableList()

        val integerPart = valueArray[0]
            .replace(currencySymbol, "")
            .trim()

        if (integerPart.isEmpty()) return value

        val integerUserValue = integerFormat.format(integerPart.toLongOrNull() ?: previousIntegerPartValue)

        valueArray[0] = prefix + integerUserValue

        return valueArray.joinToString(decimalSeparator)
    }

    private fun removeGroupingSeparator(value: String): String = value.replace(groupingSeparator, "")

    private fun setAttributes(attrs: AttributeSet?) {
        val inputType = attrs
            ?.getAttributeIntValue(
                "http://schemas.android.com/apk/res/android",
                "inputType",
                -1
            )

        require(
            inputType != null &&
                    (inputType == InputType.TYPE_CLASS_NUMBER || inputType == InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_CLASS_NUMBER)
        ) {
            "You must set an input type, either 'number' or 'numberDecimal'"
        }

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CurrencyEditText,
            0,
            0
        ).apply {
            try {
                decimalPlaces = getInt(R.styleable.CurrencyEditText_decimalPlaces, 0)
                require(
                    decimalPlaces == 0 && inputType == InputType.TYPE_CLASS_NUMBER ||
                            (decimalPlaces > 0 && inputType == InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_CLASS_NUMBER)
                ) {
                    "You must define a value for decimalPlaces when you set the inputType as 'numberDecimal'"
                }
            } finally {
                recycle()
            }
        }
    }
}
