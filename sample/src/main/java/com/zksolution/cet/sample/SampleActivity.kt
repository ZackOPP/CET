package com.zksolution.cet.sample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import kotlinx.android.synthetic.main.layout_currency.*

class SampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        currencySpinner.setUp()
        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.run {
                    val currencySymbol = getItemAtPosition(position) as String
                    updateCurrencySymbol(currencySymbol)
                }
            }
        }
    }

    private fun AppCompatSpinner.setUp() =  ArrayAdapter.createFromResource(
        this@SampleActivity,
        R.array.currencies_sample,
        android.R.layout.simple_spinner_item
    ).also { adapter ->
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.adapter = adapter
    }

    private fun updateCurrencySymbol(symbol: String) {
        noDecimalCurrencyEditText.setCurrencySymbol(symbol)
        decimalCurrencyEditText.setCurrencySymbol(symbol)
        insideTextInputLayoutCurrencyEditText.setCurrencySymbol(symbol)
    }
}
