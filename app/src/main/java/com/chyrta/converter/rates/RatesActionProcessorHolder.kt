package com.chyrta.converter.rates

import com.chyrta.converter.data.model.Rate
import com.chyrta.converter.data.source.RatesRepository
import com.chyrta.converter.rates.RatesAction.*
import com.chyrta.converter.rates.RatesResult.*
import com.chyrta.converter.util.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class RatesActionProcessorHolder(
  private val ratesRepository: RatesRepository,
  private val schedulerProvider: BaseSchedulerProvider
) {

    private val refreshRatesProcessor = ObservableTransformer<RefreshRatesAction, RefreshRatesResult> { actions ->
        actions.flatMap { action ->
            ratesRepository.getRates(action.base)
                .toObservable()
                .map { it.rates.map { Rate(it.key, it.value) }.toMutableList() }
                .map {
                    it.add(0, Rate(action.base, 1.00F))
                    it
                }
                .map { RefreshRatesResult.Success(action.base, it) }
                .cast(RefreshRatesResult::class.java)
                .onErrorReturn(RefreshRatesResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(RefreshRatesResult.InFlight)
        }
    }

    private val changeAmountProcessor = ObservableTransformer<ChangeAmountAction, ChangeAmountResult> { actions ->
        actions.flatMap { action ->
            Observable.just(action.rate)
                .map { ChangeAmountResult.Success(it) }
                .cast(ChangeAmountResult::class.java)
                .onErrorReturn(ChangeAmountResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(ChangeAmountResult.InFlight)
        }
    }

    private val changeCurrencyProcessor = ObservableTransformer<ChangeCurrencyAction, ChangeCurrencyResult> { actions ->
        actions.flatMap { action ->
            Observable.just(action.base)
                .map { ChangeCurrencyResult.Success(it) }
                .cast(ChangeCurrencyResult::class.java)
                .onErrorReturn(ChangeCurrencyResult::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(ChangeCurrencyResult.InFlight)
        }
    }

    internal var actionProcessor =
        ObservableTransformer<RatesAction, RatesResult> { actions ->
            actions.publish { shared ->
                Observable.merge(
                    shared.ofType(RefreshRatesAction::class.java).compose(refreshRatesProcessor),
                    shared.ofType(ChangeAmountAction::class.java).compose(changeAmountProcessor),
                    shared.ofType(ChangeCurrencyAction::class.java).compose(changeCurrencyProcessor)
                ).mergeWith(
                    shared.filter { v ->
                        v !is RefreshRatesAction &&
                                v !is ChangeAmountAction &&
                                v !is ChangeCurrencyAction
                    }.flatMap { w ->
                        Observable.error<RatesResult>(
                            IllegalArgumentException("Unknown action type: $w")
                        )
                    }
                )
            }
        }
}
