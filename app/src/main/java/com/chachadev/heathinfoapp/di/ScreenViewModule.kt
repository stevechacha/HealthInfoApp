package com.chachadev.heathinfoapp.di

import com.chachadev.heathinfoapp.presentation.ClientViewModel
import com.chachadev.heathinfoapp.presentation.clientProgram.CreateProgramViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val screenViewModelModule = module {
    // Provide ViewModel
    viewModel { ClientViewModel(get()) }
    viewModel { CreateProgramViewModel(get()) }
}
