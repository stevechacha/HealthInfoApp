package com.chachadev.heathinfoapp.presentation.enrollClient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.EnrollmentRequest
import com.chachadev.heathinfoapp.domain.entity.PatientRequest
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EnrollPatientViewModel(
    private val healthRepository: HealthcareRepository
) : ViewModel() {

    sealed class EnrollState {
        object Idle : EnrollState()
        object Loading : EnrollState()
        data class Success(val patient: PatientResponse) : EnrollState()
        data class Error(val message: String) : EnrollState()
    }

    private val _enrollState = MutableStateFlow<EnrollState>(EnrollState.Idle)
    val enrollState: StateFlow<EnrollState> = _enrollState

    private val _patients = MutableStateFlow<Resource<List<PatientResponse>>>(Resource.Loading())
    val patients: StateFlow<Resource<List<PatientResponse>>> = _patients

    private val _programs = MutableStateFlow<Resource<List<ProgramResponse>>>(Resource.Loading())
    val programs: StateFlow<Resource<List<ProgramResponse>>> = _programs

    init {
        loadPatientsAndPrograms()
    }

    fun loadPatientsAndPrograms() {
        viewModelScope.launch {
            _patients.value = Resource.Loading()
            _programs.value = Resource.Loading()

            try {
                // Load patients
                healthRepository.getPatients().collect { patientResource ->
                    _patients.value = patientResource
                }

                // Load programs
                healthRepository.getPrograms().collect { programResource ->
                    _programs.value = programResource
                }

            } catch (e: Exception) {
                _enrollState.value = EnrollState.Error(
                    e.message ?: "Failed to load patients and programs"
                )
            }
        }
    }

    fun enrollPatient(patientId: String, programId: String) {
        viewModelScope.launch {
            _enrollState.value = EnrollState.Loading
            try {
                when (val response = healthRepository.enrollPatient(patientId, programId)) {
                    is Resource.Success -> {
                        response.data?.let { patient ->
                            _enrollState.value = EnrollState.Success(patient)
                        } ?: run {
                            _enrollState.value = EnrollState.Error("Enrollment failed - no data received")
                        }
                    }
                    is Resource.Error -> {
                        _enrollState.value = EnrollState.Error(
                            response.message ?: "Failed to enroll patient"
                        )
                    }

                    is Resource.Loading<*> -> {

                    }
                }
            } catch (e: Exception) {
                _enrollState.value = EnrollState.Error(
                    e.message ?: "Failed to enroll patient"
                )
            }
        }
    }

    fun resetState() {
        _enrollState.value = EnrollState.Idle
    }
}