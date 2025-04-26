package com.chachadev.heathinfoapp.presentation.patientProfile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chachadev.heathinfoapp.presentation.clientProgram.CreateProgramViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ClientProfileScreen(
    clientId: Int,
    viewModel: CreateProgramViewModel = koinViewModel()
) {
//    val profile by viewModel.getClientProfile(clientId).collectAsState()
//
//    profile?.let {
//        Column(Modifier.padding(16.dp)) {
//            Text("Name: ${it.client.name}")
//            Text("Age: ${it.client.age}")
//            Text("Gender: ${it.client.gender}")
//            Spacer(Modifier.height(8.dp))
//            Text("Enrolled Programs:", style = MaterialTheme.typography.titleSmall)
//            it.programs.forEach { program ->
//                Text("- ${program.name}")
//            }
//        }
//    }
}
