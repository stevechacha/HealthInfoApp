package com.chachadev.heathinfoapp.presentation.patientDetailsProfile

import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse

data class PatientDetailsUIState(
    val isLoading: Boolean = false,
    val data: PatientResponse? = null,
    val errorMessage: String = ""
)
