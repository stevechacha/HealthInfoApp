package com.chachadev.heathinfoapp.presentation.clientRegistration

import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chachadev.heathinfoapp.presentation.clientProgram.CreateProgramViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ClientRegistrationScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateProgramViewModel = koinViewModel()
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
        OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") })

        Button(onClick = {
            if (name.isNotBlank() && age.isNotBlank() && gender.isNotBlank()) {
                viewModel.registerClient(name, age.toInt(), gender)
                name = ""; age = ""; gender = ""
            }
        }) {
            Text("Register Client")
        }
    }
}

