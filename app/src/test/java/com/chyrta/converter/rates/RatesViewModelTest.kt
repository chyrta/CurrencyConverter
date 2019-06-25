package com.chyrta.converter.rates

import com.chyrta.converter.data.model.Rate
import com.chyrta.converter.data.model.RateList
import com.chyrta.converter.data.source.RatesRepository
import com.chyrta.converter.util.schedulers.BaseSchedulerProvider
import com.chyrta.converter.util.schedulers.ImmediateSchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class RatesViewModelTest {

    @Mock
    private lateinit var ratesRepository: RatesRepository
    private lateinit var schedulerProvider: BaseSchedulerProvider
    private lateinit var ratesViewModel: RatesViewModel
    private lateinit var testObserver: TestObserver<RatesViewState>
    private lateinit var rateList: RateList
    private lateinit var rates: Map<String, Float>
    private lateinit var convertedList: List<Rate>

    @Before
    fun setupRatesViewModel() {
        MockitoAnnotations.initMocks(this)

        schedulerProvider = ImmediateSchedulerProvider()

        ratesViewModel = RatesViewModel(RatesActionProcessorHolder(ratesRepository, schedulerProvider))

        rates = mapOf(
            Pair("EUR", 0.86F),
            Pair("CZK", 22.10F)
        )

        rateList = RateList(
            base = "USD",
            date = "2019-01-01",
            rates = rates
        )

        convertedList = listOf(
            Rate("USD", 1.0F),
            Rate("EUR", 0.86F),
            Rate("CZK", 22.10F)
        )

        testObserver = ratesViewModel.states().test()
    }

    @Test
    fun loadRatesFromRepositoryAndLoadIntoView() {
        `when`(ratesRepository.getRates("USD")).thenReturn(Single.just(rateList))

        ratesViewModel.processIntents(Observable.just(RatesIntent.InitialIntent("USD")))

        testObserver.assertValueAt(0) { it.base == "EUR" }
        testObserver.assertValueAt(0) { it.rates.isEmpty() }
        testObserver.assertValueAt(1) { it.base == "USD" }
        testObserver.assertValueAt(1) { it.rates == convertedList }
    }

    @Test
    fun errorLoadingRatesAndShowError() {
        `when`(ratesRepository.getRates("EUR")).thenReturn(Single.error(Exception()))

        ratesViewModel.processIntents(Observable.just(RatesIntent.RefreshRatesIntent("EUR")))

        testObserver.assertValueAt(0) { it.error == null }
        testObserver.assertValueAt(1) { it.error != null }
    }
}
