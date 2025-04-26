package com.chachadev.heathinfoapp.di

import org.koin.dsl.module

fun appModule() = module {
    apiModule
    databaseModule
    environmentModule
    networkModule
    repoModule
    screenViewModelModule
}