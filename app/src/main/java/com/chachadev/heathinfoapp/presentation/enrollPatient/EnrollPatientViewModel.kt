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

    private val _uiState = MutableStateFlow(EnrollUiState())
    val uiState: StateFlow<EnrollUiState> = _uiState

    init {
        loadAllPatients()
        loadPrograms()
    }

    fun loadInitialData(){
        loadPrograms()
        loadAllPatients()
    }


    private fun loadPrograms(refresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = false) }
            try {
                healthRepository.getPrograms(refresh).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _uiState.update { it.copy(isLoading = false, programs = resource.data) }
                        }
                        is Resource.Error -> {
                            _uiState.update { it.copy(isLoading = false, errorMessage = "Programs Error Message ${resource.message}") }
                        }
                        is Resource.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to load programs") }
            }
        }
    }


    private fun loadAllPatients(refresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = false, isRefreshing = true) }
            healthRepository.getPatients(refresh).collect { result ->
                when(result){
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }

                    }
                    is Resource.Loading-> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { it.copy(isLoading = false, patients = result.data, isRefreshing = false) }

                    }
                }
            }
        }
    }

    fun enrollPatient(patientId: String, programId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            healthRepository.enrollPatient(patientId, programId)
                .collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            _uiState.update { it.copy(isLoading = false, isRefreshing = false, enrolledPatient = response.data) }
                        }
                        is Resource.Error -> {
                            _uiState.update { it.copy(isLoading = false, isRefreshing = false, errorMessage = response.message) }

                        }

                        is Resource.Loading<*> -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }
                    }
                }
        }
    }
}

