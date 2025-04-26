package com.chachadev.heathinfoapp.di

import androidx.room.Room
import com.chachadev.heathinfoapp.data.local.HealthcareDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    // Provide Room Database singleton
    single {
        Room.databaseBuilder(
            androidContext(),
            HealthcareDatabase::class.java,
            "health-info-db"
        ).build()
    }

    // Database
    single { HealthcareDatabase.getDatabase(androidContext()) }

    // DAOs
    single { get<HealthcareDatabase>().patientDao() }
    single { get<HealthcareDatabase>().programDao() }
    single { get<HealthcareDatabase>().enrollmentDao() }
}
