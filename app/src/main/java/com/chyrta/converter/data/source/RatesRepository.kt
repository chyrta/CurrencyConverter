package com.chyrta.converter.data.source

import com.chyrta.converter.data.model.RateList
import com.chyrta.converter.data.source.remote.RateService
import io.reactivex.Single

open class RatesRepository(private val ratesService: RateService) : RatesDataSource {

    override fun getRates(base: String): Single<RateList> {
        return ratesService.getLatest(base)
    }
}
