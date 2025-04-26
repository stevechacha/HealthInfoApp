package com.chachadev.heathinfoapp.di

import com.chachadev.heathinfoapp.presentation.ClientViewModel
import com.chachadev.heathinfoapp.presentation.clientProgram.CreateProgramViewModel
import com.chachadev.heathinfoapp.presentation.enrollClient.EnrollPatientViewModel
import com.chachadev.heathinfoapp.presentation.enrollClient.PatientEnrollmentViewModel
import com.chachadev.heathinfoapp.presentation.patient.PatientViewModel
import com.chachadev.heathinfoapp.presentation.patientDetails.PatientDetailsViewModel
import com.chachadev.heathinfoapp.presentation.patientList.PatientsListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val screenViewModelModule = module {
    // Provide ViewModel
    viewModel { ClientViewModel(get()) }
    viewModel { CreateProgramViewModel(get()) }
    viewModel { PatientViewModel(get()) }
    viewModel { PatientEnrollmentViewModel(get()) }
    viewModel { PatientViewModel(get()) }
    viewModel { PatientsListViewModel(get()) }
    viewModel { PatientDetailsViewModel(get()) }
    viewModel { EnrollPatientViewModel(get()) }

}
