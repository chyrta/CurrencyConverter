package com.chyrta.converter.rates

import com.chyrta.converter.base.MviAction
import com.chyrta.converter.data.model.Rate

sealed class RatesAction : MviAction {
    data class RefreshRatesAction(val base: String) : RatesAction()
    data class ChangeCurrencyAction(val base: String) : RatesAction()
    data class ChangeAmountAction(val rate: Rate) : RatesAction()
}
