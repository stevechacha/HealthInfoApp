package com.chachadev.heathinfoapp.presentation.clientProgram


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramType
import com.chachadev.heathinfoapp.data.repo.HealthcareRepository
import com.chachadev.heathinfoapp.domain.entity.ProgramRequest
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateProgramViewModel(
    private val healthRepository: HealthcareRepository
) : ViewModel() {

    // Sealed class for UI state
    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val program: ProgramResponse) : UiState()
        data class Error(val message: String) : UiState()
    }

    // Form state
    private val _formState = MutableStateFlow(ProgramFormState())
    val formState = _formState.asStateFlow()

    // UI state
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    // Handle form field updates
    fun onNameChange(name: String) {
        _formState.update { it.copy(name = name, nameError = null) }
    }

    fun onTypeChange(type: ProgramType) {
        _formState.update { it.copy(type = type, typeError = null) }
    }

    fun onAgeGroupChange(ageGroup: String) {
        _formState.update {
            it.copy(
                targetAgeGroup = ageGroup,
                ageGroupError = if (ageGroup.isNotBlank() && !ageGroup.matches(Regex("^\\d+-\\d+$"))) {
                    "Format: min-max (e.g., 18-65)"
                } else {
                    null
                }
            )
        }
    }

    fun onRiskFactorsChange(factors: String) {
        _formState.update { it.copy(riskFactors = factors) }
    }

    // Validate and submit form
    fun createProgram() {
        val currentState = _formState.value

        // Validate inputs
        val nameError = if (currentState.name.isBlank()) "Program name is required" else null
        val typeError = if (currentState.type == null) "Please select a program type" else null

        _formState.update {
            it.copy(
                nameError = nameError,
                typeError = typeError
            )
        }

        if (nameError != null || typeError != null) {
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val response = healthRepository.createProgram(
                    ProgramRequest(
                        name = currentState.name,
                        program_type = currentState.type!!,
                        target_age_group = currentState.targetAgeGroup.takeIf { it.isNotBlank() },
                        risk_factors = currentState.riskFactors.split(",")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                    )
                )

                when (response) {
                    is Resource.Success -> {
                        response.data?.let { program ->
                            _uiState.value = UiState.Success(program)
                            resetForm()
                        } ?: run {
                            _uiState.value = UiState.Error("Program creation failed")
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.Error(response.message ?: "Unknown error")
                    }
                    is Resource.Loading<*> -> {}
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to create program")
            }
        }
    }

    private fun resetForm() {
        _formState.value = ProgramFormState()
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }
}