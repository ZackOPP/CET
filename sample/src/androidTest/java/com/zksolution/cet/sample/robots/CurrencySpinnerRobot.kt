package com.zksolution.cet.sample.robots

import com.zksolution.cet.sample.R
import com.zksolution.cet.sample.utils.clickItemInSpinner

class CurrencySpinnerRobot {

    fun selectItem(item: String) = R.id.currencySpinner.clickItemInSpinner(item)
}
