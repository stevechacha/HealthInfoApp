package com.chachadev.heathinfoapp.presentation.programsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramType
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgramsListViewModel(
    private val repository: HealthcareRepository
) : ViewModel() {

    private val _programs = MutableStateFlow(ProgramsUiState())
    val programs: StateFlow<ProgramsUiState> = _programs.asStateFlow()

    private var allPrograms = listOf<ProgramResponse>()

    init {
        loadPrograms()
    }

    private fun loadPrograms(refresh: Boolean = false) {
        viewModelScope.launch {
            _programs.update { it.copy(isLoading = true) }
            try {
                repository.getPrograms(refresh).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            allPrograms = resource.data ?: emptyList()
                            _programs.update { it.copy(programs = resource.data) }
                        }
                        is Resource.Error -> _programs.update { it.copy(errorMessage = resource.message) }
                        is Resource.Loading<*> -> {_programs.update { it.copy(isLoading = true) }}
                    }
                }
            } catch (e: Exception) {
                _programs.update { it.copy(errorMessage = e.message ?: "Failed to load programs") }
            }
        }
    }

    fun filterPrograms(type: ProgramType?) {
        _programs.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (type == null){
                _programs.update { it.copy(isLoading = false, programs = allPrograms) }

            } else {
                allPrograms.filter { it.program_type == type }
            }
        }
    }

    fun refresh() {
        loadPrograms(refresh = true)
    }
}