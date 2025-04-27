package com.chachadev.heathinfoapp.presentation.enrollPatient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EnrollPatientViewModel(
    private val healthRepository: HealthcareRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EnrollUiState>(EnrollUiState.Loading)
    val uiState: StateFlow<EnrollUiState> = _uiState

    init {
        loadInitialData()
    }



    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = EnrollUiState.Loading

            val patientsResult = healthRepository.getPatients().first()
            val programsResult = healthRepository.getPrograms().first()

            println(patientsResult)
            println(programsResult)

            when {
                patientsResult is Resource.Error -> {
                    _uiState.value = EnrollUiState.Error(
                        patientsResult.message ?: "Failed to load patients"
                    )
                }
                programsResult is Resource.Error -> {
                    _uiState.value = EnrollUiState.Error(
                        programsResult.message ?: "Failed to load programs"
                    )
                }
                programsResult is Resource.Loading ->{

                }
                patientsResult is Resource.Loading ->{

                }
                else -> {
                    _uiState.value = EnrollUiState.Success(
                        patients = (patientsResult as Resource.Success).data ?: emptyList(),
                        programs = (programsResult as Resource.Success).data ?: emptyList()
                    )
                }
            }
        }
    }

    fun enrollPatient(patientId: String, programId: String) {
        viewModelScope.launch {
            _uiState.value = (_uiState.value as? EnrollUiState.Success)?.copy(
                isLoading = true
            ) ?: return@launch

            healthRepository.enrollPatient(patientId, programId)
                .collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            _uiState.value = EnrollUiState.Success(
                                patients = (_uiState.value as? EnrollUiState.Success)?.patients ?: emptyList(),
                                programs = (_uiState.value as? EnrollUiState.Success)?.programs ?: emptyList(),
                                enrolledPatient = response.data,
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {
                            _uiState.value = (_uiState.value as? EnrollUiState.Success)?.copy(
                                isLoading = false,
                                errorMessage = response.message
                            ) ?: return@collect
                        }

                        is Resource.Loading<*> -> {
                            _uiState.value = (_uiState.value as? EnrollUiState.Success)?.copy(
                                isLoading = true
                            ) ?: return@collect
                        }
                    }
                }
        }
    }
}

