package com.zksolution.cet

import android.content.Context
import android.text.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.os.LocaleListCompat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat

open class NumericEditText(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    private val currentLocale = LocaleListCompat.getDefault()[0]

    protected val decimalFormatSymbols: DecimalFormatSymbols = DecimalFormatSymbols.getInstance(currentLocale)
    protected val decimalSeparator = decimalFormatSymbols.decimalSeparator.toString()
    protected val integerFormat: NumberFormat = NumberFormat
        .getIntegerInstance(currentLocale)
        .apply { maximumFractionDigits = 0 }

    private var onDataValueChangedListener: ((String) -> Unit)? = null
    private var decimalPlaces = 0

    private val groupingSeparator = decimalFormatSymbols.groupingSeparator.toString()
    private val dataValueDecimalSeparator = "."

    /**
     * We need to store the previous integer part value so that we can recover in case of failure.
     * Specially when the user types a large number that can't be parsed
     */
    protected var previousIntegerPartValue = 0L

    init {
        setAttributes(attrs)
        setUpFilters()
        setListeners()
    }

    fun setOnDataValueChangedListener(listener: (String) -> Unit) {
        onDataValueChangedListener = listener
    }

    fun setValueFromDataValue(dataValue: String) {
        setText(dataValue.replace(dataValueDecimalSeparator, decimalSeparator))
    }

    open fun getDataValue(): String {
        return removeGroupingSeparator(text.toString())
            .replace(decimalSeparator, dataValueDecimalSeparator)
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

    protected open fun getIntegerPartFromUserValue(userValue: String): String {
        return removeGroupingSeparator(userValue).split(decimalSeparator)[0]
    }

    protected open fun formatToUserValue(value: String): String {
        if (value.isEmpty()) return value
        val valueArray = removeGroupingSeparator(value)
            .split(decimalSeparator)
            .toMutableList()
        val integerPart = valueArray[0]
        if (integerPart.isEmpty()) return value
        valueArray[0] = integerFormat.format(integerPart.toLongOrNull() ?: previousIntegerPartValue)
        return valueArray.joinToString(decimalSeparator)
    }

    protected fun removeGroupingSeparator(value: String): String = value.replace(groupingSeparator, "")

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
            R.styleable.NumericEditText,
            0,
            0
        ).apply {
            try {
                decimalPlaces = getInt(R.styleable.NumericEditText_decimalPlaces, 0)
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

    private fun setUpFilters() {
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
            }
        )
    }

    private fun setListeners() {
        addTextChangedListener(
            object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {
                    // No-op
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    getIntegerPartFromUserValue(s.toString())
                        .toLongOrNull()?.let { previousIntegerPartValue = it }
                }

                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    removeTextChangedListener(this)

                    val value = text.toString()
                    setText(formatToUserValue(value))

                    addTextChangedListener(this)

                    onDataValueChangedListener?.invoke(getDataValue())
                }
            }
        )
    }
}
