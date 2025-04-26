package com.chachadev.heathinfoapp.data.repo

import com.chachadev.heathinfoapp.data.local.dao.ClientDao
import com.chachadev.heathinfoapp.data.local.dao.ProgramDao
import com.chachadev.heathinfoapp.data.local.entity.ClientEntity
import com.chachadev.heathinfoapp.data.local.entity.ClientProgramCrossRef
import com.chachadev.heathinfoapp.data.local.entity.ClientWithPrograms
import com.chachadev.heathinfoapp.data.local.entity.HealthProgramEntity
import com.chachadev.heathinfoapp.data.mapper.createEnrollRequest
import com.chachadev.heathinfoapp.data.mapper.toEntity
import com.chachadev.heathinfoapp.data.mapper.toRequest
import com.chachadev.heathinfoapp.data.network.api.HealthApiService

class ClientRepository(
    private val clientDao: ClientDao,
    private val programDao: ProgramDao,
    private val api: HealthApiService
) {

    suspend fun insertClient(client: ClientEntity): Long {
        return clientDao.insertClient(client)
    }

    suspend fun enrollClientInPrograms(clientId: Int, programIds: List<Int>) {
        val refs = programIds.map { ClientProgramCrossRef(clientId, it) }
        clientDao.enrollClientInPrograms(refs)
    }

    suspend fun getClientWithPrograms(id: Int): ClientWithPrograms? {
        return clientDao.getClientWithPrograms(id)
    }

    // Insert health programs into Room DB
    suspend fun insertPrograms(programs: List<HealthProgramEntity>) {
        programDao.insertPrograms(programs)
    }

    suspend fun getAllPrograms(): List<HealthProgramEntity> {
        return programDao.getAllPrograms()
    }

    suspend fun getAllClientsRemote(): List<ClientEntity> {
        val remoteClients = api.getAllClients()
        val entities = remoteClients.map { it.toEntity() }

        // Optionally cache
        clientDao.insertClients(entities)

        return entities
    }

    suspend fun getAllProgramsRemote(): List<HealthProgramEntity> {
        val remotePrograms = api.getPrograms()
        val entities = remotePrograms.map { it.toEntity() }

        // Optionally cache
        programDao.insertPrograms(entities)

        return entities
    }


//    suspend fun getClientById(clientId: Int): ClientEntity? {
//        return clientDao.getClientWithPrograms(clientId)
//    }
//
//    suspend fun insertProgram(program: HealthProgramEntity) {
//        programDao.insertPrograms(listOf(program))
//    }
//
//    // Insert clients into Room DB
//    suspend fun insertClients(clients: List<ClientEntity>) {
//        clientDao.insertClients(clients)
//    }



    // Get all clients from Room DB
    suspend fun getAllClients(): List<ClientEntity> {
        return clientDao.getAllClients()
    }

    suspend fun searchClients(query: String): List<ClientEntity> = clientDao.searchClients(query)


    suspend fun registerClientRemotelyThenLocally(client: ClientEntity): Result<Unit> {
        return try {
            val response = api.registerClient(client.toRequest())
            clientDao.insertClient(response.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertProgramRemotelyThenLocally(program: HealthProgramEntity): Result<Unit> {
        return try {
            val apiProgram = api.createProgram(program.toRequest())
            programDao.insertPrograms(listOf(apiProgram.toEntity()))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    suspend fun enrollClientRemotelyThenLocally(clientId: Int, programName: String): Result<Unit> {
//        return try {
//            val response = api.enrollClient(createEnrollRequest(clientId, programName))
//            val program = programDao.getProgramByName(programName)
//            program?.let {
//                clientDao.enrollClientInPrograms(
//                    listOf(ClientProgramCrossRef(clientId, it.id))
//                )
//            }
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
}
