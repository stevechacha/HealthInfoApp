package com.chachadev.heathinfoapp.presentation.patientList

import androidx.room.Query
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse

data class PatientListUiState(
    val patients: List<PatientResponse>?= emptyList(),
    val programs: List<ProgramResponse>? = emptyList(),
    val enrolledPatient: PatientResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val patientId: String = ""
)
