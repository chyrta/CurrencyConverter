package com.chyrta.converter.rates

import com.chyrta.converter.base.MviIntent
import com.chyrta.converter.data.model.Rate

sealed class RatesIntent : MviIntent {
    data class InitialIntent(val currency: String) : RatesIntent()
    data class RefreshRatesIntent(val currency: String) : RatesIntent()
    data class ChangeCurrencyIntent(val currency: String) : RatesIntent()
    data class ChangeAmountIntent(val amount: Rate) : RatesIntent()
}
