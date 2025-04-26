package com.chachadev.heathinfoapp.data.mapper

import com.chachadev.heathinfoapp.data.local.entity.ClientEntity
import com.chachadev.heathinfoapp.data.local.entity.HealthProgramEntity
import com.chachadev.heathinfoapp.data.network.api.Program
import com.chachadev.heathinfoapp.data.network.reponses.ClientResponse
import com.chachadev.heathinfoapp.domain.entity.ClientRequest


// Mapper from local to API request
fun ClientEntity.toRequest(): ClientRequest =
    ClientRequest(
        name = this.name,
        age = this.age,
        id = TODO(),
        gender = TODO(),
        programs = TODO()
    )

// Mapper from API response to local
fun ClientResponse.toEntity(): ClientEntity =
    ClientEntity(
        id = this.id, 
        name = this.name, 
        age = this.age,
        gender = TODO()
    )

fun HealthProgramEntity.toRequest(): Program =
    Program(name = this.name)

fun Program.toEntity(): HealthProgramEntity =
    HealthProgramEntity(
        name = this.name,
        id = TODO(),
        description = TODO()
    )

fun createEnrollRequest(clientId: Int, programName: String): EnrollRequest =
    EnrollRequest(client_id = clientId, program_name = programName)