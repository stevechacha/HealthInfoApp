package com.chachadev.heathinfoapp.di

import com.chachadev.heathinfoapp.data.network.utils.Environment
import org.koin.dsl.module

val environmentModule = module {
    single<Environment> { Environment.Main }
}