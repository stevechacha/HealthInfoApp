package com.chachadev.heathinfoapp

import android.app.Application
import com.chachadev.heathinfoapp.di.apiModule
import com.chachadev.heathinfoapp.di.databaseModule
import com.chachadev.heathinfoapp.di.environmentModule
import com.chachadev.heathinfoapp.di.networkModule
import com.chachadev.heathinfoapp.di.repoModule
import com.chachadev.heathinfoapp.di.screenViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class HealthInfoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@HealthInfoApp)
            modules(
                networkModule,
                environmentModule,
                apiModule,
                databaseModule,
                environmentModule,
                repoModule,
                screenViewModelModule,
                apiModule
            )
        }
    }
}