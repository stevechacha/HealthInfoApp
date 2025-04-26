package com.chachadev.heathinfoapp.presentation.enrollClient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PatientEnrollmentViewModel(private val repository: HealthcareRepository) : ViewModel(){

    private val _enrollmentState = MutableStateFlow<Resource<PatientResponse>?>(null)
    val enrollmentState: StateFlow<Resource<PatientResponse>?> = _enrollmentState


    fun enrollPatient(patientId: String, programId: String) {
        viewModelScope.launch {
            _enrollmentState.value = Resource.Loading()
            _enrollmentState.value = repository.enrollPatient(patientId, programId)
        }
    }
}