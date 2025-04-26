package com.chachadev.heathinfoapp.di


import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repoModule = module {
    single { "securekey123" }

    single {
        HealthcareRepository(
            apiService = get(),
            db = get(),
            apiKey = "securekey123",
            context = androidContext()
        )
    }

}
