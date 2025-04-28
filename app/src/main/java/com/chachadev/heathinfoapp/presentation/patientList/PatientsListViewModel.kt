package com.chachadev.heathinfoapp.presentation.patientList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PatientsListViewModel(
    private val repository: HealthcareRepository
) : ViewModel() {

    private val _patientsState = MutableStateFlow(PatientListUiState())
    val patientsState: StateFlow<PatientListUiState> = _patientsState

    init {
        loadAllPatients()
    }

    private fun loadAllPatients(refresh: Boolean = false) {
        viewModelScope.launch {
            _patientsState.update { it.copy(isLoading = true, isRefreshing = refresh) }
            repository.getPatients(refresh).collect { resource ->
                when(resource) {
                    is Resource.Error -> {
                        _patientsState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message,
                                isRefreshing = false
                            )
                        }
                    }
                    is Resource.Success -> {
                        _patientsState.update {
                            it.copy(
                                isLoading = false,
                                patients = resource.data,
                                isRefreshing = false,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _patientsState.update {
                            it.copy(
                                isLoading = true,
                                isRefreshing = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _patientsState.update { it.copy(searchQuery = query) }
    }

    fun refresh() {
        loadAllPatients(refresh = true)
    }
}