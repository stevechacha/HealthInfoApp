package com.chachadev.heathinfoapp.presentation.patientList

import android.media.tv.TvContract.Programs
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsListScreen(
    viewModel: PatientsListViewModel = koinViewModel(),
    onPatientClick: (String) -> Unit
) {
    val patientsState by viewModel.patientsState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val refreshState = rememberPullToRefreshState()

    // Filter patients based on search query
    val filteredPatients = remember(patientsState.data, searchQuery) {
        when (patientsState) {
            is Resource.Success -> {
                patientsState.data?.filter { patient ->
                    patient.full_name.contains(searchQuery, ignoreCase = true) ||
                            patient.national_id.contains(searchQuery, ignoreCase = true) ||
                            patient.patient_id.contains(searchQuery, ignoreCase = true)
                } ?: emptyList()
            }
            else -> emptyList()
        }
    }

    Scaffold(
        topBar = {
            SearchAppBar(
                query = searchQuery,
                onQueryChange = viewModel::updateSearchQuery,
                onSearch = { viewModel.updateSearchQuery(it) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            PullToRefreshBox(
                state = refreshState,
                modifier = Modifier.fillMaxSize(),
                isRefreshing = true,
                indicator = {},
                contentAlignment = Alignment.TopStart,
                onRefresh = {},
            ) {
                when (patientsState) {
                    is Resource.Loading -> {
                        if (filteredPatients.isEmpty()) {
                            CenterLoadingIndicator()
                        }
                    }
                    is Resource.Error -> {
                        ErrorState(
                            message = (patientsState as Resource.Error).message
                                ?: "Error loading patients",
                            onRetry = viewModel::refresh
                        )
                    }
                    is Resource.Success -> {
                        if (filteredPatients.isEmpty()) {
                            EmptySearchState(
                                searchQuery = searchQuery,
                                onClearSearch = { viewModel.updateSearchQuery("") }
                            )
                        } else {
                            LazyColumn {
                                items(
                                    items = filteredPatients,
                                    key = { it.patient_id }
                                ) { patient ->
                                    PatientListItem(
                                        patient = patient,
                                        onClick = { onPatientClick(patient.patient_id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    var searchActive by remember { mutableStateOf(false) }

    if (searchActive) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = { onSearch(it) },
            active = true,
            onActiveChange = { searchActive = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Search suggestions could go here
        }
    } else {
        TopAppBar(
            title = { Text("Patients") },
            actions = {
                IconButton(onClick = { searchActive = true }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        )
    }
}

@Composable
private fun EmptySearchState(
    searchQuery: String,
    onClearSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = "No results",
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No patients found for \"$searchQuery\"",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onClearSearch) {
            Text("Clear search")
        }
    }
}

@Composable
private fun PatientListItem(
    patient: PatientResponse,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = patient.full_name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ID: ${patient.national_id}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "DOB: ${patient.date_of_birth}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}



@Composable
private fun CenterLoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
