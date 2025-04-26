package com.chachadev.heathinfoapp.di


import com.chachadev.heathinfoapp.data.repo.ClientRepository
import org.koin.dsl.module

val repoModule = module {
    // Provide Repository
    single { ClientRepository(get(), get()) }

}
