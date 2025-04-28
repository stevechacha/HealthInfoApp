package com.chachadev.heathinfoapp.presentation.patient


import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse

data class PatientRegistrationUIState(
    val isLoading: Boolean = false,
    val nationalId: String = "",
    val fullName: String = "",
    val dateOfBirth: String = "",
    val bloodType: String = "",
    val expanded: Boolean = false,
    val data: PatientResponse? = null,
    val error: String = ""
)
