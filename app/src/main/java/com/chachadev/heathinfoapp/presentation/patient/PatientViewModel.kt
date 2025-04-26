package com.chachadev.heathinfoapp.presentation.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.PatientRequest
import kotlinx.coroutines.flow.MutableStateFlow
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// PatientViewModel.kt
class PatientViewModel(private val repository: HealthcareRepository) : ViewModel() {
    private val _patientState = MutableStateFlow<Resource<PatientResponse>?>(null)
    val patientState: StateFlow<Resource<PatientResponse>?> = _patientState

    private val _enrollmentState = MutableStateFlow<Resource<PatientResponse>?>(null)
    val enrollmentState: StateFlow<Resource<PatientResponse>?> = _enrollmentState

    fun registerPatient(patient: PatientRequest) {
        viewModelScope.launch {
            _patientState.value = Resource.Loading()
            _patientState.value = repository.registerPatient(patient)
        }
    }

    fun enrollPatient(patientId: String, programId: String) {
        viewModelScope.launch {
            _enrollmentState.value = Resource.Loading()
            _enrollmentState.value = repository.enrollPatient(patientId, programId)
        }
    }

    fun getPatient(patientId: String) {
        viewModelScope.launch {
            _patientState.value = Resource.Loading()
            _patientState.value = repository.getPatient(patientId)
        }
    }
}