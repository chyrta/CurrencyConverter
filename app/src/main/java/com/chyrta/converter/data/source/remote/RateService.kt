package com.chyrta.converter.data.source.remote

import com.chyrta.converter.data.model.RateList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RateService {

    @GET("/latest")
    fun getLatest(@Query("base") currency: String): Single<RateList>

    companion object {
        val BASE_URL = "https://revolut.duckdns.org"
    }
}
