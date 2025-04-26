package com.chachadev.heathinfoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ClientViewModel(application: Application) : AndroidViewModel(application) {



//    private val repository: ClientRepository = ClientRepository()
//
//    init {
//        fetchClients()
//    }
//
//    // Fetch clients from API and save them in Room
//    private fun fetchClients() {
//        viewModelScope.launch {
//            try {
//                val clientResponses = RetrofitInstance.apiService.getClients()
//
//                // Convert response to Client model and save to Room DB
//                val clientsToInsert = clientResponses.map {
//                    Client(
//                        id = it.id,
//                        name = it.name,
//                        age = it.age,
//                        gender = it.gender,
//                        programs = emptyList()  // We'll add programs after fetching them
//                    )
//                }
//
//                // Save to Room DB
//                repository.insertClients(clientsToInsert)
//                _clients.value = clientsToInsert
//
//                // Fetch health programs to link with clients
//                fetchHealthPrograms()
//            } catch (e: Exception) {
//                // Handle errors (e.g., network issues)
//            }
//        }
//    }
//
    // Fetch health programs from API
//    private fun fetchHealthPrograms() {
//        viewModelScope.launch {
//            try {
//                val programResponses = RetrofitInstance.apiService.getHealthPrograms()
//
//                // Save programs to Room DB
//                val programsToInsert = programResponses.map {
//                    HealthProgramEntity(
//                        id = it.id,
//                        name = it.name,
//                        description = it.description
//                    )
//                }
//
//                repository.insertPrograms(programsToInsert)
//            } catch (e: Exception) {
//                // Handle errors (e.g., network issues)
//            }
//        }
//    }
}
