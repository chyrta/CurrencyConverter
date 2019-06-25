package com.chyrta.converter.data.source

import com.chyrta.converter.data.model.RateList
import com.chyrta.converter.data.source.remote.RateService
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.lang.Exception

class RatesRepositoryTest {
    private lateinit var ratesRepository: RatesRepository
    private lateinit var ratesTestObserver: TestObserver<RateList>
    @Mock private lateinit var ratesService: RateService

    @Before
    fun setupRatesRepository() {
        MockitoAnnotations.initMocks(this)

        ratesRepository = RatesRepository(ratesService)
        ratesTestObserver = TestObserver()
    }

    @Test
    fun getRatesRequestFromNetwork() {
        val rates = mapOf(
            Pair("EUR", 0.86F),
            Pair("CZK", 22.10F)
        )

        val rateList = RateList(
            base = "USD",
            date = "2019-01-01",
            rates = rates
        )

        `when`(ratesService.getLatest("EUR")).thenReturn(Single.just(rateList))

        ratesRepository.getRates("EUR").subscribe(ratesTestObserver)

        ratesTestObserver.assertValueAt(0, rateList)
    }

    @Test
    fun getRatesHandleErrors() {
        `when`(ratesService.getLatest("EUR")).thenReturn(Single.error(Exception()))

        ratesRepository.getRates("EUR").subscribe(ratesTestObserver)

        ratesTestObserver.assertNoValues()
    }
}
