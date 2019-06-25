package com.chyrta.converter.rates

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chyrta.converter.R
import com.chyrta.converter.base.MviView
import com.chyrta.converter.data.model.Rate
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class RatesFragment : Fragment(), MviView<RatesIntent, RatesViewState> {

    private lateinit var adapter: RatesAdapter
    private lateinit var currenciesList: RecyclerView

    private val changeAmountIntentPublisher:
            PublishSubject<RatesIntent.ChangeAmountIntent> = PublishSubject.create()
    private val changeCurrencyIntentPublisher:
            PublishSubject<RatesIntent.ChangeCurrencyIntent> = PublishSubject.create()
    private val refreshRatesIntentPublisher:
            PublishSubject<RatesIntent.RefreshRatesIntent> = PublishSubject.create()

    private val disposables = CompositeDisposable()

    private val viewModel: RatesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = RatesAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currenciesList = view.findViewById(R.id.rv_currencies)
        currenciesList.layoutManager = LinearLayoutManager(context)
        currenciesList.adapter = adapter
        bind()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    fun bind() {
        disposables.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())

        disposables.addAll(
            adapter.amountChangedObservable
                .subscribe {
                    changeAmountIntentPublisher.onNext(RatesIntent.ChangeAmountIntent(it))
                },
            adapter.baseCurrencyChangedObservable
                .subscribe {
                    changeCurrencyIntentPublisher.onNext(RatesIntent.ChangeCurrencyIntent(it))
                },
            Observable.interval(1, TimeUnit.SECONDS)
                .subscribe {
                    refreshRatesIntentPublisher.onNext(RatesIntent.RefreshRatesIntent(adapter.base))
                }
        )
    }

    private fun initialIntent(): Observable<RatesIntent.InitialIntent> {
        return Observable.just(RatesIntent.InitialIntent(adapter.base))
    }

    private fun changeAmountIntent(): Observable<RatesIntent.ChangeAmountIntent> {
        return changeAmountIntentPublisher
    }

    private fun changeCurrencyIntent(): Observable<RatesIntent.ChangeCurrencyIntent> {
        return changeCurrencyIntentPublisher
    }

    private fun refreshRatesIntent(): Observable<RatesIntent.RefreshRatesIntent> {
        return refreshRatesIntentPublisher
    }

    override fun intents(): Observable<RatesIntent> {
        return Observable.merge(
            initialIntent(),
            changeAmountIntent(),
            changeCurrencyIntent(),
            refreshRatesIntent()
        )
    }

    override fun render(state: RatesViewState) {
        if (state.error != null) {
            showLoadingRatesError(state.error.message!!)
            return
        }

        updateCurrenciesAdapter(state.base, state.amount, state.rates)
    }

    private fun showMessage(message: String) {
        val view = view ?: return
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoadingRatesError(message: String) {
        showMessage(message)
    }

    private fun updateCurrenciesAdapter(base: String, amount: Float, rates: List<Rate>) {
        if (adapter.symbolPosition.isEmpty()) {
            adapter.symbolPosition.addAll(rates.map { it.symbol })
        }

        for (rate in rates) {
            adapter.symbolRate[rate.symbol] = rate
        }

        adapter.base = base
        adapter.amount = amount

        adapter.notifyAdapter()
    }

    companion object {
        operator fun invoke(): RatesFragment = RatesFragment()
    }
}
