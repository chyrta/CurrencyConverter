package com.chyrta.converter.di

import com.chyrta.converter.data.source.RatesRepository
import com.chyrta.converter.data.source.remote.RateService
import com.chyrta.converter.rates.RatesActionProcessorHolder
import com.chyrta.converter.rates.RatesViewModel
import com.chyrta.converter.util.schedulers.BaseSchedulerProvider
import com.chyrta.converter.util.schedulers.SchedulerProvider
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val ratesViewModule = module {
    single { RatesActionProcessorHolder(get(), get()) }
    viewModel { RatesViewModel(get()) }
}

val schedulerProvides = module {
    single<BaseSchedulerProvider> { SchedulerProvider }
}

val repositoryModule = module {
    single { RatesRepository(get()) }
}

val networkModule = module {
    single { provideRetrofit() }
    single { provideRateService(get()) }
}

fun provideRetrofit(): Retrofit =
    Retrofit.Builder()
        .baseUrl(RateService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

fun provideRateService(retrofit: Retrofit): RateService = retrofit.create(RateService::class.java)
