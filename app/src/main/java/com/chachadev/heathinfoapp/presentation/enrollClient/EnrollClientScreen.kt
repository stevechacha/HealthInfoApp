package com.chachadev.heathinfoapp.presentation.enrollClient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chachadev.heathinfoapp.presentation.clientProgram.CreateProgramViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EnrollClientScreen(clientId: Int, viewModel: CreateProgramViewModel = koinViewModel()) {
    val programs by viewModel.programs.collectAsState()
    val selected = remember { mutableStateListOf<Int>() }


    LaunchedEffect(Unit) {
        viewModel.fetchPrograms()
    }

    Column(Modifier.padding(16.dp)) {
        Text("Enroll in Programs", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        programs.forEach { program ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selected.contains(program.id),
                    onCheckedChange = {
                        if (it) selected.add(program.id) else selected.remove(program.id)
                    }
                )
                Text(program.name)
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            viewModel.enrollClient(clientId, selected.toList())
        }) {
            Text("Enroll")
        }
    }
}