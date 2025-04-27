package com.chachadev.heathinfoapp.presentation.createProgram


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramType
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProgramScreen(
    viewModel: CreateProgramViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onProgramCreated: (ProgramResponse) -> Unit = {}
) {
    val formState by viewModel.formState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Handle successful creation
    LaunchedEffect(uiState) {
        if (uiState is CreateProgramViewModel.UiState.Success) {
            val program = (uiState as CreateProgramViewModel.UiState.Success).program
            onProgramCreated(program)
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create New Program") },
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
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Program Name
            OutlinedTextField(
                value = formState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Program Name*") },
                isError = formState.nameError != null,
                supportingText = { formState.nameError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Program Type Selection
            Text("Program Type*", style = MaterialTheme.typography.labelLarge)
            ProgramType.entries.forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (formState.type == type),
                            onClick = { viewModel.onTypeChange(type) }
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (formState.type == type),
                        onClick = { viewModel.onTypeChange(type) }
                    )
                    Text(
                        text = type.name.replace('_', ' '),
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            formState.typeError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Target Age Group
            OutlinedTextField(
                value = formState.targetAgeGroup,
                onValueChange = viewModel::onAgeGroupChange,
                label = { Text("Target Age Group (optional)") },
                isError = formState.ageGroupError != null,
                supportingText = { formState.ageGroupError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                placeholder = { Text("e.g., 18-65") }
            )

            // Risk Factors
            OutlinedTextField(
                value = formState.riskFactors,
                onValueChange = viewModel::onRiskFactorsChange,
                label = { Text("Risk Factors (comma separated)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                minLines = 3,
                placeholder = { Text("e.g., diabetes, hypertension, obesity") }
            )

            // Submit Button
            Button(
                onClick = viewModel::createProgram,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                enabled = formState.name.isNotBlank() && formState.type != null
            ) {
                Text("Create Program")
            }

            // Handle loading and error states
            when (uiState) {
                is CreateProgramViewModel.UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is CreateProgramViewModel.UiState.Error -> {
                    val error = (uiState as CreateProgramViewModel.UiState.Error).message
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
