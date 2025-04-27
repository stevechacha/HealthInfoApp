package com.chachadev.heathinfoapp.presentation.programsList


import android.media.tv.TvContract.Programs
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramType
import com.chachadev.heathinfoapp.domain.entity.Resource
import com.chachadev.heathinfoapp.presentation.patientList.ErrorState
import com.google.android.material.chip.Chip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramsListScreen(
    viewModel: ProgramsListViewModel = koinViewModel(),
    onProgramClick: (String) -> Unit = {},
    onCreateNewClick: () -> Unit = {}
) {
    val programsState by viewModel.programs.collectAsState()
    val scrollState = rememberLazyListState()
    var showFilters by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf<ProgramType?>(null) }
    val rememberSwipeRefreshState = rememberPullToRefreshState()

    // Pull-to-refresh state
    LaunchedEffect(selectedFilter) {
        viewModel.filterPrograms(selectedFilter)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Programs") },
                actions = {
                    IconButton(onClick = { showFilters = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNewClick,
                modifier = Modifier.padding(bottom = 56.dp) // Account for bottom nav
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Program")
            }
        }
    ) { padding ->
        PullToRefreshBox(
            state = rememberSwipeRefreshState,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.padding(padding),
            isRefreshing = true ,
            contentAlignment = Alignment.Center,
            indicator = {},
        ) {
            when (programsState) {
                is Resource.Loading -> {
                    if ((programsState as Resource.Loading).data.isNullOrEmpty()) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Error -> {
                    ErrorState(
                        message = (programsState as Resource.Error).message ?: "Error loading programs",
                        onRetry = { viewModel.refresh() }
                    )
                }
                is Resource.Success -> {
                    val programs = (programsState as Resource.Success).data ?: emptyList()
                    
                    if (programs.isEmpty()) {
                        EmptyState(
                            onRefresh = { viewModel.refresh() },
                            onCreateNew = onCreateNewClick
                        )
                    } else {
                        LazyColumn(state = scrollState) {
                            items(programs, key = { it.program_id }) { program ->
                                ProgramListItem(
                                    program = program,
                                    onClick = { onProgramClick(program.program_id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Filter dialog
    if (showFilters) {
        AlertDialog(
            onDismissRequest = { showFilters = false },
            title = { Text("Filter Programs") },
            text = {
                Column {
                    ProgramType.entries.forEach { type ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedFilter = type.takeIf { it != selectedFilter } }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedFilter == type,
                                onClick = { selectedFilter = type.takeIf { it != selectedFilter } }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(type.name.replace('_', ' '), style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilters = false }) {
                    Text("Apply")
                }
            }
        )
    }
}

@Composable
private fun ProgramListItem(
    program: ProgramResponse,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = program.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                ElevatedAssistChip(
                    label = { Text(program.program_type.name.replace('_', ' ')) },
                    onClick = {},
                    enabled = false,
                    leadingIcon = {  },
                    trailingIcon = {},
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            program.target_age_group?.let { ageGroup ->
                ProgramDetailRow("Age Group", ageGroup)
            }
            
            ProgramDetailRow("Risk Factors", program.risk_factors.joinToString(", "))
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Created ${program.created_at}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun ProgramDetailRow(label: String, value: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun EmptyState(
    onRefresh: () -> Unit,
    onCreateNew: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "No programs",
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No programs available",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Create a new program or refresh to load data",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onCreateNew) {
            Text("Create Program")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onRefresh) {
            Text("Refresh")
        }
    }
}