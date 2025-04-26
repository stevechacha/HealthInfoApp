package com.chachadev.heathinfoapp.di

import com.chachadev.heathinfoapp.BuildConfig
import com.chachadev.heathinfoapp.data.network.api.HealthcareApiService
import com.chachadev.heathinfoapp.data.network.utils.Environment
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BODY
            }
        }
    } bind Interceptor::class

    single {
        Retrofit.Builder()
            .baseUrl(Environment.Main.url)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<HealthcareApiService> {
        get<Retrofit>().create(HealthcareApiService::class.java)
    }

}