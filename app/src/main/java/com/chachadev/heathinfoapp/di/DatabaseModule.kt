package com.chachadev.heathinfoapp.di

import androidx.room.Room
import com.chachadev.heathinfoapp.data.local.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    // Provide Room Database singleton
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "health-info-db"
        ).build()
    }

    // Provide DAOs
    single { get<AppDatabase>().clientDao() }
    single { get<AppDatabase>().programDao() }
}
