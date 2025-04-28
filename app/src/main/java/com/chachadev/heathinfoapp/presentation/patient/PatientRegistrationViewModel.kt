package com.chachadev.heathinfoapp.presentation.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.PatientRequest
import kotlinx.coroutines.flow.MutableStateFlow
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientRegistrationViewModel(private val repository: HealthcareRepository) : ViewModel() {
    private val _patientUiState = MutableStateFlow(PatientRegistrationUIState())
    val patientUiState: StateFlow<PatientRegistrationUIState> = _patientUiState

    fun registerPatient(patient: PatientRequest) {
        viewModelScope.launch {
            try {
                repository.registerPatient(patient).collect { result ->
                    when (result) {
                        is Resource.Error -> {
                            _patientUiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.message ?: "Unknown error"
                                )
                            }
                        }

                        is Resource.Loading -> {
                            _patientUiState.update { it.copy(isLoading = true) }
                        }

                        is Resource.Success -> {
                            _patientUiState.update {
                                it.copy(
                                    isLoading = false,
                                    data = result.data,
                                    error = ""
                                )
                            }
                        }
                    }

                }
            } catch (e: Exception) {
                _patientUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to register patient"
                    )
                }
            }
        }
    }

    fun clearForm() {
        _patientUiState.update {
            it.copy(
                nationalId = "",
                fullName = "",
                dateOfBirth = "",
                bloodType = "",
                expanded = false,
                // Keep the data for the success dialog
                isLoading = false
            )
        }
    }

    fun updateNationId(nationalId: String) {
        _patientUiState.update { it.copy(nationalId = nationalId) }
    }

    fun updateFullName(fullName: String) {
        _patientUiState.update { it.copy(fullName = fullName) }
    }

    fun updateBloodType(bloodType: String) {
        _patientUiState.update { it.copy(bloodType = bloodType) }
    }

    fun updateDateOfBirth(dateOfBirth: String) {
        _patientUiState.update { it.copy(dateOfBirth = dateOfBirth) }
    }

    fun updateExpand(expanded: Boolean) {
        _patientUiState.update { it.copy(expanded = expanded) }
    }

    fun updateError(error: String) {
        _patientUiState.update { it.copy(error = error) }
    }
}