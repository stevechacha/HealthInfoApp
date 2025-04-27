package com.chachadev.heathinfoapp.presentation.patientList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PatientsListViewModel(
    private val repository: HealthcareRepository
) : ViewModel() {

    // State for all patients
    private val _patientsState = MutableStateFlow<Resource<List<PatientResponse>>>(Resource.Loading())
    val patientsState: StateFlow<Resource<List<PatientResponse>>> = _patientsState

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Refresh state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        loadAllPatients()
    }

    private fun loadAllPatients(refresh: Boolean = false) {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.getPatients(refresh).collect { resource ->
                _patientsState.value = resource
                _isRefreshing.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun refresh() {
        loadAllPatients(refresh = true)
    }
}