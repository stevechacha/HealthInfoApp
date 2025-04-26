package com.chachadev.heathinfoapp.presentation.enrollClient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.domain.entity.Resource
import com.chachadev.heathinfoapp.presentation.clientProgram.CreateProgramViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnrollPatientScreen(
    viewModel: EnrollPatientViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onEnrollmentSuccess: (PatientResponse) -> Unit = {}
) {
    // Form state
    var selectedPatientId by remember { mutableStateOf<String?>(null) }
    var selectedProgramId by remember { mutableStateOf<String?>(null) }
    var expandedPatient by remember { mutableStateOf(false) }
    var expandedProgram by remember { mutableStateOf(false) }

    // Collect states
    val enrollState by viewModel.enrollState.collectAsState()
    val patientsState by viewModel.patients.collectAsState()
    val programsState by viewModel.programs.collectAsState()

    // Extract patient and program lists from Resource
    val patients = remember(patientsState) {
        (patientsState as? Resource.Success)?.data ?: emptyList()
    }
    val programs = remember(programsState) {
        (programsState as? Resource.Success)?.data ?: emptyList()
    }

    // Handle successful enrollment
    LaunchedEffect(enrollState) {
        if (enrollState is EnrollPatientViewModel.EnrollState.Success) {
            val patient = (enrollState as EnrollPatientViewModel.EnrollState.Success).patient
            onEnrollmentSuccess(patient)
            selectedPatientId = null
            selectedProgramId = null
            viewModel.resetState()
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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Show loading state for initial data load
            when {
                patientsState is Resource.Loading || programsState is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                patientsState is Resource.Error -> {
                    ErrorMessage(
                        message = (patientsState as Resource.Error).message ?: "Failed to load patients",
                        onRetry = { viewModel.loadPatientsAndPrograms() }
                    )
                }
                programsState is Resource.Error -> {
                    ErrorMessage(
                        message = (programsState as Resource.Error).message ?: "Failed to load programs",
                        onRetry = { viewModel.loadPatientsAndPrograms() }
                    )
                }
                else -> {
                    // Patient Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedPatient,
                        onExpandedChange = { expandedPatient = it }
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = patients.find { it.patient_id == selectedPatientId }?.full_name ?: "",
                            onValueChange = {},
                            label = { Text("Select Patient*") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPatient)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            isError = patients.isEmpty()
                        )

                        if (patients.isNotEmpty()) {
                            ExposedDropdownMenu(
                                expanded = expandedPatient,
                                onDismissRequest = { expandedPatient = false }
                            ) {
                                patients.forEach { patient ->
                                    DropdownMenuItem(
                                        text = { Text("${patient.full_name} (${patient.national_id})") },
                                        onClick = {
                                            selectedPatientId = patient.patient_id
                                            expandedPatient = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Program Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedProgram,
                        onExpandedChange = { expandedProgram = it }
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = programs.find { it.program_id == selectedProgramId }?.name ?: "",
                            onValueChange = {},
                            label = { Text("Select Program*") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProgram)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            isError = programs.isEmpty()
                        )

                        if (programs.isNotEmpty()) {
                            ExposedDropdownMenu(
                                expanded = expandedProgram,
                                onDismissRequest = { expandedProgram = false }
                            ) {
                                programs.forEach { program ->
                                    DropdownMenuItem(
                                        text = {
                                            Text("${program.name} (${program.program_type.name.lowercase()})")
                                        },
                                        onClick = {
                                            selectedProgramId = program.program_id
                                            expandedProgram = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Submit Button
                    Button(
                        onClick = {
                            selectedPatientId?.let { patientId ->
                                selectedProgramId?.let { programId ->
                                    viewModel.enrollPatient(patientId, programId)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedPatientId != null &&
                                selectedProgramId != null &&
                                enrollState !is EnrollPatientViewModel.EnrollState.Loading
                    ) {
                        Text("Enroll Patient")
                    }

                    // Handle enrollment states
                    when (enrollState) {
                        is EnrollPatientViewModel.EnrollState.Loading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        is EnrollPatientViewModel.EnrollState.Error -> {
                            val error = (enrollState as EnrollPatientViewModel.EnrollState.Error).message
                            Text(
                                text = "Error: $error",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        else -> {}
                    }
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