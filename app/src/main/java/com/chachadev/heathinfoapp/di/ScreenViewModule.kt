package com.chachadev.heathinfoapp.di

import com.chachadev.heathinfoapp.presentation.createProgram.CreateProgramViewModel
import com.chachadev.heathinfoapp.presentation.enrollPatient.EnrollPatientViewModel
import com.chachadev.heathinfoapp.presentation.patient.PatientRegistrationViewModel
import com.chachadev.heathinfoapp.presentation.patientDetailsProfile.PatientDetailsViewModel
import com.chachadev.heathinfoapp.presentation.patientList.PatientsListViewModel
import com.chachadev.heathinfoapp.presentation.programsList.ProgramsListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val screenViewModelModule = module {
    viewModel { CreateProgramViewModel(get()) }
    viewModel { PatientRegistrationViewModel(get()) }
    viewModel { PatientRegistrationViewModel(get()) }
    viewModel { PatientsListViewModel(get()) }
    viewModel { PatientDetailsViewModel(get()) }
    viewModel { EnrollPatientViewModel(get()) }
    viewModel { ProgramsListViewModel(get()) }
    viewModel { EnrollPatientViewModel(get()) }

}
