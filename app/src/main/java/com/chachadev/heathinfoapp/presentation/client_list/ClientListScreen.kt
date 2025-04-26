package com.chachadev.heathinfoapp.presentation.client_list

import android.media.tv.TvContract.Programs
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.chachadev.heathinfoapp.presentation.ClientViewModel
import com.chachadev.heathinfoapp.presentation.clientProgram.CreateProgramViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun ClientListScreen(
   viewModel: CreateProgramViewModel = koinViewModel()
) {

    var query by remember { mutableStateOf("") }
    val results by viewModel.searchResults.collectAsState()
    val allClients by viewModel.allClients.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllClients()
    }

    val displayList = if (query.isEmpty()) allClients else results

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.searchClients(it)
            },
            label = { Text("Search Client") }
        )
        LazyColumn {
            items(displayList) { client ->
                Text(
                    text = client.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(8.dp)
                )
            }
        }
    }
}


@Composable
fun ClientCard(client: Client, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { navController.navigate("client_profile/${client.id}") },
        elevation = CardDefaults.cardElevation()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = client.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Age: ${client.age}, Gender: ${client.gender}")
//            Text(text = "Programs: ${client.programs.joinToString { it.name }}")
        }
    }
}

data class Client(
    val id: String,
    val name: String,
    val age: String,
    val gender: String,
    val programs: Programs
)
