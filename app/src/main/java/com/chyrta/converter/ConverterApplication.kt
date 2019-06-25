package com.chyrta.converter

import android.app.Application
import com.chyrta.converter.di.networkModule
import com.chyrta.converter.di.ratesViewModule
import com.chyrta.converter.di.repositoryModule
import com.chyrta.converter.di.schedulerProvides
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ConverterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ConverterApplication)
            modules(listOf(ratesViewModule, networkModule, schedulerProvides, repositoryModule))
        }
    }
}
