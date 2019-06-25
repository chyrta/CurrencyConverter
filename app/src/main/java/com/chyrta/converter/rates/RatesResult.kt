package com.chyrta.converter.rates

import com.chyrta.converter.base.MviResult
import com.chyrta.converter.data.model.Rate

sealed class RatesResult : MviResult {
    sealed class RefreshRatesResult : RatesResult() {
        data class Success(val currency: String, val rates: List<Rate>) : RefreshRatesResult()
        data class Failure(val error: Throwable) : RefreshRatesResult()
        object InFlight : RefreshRatesResult()
    }

    sealed class ChangeCurrencyResult : RatesResult() {
        data class Success(val currency: String) : ChangeCurrencyResult()
        data class Failure(val error: Throwable) : ChangeCurrencyResult()
        object InFlight : ChangeCurrencyResult()
    }

    sealed class ChangeAmountResult : RatesResult() {
        data class Success(val amount: Rate) : ChangeAmountResult()
        data class Failure(val error: Throwable) : ChangeAmountResult()
        object InFlight : ChangeAmountResult()
    }
}
