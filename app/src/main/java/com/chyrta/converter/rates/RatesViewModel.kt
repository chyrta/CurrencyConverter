package com.chyrta.converter.rates

import androidx.lifecycle.ViewModel
import com.chyrta.converter.base.MviViewModel
import com.chyrta.converter.util.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class RatesViewModel(
  private val actionProcessorHolder: RatesActionProcessorHolder
) : ViewModel(), MviViewModel<RatesIntent, RatesViewState> {

    private val intentsSubject: PublishSubject<RatesIntent> = PublishSubject.create()
    private val statesObservable: Observable<RatesViewState> = compose()
    private val disposables = CompositeDisposable()

    private val intentFilter: ObservableTransformer<RatesIntent, RatesIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                        shared.ofType(RatesIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(RatesIntent.InitialIntent::class.java)
                )
            }
        }

    override fun processIntents(intents: Observable<RatesIntent>) {
        disposables.add(intents.subscribe(intentsSubject::onNext))
    }

    override fun states(): Observable<RatesViewState> = statesObservable

    private fun compose(): Observable<RatesViewState> {
        return intentsSubject
                .compose(intentFilter)
                .map(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(RatesViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    private fun actionFromIntent(intent: RatesIntent): RatesAction {
        return when (intent) {
            is RatesIntent.InitialIntent -> RatesAction.RefreshRatesAction(intent.currency)
            is RatesIntent.RefreshRatesIntent -> RatesAction.RefreshRatesAction(intent.currency)
            is RatesIntent.ChangeCurrencyIntent -> RatesAction.ChangeCurrencyAction(intent.currency)
            is RatesIntent.ChangeAmountIntent -> RatesAction.ChangeAmountAction(intent.amount)
        }
    }

    override fun onCleared() {
        disposables.dispose()
    }

    companion object {
        private val reducer = BiFunction { previousState: RatesViewState, result: RatesResult ->
            when (result) {
                is RatesResult.RefreshRatesResult -> when (result) {
                    is RatesResult.RefreshRatesResult.Success -> {
                        previousState.copy(
                                rates = result.rates,
                                base = result.currency,
                                error = null
                        )
                    }
                    is RatesResult.RefreshRatesResult.Failure -> {
                        previousState.copy(
                                error = result.error
                        )
                    }
                    is RatesResult.RefreshRatesResult.InFlight -> {
                        previousState
                    }
                }
                is RatesResult.ChangeCurrencyResult -> when (result) {
                    is RatesResult.ChangeCurrencyResult.Success -> {
                        previousState.copy(
                                base = result.currency,
                                error = null
                        )
                    }
                    is RatesResult.ChangeCurrencyResult.Failure -> {
                        previousState.copy(
                                error = result.error
                        )
                    }
                    is RatesResult.ChangeCurrencyResult.InFlight -> {
                        previousState
                    }
                }
                is RatesResult.ChangeAmountResult -> when (result) {
                    is RatesResult.ChangeAmountResult.Success -> {
                        previousState.copy(
                                amount = result.amount.rate,
                                error = null
                        )
                    }
                    is RatesResult.ChangeAmountResult.Failure -> {
                        previousState.copy(
                                error = result.error
                        )
                    }
                    is RatesResult.ChangeAmountResult.InFlight -> {
                        previousState
                    }
                }
            }
        }
    }
}
