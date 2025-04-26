package com.chachadev.heathinfoapp.presentation.clientProgram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chachadev.heathinfoapp.data.local.entity.ClientEntity
import com.chachadev.heathinfoapp.data.local.entity.ClientWithPrograms
import com.chachadev.heathinfoapp.data.local.entity.HealthProgramEntity
import com.chachadev.heathinfoapp.data.repo.ClientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateProgramViewModel(private val repository: ClientRepository)  : ViewModel(){

    private val _programs = MutableStateFlow<List<HealthProgramEntity>>(emptyList())
    val programs: StateFlow<List<HealthProgramEntity>> = _programs
    val allClients = MutableStateFlow<List<ClientEntity>>(emptyList())


    val searchResults = MutableStateFlow<List<ClientEntity>>(emptyList())


    fun addProgram(program: HealthProgramEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPrograms(listOf(program))
        }
    }

    fun fetchPrograms() {
        viewModelScope.launch(Dispatchers.IO) {
            _programs.value = repository.getAllPrograms()
        }
    }

    fun enrollClient(clientId: Int, programIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.enrollClientInPrograms(clientId, programIds)
        }
    }

    fun searchClients(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchResults.value = repository.searchClients(query)
        }
    }

    fun loadAllClients() {
        viewModelScope.launch(Dispatchers.IO) {
            allClients.value = repository.getAllClients()
        }
    }


    fun getClientProfile(clientId: Int): StateFlow<ClientWithPrograms?> {
        val profile = MutableStateFlow<ClientWithPrograms?>(null)
        viewModelScope.launch(Dispatchers.IO) {
            profile.value = repository.getClientWithPrograms(clientId)
        }
        return profile
    }

    fun registerClient(name: String, age: Int, gender: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertClient(ClientEntity(name = name, age = age, gender = gender))
        }
    }


}