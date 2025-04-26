package com.chachadev.heathinfoapp.presentation.programsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramType
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProgramsListViewModel(
    private val repository: HealthcareRepository
) : ViewModel() {

    private val _programs = MutableStateFlow<Resource<List<ProgramResponse>>>(Resource.Loading())
    val programs: StateFlow<Resource<List<ProgramResponse>>> = _programs

    private var allPrograms = listOf<ProgramResponse>()

    init {
        loadPrograms()
    }

    fun loadPrograms(refresh: Boolean = false) {
        viewModelScope.launch {
            _programs.value = Resource.Loading()
            try {
                repository.getPrograms(refresh).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            allPrograms = resource.data ?: emptyList()
                            _programs.value = Resource.Success(allPrograms)
                        }
                        is Resource.Error -> _programs.value = resource
                        is Resource.Loading<*> -> {_programs.value = resource}
                    }
                }
            } catch (e: Exception) {
                _programs.value = Resource.Error(e.message ?: "Failed to load programs")
            }
        }
    }

    fun filterPrograms(type: ProgramType?) {
        _programs.value = Resource.Loading(_programs.value.data)
        viewModelScope.launch {
            _programs.value = Resource.Success(
                if (type == null) {
                    allPrograms
                } else {
                    allPrograms.filter { it.program_type == type }
                }
            )
        }
    }

    fun refresh() {
        loadPrograms(refresh = true)
    }
}