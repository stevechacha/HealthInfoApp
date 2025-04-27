package com.chachadev.heathinfoapp.presentation.enrollPatient

import com.chachadev.heathinfoapp.data.local.HealthcareDatabase
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse

sealed class EnrollUiState {
    data object Loading : EnrollUiState()
    data class Error(val message: String) : EnrollUiState()
    data class Success(
        val patients: List<PatientResponse>,
        val programs: List<ProgramResponse>,
        val enrolledPatient: PatientResponse? = null,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ) : EnrollUiState()
}