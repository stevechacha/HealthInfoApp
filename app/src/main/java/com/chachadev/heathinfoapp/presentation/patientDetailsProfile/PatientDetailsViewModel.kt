package com.chachadev.heathinfoapp.presentation.patientDetailsProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientDetailsViewModel(
    private val repository: HealthcareRepository
) : ViewModel() {

    private val _patientState = MutableStateFlow(PatientDetailsUIState())
    val patientState: StateFlow<PatientDetailsUIState> = _patientState

    fun loadPatient(patientId: String, refresh: Boolean = false) {
        viewModelScope.launch {
            _patientState.update { it.copy(isLoading = true) }
            try {
                when(val response = repository.getPatient(patientId, refresh)){
                    is Resource.Error<*> -> {
                        _patientState.update { it.copy(isLoading = false, errorMessage = response.message!!) }
                    }
                    is Resource.Loading<*> -> {
                        _patientState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success<*> -> {
                        _patientState.update { it.copy(isLoading = false, data = response.data) }
                    }
                }
            } catch (e: Exception) {
                _patientState.update { it.copy(isLoading = false, errorMessage = e.message.toString()) }
            }
        }
    }
}