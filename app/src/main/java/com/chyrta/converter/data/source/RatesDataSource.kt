package com.chyrta.converter.data.source

import com.chyrta.converter.data.model.RateList
import io.reactivex.Single

interface RatesDataSource {
    fun getRates(base: String): Single<RateList>
}
