package com.chachadev.heathinfoapp.presentation.patient

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.domain.entity.PatientRequest
import com.chachadev.heathinfoapp.domain.entity.Resource
import com.chachadev.heathinfoapp.presentation.common.composables.CenterLoadingIndicator
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import retrofit2.HttpException
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientRegistrationScreen(
    viewModel: PatientRegistrationViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.patientUiState.collectAsState()
    var showSuccessDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    // Error states
    var dateError by remember { mutableStateOf<String?>(null) }
    var bloodTypeError by remember { mutableStateOf<String?>(null) }
    val apiErrors = remember { mutableStateListOf<String>() }

    // Blood type options
    val bloodTypeOptions = listOf(
        "A+", "A-", "B+", "B-",
        "AB+", "AB-", "O+", "O-"
    )

    // Handle API responses
    LaunchedEffect(uiState.data) {
        uiState.data?.let {
            showSuccessDialog = true
            println("Print$it")
            // Clear form through ViewModel
            viewModel.clearForm()
        }
    }

    // Success Dialog
    if (showSuccessDialog && uiState.data != null) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Registration Successful") },
            text = {
                Column {
                    Text("Patient ID: ${uiState.data!!.patient_id}")
                    Text("Name: ${uiState.data!!.full_name}")
                    Text("National ID: ${uiState.data!!.national_id}")
                    Text("Date of Birth: ${uiState.data!!.date_of_birth}")
                    Text("Blood Type: ${uiState.data!!.blood_type}")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onBackClick()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Register New Patient") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            AppOutlinedTextField(
                value = uiState.nationalId,
                onValueChange = viewModel::updateNationId,
                label = "National ID",
                isError = uiState.nationalId.length !in 10..15,
                supportingText = {
                    if (uiState.nationalId.length !in 10..15) {
                        Text("Must be 10-15 characters")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            AppOutlinedTextField(
                value = uiState.fullName,
                onValueChange = viewModel::updateFullName,
                label = "Full Name",
                isError = uiState.fullName.length < 3,
                supportingText = {
                    if (uiState.fullName.length < 3) {
                        Text("Must be at least 3 characters")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            AppOutlinedTextField(
                value = uiState.dateOfBirth,
                onValueChange = viewModel::updateDateOfBirth,
                label = "Date of Birth (YYYY-MM-DD)",
                isError = dateError != null,
                supportingText = { dateError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // Blood Type Dropdown
            ExposedDropdownMenuBox(
                expanded = uiState.expanded,
                onExpandedChange = viewModel::updateExpand
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = uiState.bloodType,
                    onValueChange = {},
                    label = { Text("Blood Type") },
                    isError = bloodTypeError != null,
                    supportingText = { bloodTypeError?.let { Text(it) } },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = uiState.expanded,
                    onDismissRequest = { viewModel.updateExpand(false) }
                ) {
                    bloodTypeOptions.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                viewModel.updateBloodType(type)
                                bloodTypeError = if (type.matches(Regex("^(A|B|AB|O)[+-]$"))) {
                                    null
                                } else {
                                    "Invalid blood type format"
                                }
                                viewModel.updateExpand(false)
                            }
                        )
                    }
                }
            }

            // API Errors
            if (uiState.error.isNotEmpty()) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            AppButton(
                text = "Register Patient",
                onClick = {
                    // Clear previous errors
                    dateError = null
                    bloodTypeError = null
                    viewModel.updateError("")

                    // Validate before submission
                    var isValid = true

                    if (!isValidDate(uiState.dateOfBirth)) {
                        dateError = "Invalid date format (YYYY-MM-DD)"
                        isValid = false
                    }

                    if (!uiState.bloodType.matches(Regex("^(A|B|AB|O)[+-]$"))) {
                        bloodTypeError = "Must be in format A+, B-, etc."
                        isValid = false
                    }

                    if (isValid) {
                        viewModel.registerPatient(
                            PatientRequest(
                                national_id = uiState.nationalId,
                                full_name = uiState.fullName,
                                date_of_birth = uiState.dateOfBirth,
                                blood_type = uiState.bloodType
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = uiState.nationalId.isNotEmpty() &&
                        uiState.fullName.isNotEmpty() &&
                        uiState.dateOfBirth.isNotEmpty() &&
                        uiState.bloodType.isNotEmpty()
            )




        }
    }
}



// Date validation helper
@RequiresApi(Build.VERSION_CODES.O)
private fun isValidDate(date: String): Boolean {
    return try {
        val parts = date.split("-")
        if (parts.size != 3) return false
        LocalDate.of(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
        true
    } catch (e: Exception) {
        false
    }
}


@Composable
fun PatientInfo(patient: PatientResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Patient Registered Successfully!", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("ID: ${patient.patient_id}")
            Text("Name: ${patient.full_name}")
            Text("DOB: ${patient.date_of_birth}")
            patient.blood_type?.let { Text("Blood Type: $it") }
        }
    }
}


@Composable
fun AppOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = 1,
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        supportingText = supportingText,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        maxLines = maxLines
    )

}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Text(text)
    }
}

