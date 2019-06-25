package com.chyrta.converter.rates

import com.chyrta.converter.base.MviViewState
import com.chyrta.converter.data.model.Rate

data class RatesViewState(
  val base: String,
  val amount: Float,
  val rates: List<Rate>,
  val error: Throwable?
) : MviViewState {

    companion object {
        fun idle(): RatesViewState {
            return RatesViewState(
                    base = "EUR",
                    amount = 1.00F,
                    rates = emptyList(),
                    error = null
            )
        }
    }
}
