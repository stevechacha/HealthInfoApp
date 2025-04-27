package com.chachadev.heathinfoapp.presentation.patientDetailsProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PatientDetailsViewModel(
    private val repository: HealthcareRepository
) : ViewModel() {

    private val _patientState = MutableStateFlow<Resource<PatientResponse>>(Resource.Loading())
    val patientState: StateFlow<Resource<PatientResponse>> = _patientState

    fun loadPatient(patientId: String, refresh: Boolean = false) {
        viewModelScope.launch {
            _patientState.value = Resource.Loading()
            try {
                val response = repository.getPatient(patientId, refresh)
                _patientState.value = response
            } catch (e: Exception) {
                _patientState.value = Resource.Error(data = null, message = e.message.toString())
            }
        }
    }
}