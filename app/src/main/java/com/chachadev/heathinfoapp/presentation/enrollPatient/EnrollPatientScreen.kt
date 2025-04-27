package com.chachadev.heathinfoapp.presentation.enrollPatient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.domain.entity.Resource
import com.chachadev.heathinfoapp.presentation.patientList.ErrorState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnrollPatientScreen(
    viewModel: EnrollPatientViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onEnrollmentSuccess: (PatientResponse) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedPatientId by remember { mutableStateOf<String?>(null) }
    var selectedProgramId by remember { mutableStateOf<String?>(null) }

    // Handle enrollment success
    LaunchedEffect(uiState) {
        if (uiState is EnrollUiState.Success && (uiState as EnrollUiState.Success).enrolledPatient != null) {
            onEnrollmentSuccess((uiState as EnrollUiState.Success).enrolledPatient!!)
            selectedPatientId = null
            selectedProgramId = null
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Enroll Patient") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is EnrollUiState.Loading -> CircularProgressIndicator()
            is EnrollUiState.Error -> ErrorState(
                message = state.message,
                onRetry = { viewModel.loadInitialData() }
            )
            is EnrollUiState.Success -> {
                EnrollmentForm(
                    patients = state.patients,
                    programs = state.programs,
                    isLoading = state.isLoading,
                    errorMessage = state.errorMessage,
                    selectedPatientId = selectedPatientId,
                    selectedProgramId = selectedProgramId,
                    onPatientSelected = { selectedPatientId = it },
                    onProgramSelected = { selectedProgramId = it },
                    onEnrollClick = { patientId, programId ->
                        viewModel.enrollPatient(patientId, programId)
                    },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
private fun EnrollmentForm(
    patients: List<PatientResponse>,
    programs: List<ProgramResponse>,
    isLoading: Boolean,
    errorMessage: String?,
    selectedPatientId: String?,
    selectedProgramId: String?,
    onPatientSelected: (String) -> Unit,
    onProgramSelected: (String) -> Unit,
    onEnrollClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedPatient by remember { mutableStateOf(false) }
    var expandedProgram by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Patient dropdown
        DropdownSelector(
            label = "Patient",
            items = patients,
            selectedItem = patients.find { it.patient_id == selectedPatientId },
            expanded = expandedPatient,
            onExpandedChange = { expandedPatient = it },
            itemToString = { "${it.full_name} (${it.national_id})" },
            onItemSelected = { onPatientSelected(it.patient_id) }
        )

        // Program dropdown
        DropdownSelector(
            label = "Program",
            items = programs,
            selectedItem = programs.find { it.program_id == selectedProgramId },
            expanded = expandedProgram,
            onExpandedChange = { expandedProgram = it },
            itemToString = { "${it.name} (${it.program_type.name.lowercase()})" },
            onItemSelected = { onProgramSelected(it.program_id) }
        )

        // Error message
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // Submit button
        Button(
            onClick = {
                selectedPatientId?.let { patientId ->
                    selectedProgramId?.let { programId ->
                        onEnrollClick(patientId, programId)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedPatientId != null &&
                    selectedProgramId != null &&
                    !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Enroll Patient")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSelector(
    label: String,
    items: List<T>,
    selectedItem: T?,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    itemToString: (T) -> String,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(it) },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            value = selectedItem?.let { itemToString(it) } ?: "",
            onValueChange = {},
            label = { Text("$label*") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            isError = items.isEmpty()
        )

        if (items.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(itemToString(item)) },
                        onClick = {
                            onItemSelected(item)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}