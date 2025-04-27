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
    // Form states
    var nationalId by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Error states
    var dateError by remember { mutableStateOf<String?>(null) }
    var bloodTypeError by remember { mutableStateOf<String?>(null) }

    // ViewModel states
    val patientState by viewModel.patientState.collectAsState()
    val apiErrors = remember { mutableStateListOf<String>() }

    // Blood type options
    val bloodTypeOptions = listOf(
        "A+", "A-", "B+", "B-",
        "AB+", "AB-", "O+", "O-"
    )

    // Handle API error responses
    LaunchedEffect(patientState) {
        if (patientState is Resource.Error) {
            val error = (patientState as Resource.Error)
            if (error is HttpException) {
                try {
                    val errorJson = error.response()?.errorBody()?.string()
                    val errors = Json.decodeFromString<List<Map<String, Any>>>(errorJson ?: "")
                    apiErrors.clear()
                    errors.forEach { err ->
                        when (err["loc"]?.toString()) {
                            "('body', 'date_of_birth')" -> {
                                dateError = err["msg"].toString()
                            }
                            "('body', 'blood_type')" -> {
                                bloodTypeError = err["msg"].toString()
                            }
                            else -> apiErrors.add(err["msg"].toString())
                        }
                    }
                } catch (e: Exception) {
                    apiErrors.add("Failed to parse error response")
                }
            }
        }
    }

    Scaffold (
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
    ){  paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // National ID
            OutlinedTextField(
                value = nationalId,
                onValueChange = { nationalId = it },
                label = { Text("National ID") },
                isError = nationalId.length !in 10..15,
                supportingText = {
                    if (nationalId.length !in 10..15) {
                        Text("Must be 10-15 characters")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // Full Name
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                isError = fullName.length < 3,
                supportingText = {
                    if (fullName.length < 3) {
                        Text("Must be at least 3 characters")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Date of Birth
            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = {
                    dateOfBirth = it
                },
                label = { Text("Date of Birth") },
                isError = dateError != null,
                supportingText = { dateError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true
            )

            // Blood Type Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = bloodType,
                    onValueChange = {},
                    label = { Text("Blood Type") },
                    isError = bloodTypeError != null,
                    supportingText = { bloodTypeError?.let { Text(it) } },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    bloodTypeOptions.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                bloodType = type
                                bloodTypeError = if (type.matches(Regex("^(A|B|AB|O)[+-]$"))) {
                                    null
                                } else {
                                    "Invalid blood type format"
                                }
                                expanded = false
                            }
                        )
                    }
                }
            }

            // API Errors
            apiErrors.forEach { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // Submit Button
            Button(
                onClick = {
                    // Clear previous errors
                    dateError = null
                    bloodTypeError = null
                    apiErrors.clear()

                    // Validate before submission
                    var isValid = true

                    if (!isValidDate(dateOfBirth)) {
                        dateError = "Invalid date format (YYYY-MM-DD)"
                        isValid = false
                    }

                    if (!bloodType.matches(Regex("^(A|B|AB|O)[+-]$"))) {
                        bloodTypeError = "Must be in format A+, B-, etc."
                        isValid = false
                    }

                    if (isValid) {
                        viewModel.registerPatient(
                            PatientRequest(
                                national_id = nationalId,
                                full_name = fullName,
                                date_of_birth = dateOfBirth,
                                blood_type = bloodType
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = nationalId.isNotEmpty() &&
                        fullName.isNotEmpty() &&
                        dateOfBirth.isNotEmpty() &&
                        bloodType.isNotEmpty()
            ) {
                Text("Register Patient")
            }

            // Loading and Success States
            when (patientState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is Resource.Success -> {
                    (patientState as Resource.Success<PatientResponse>).data?.let { PatientInfo(patient = it) }
                    LaunchedEffect(Unit) {
                        // Reset form after 2 seconds
                        delay(2000)
                        nationalId = ""
                        fullName = ""
                        dateOfBirth = ""
                        bloodType = ""
                        apiErrors.clear()
                    }
                }
                else -> Unit
            }
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

// Date transformation for input formatting
private class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 3 || i == 5) out += "-"
        }

        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 5) return offset + 1
                if (offset <= 7) return offset + 2
                return 10
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 7) return offset - 1
                return 7
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
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
private fun PatientDetails(patient: PatientResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = patient.full_name,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("ID: ${patient.patient_id}")
            Text("National ID: ${patient.national_id}")
            Text("DOB: ${patient.date_of_birth}")
            patient.blood_type?.let { 
                Text("Blood Type: $it") 
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (patient.enrolled_programs.isNotEmpty()) {
                Text(
                    text = "Enrolled Programs:",
                    style = MaterialTheme.typography.titleMedium
                )
                patient.enrolled_programs.forEach { programId ->
                    Text("- $programId")
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error: $message",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
