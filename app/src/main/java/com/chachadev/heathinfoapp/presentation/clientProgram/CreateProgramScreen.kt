package com.chachadev.heathinfoapp.presentation.clientProgram


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chachadev.heathinfoapp.data.local.entity.HealthProgramEntity
import com.chachadev.heathinfoapp.presentation.ClientViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.compat.ViewModelCompat.getViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun CreateProgramScreen(
    viewModel: CreateProgramViewModel = koinViewModel()
) {

    var programName by remember { mutableStateOf("") }
    var programDescription by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val programs by viewModel.programs.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPrograms()
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {

        Text("Create Health Program", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = programName,
            onValueChange = { programName = it },
            label = { Text("Program Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = programDescription,
            onValueChange = { programDescription = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (programName.isNotBlank()) {
                    val program = HealthProgramEntity(
                        id = programName.hashCode(),
                        name = programName,
                        description = programDescription
                    )
                    scope.launch(Dispatchers.IO) {
                        viewModel.addProgram(program)
                    }
                    programName = ""
                    programDescription = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }

        LazyColumn {
            items(programs) { program ->
                Text("${program.name} - ${program.description}")
            }
        }
    }
}

