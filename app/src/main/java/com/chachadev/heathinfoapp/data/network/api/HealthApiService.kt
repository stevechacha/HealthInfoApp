package com.chachadev.heathinfoapp.data.network.api

import com.chachadev.heathinfoapp.data.mapper.EnrollRequest
import com.chachadev.heathinfoapp.data.network.reponses.ClientResponse
import com.chachadev.heathinfoapp.data.network.reponses.HealthProgramResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.domain.entity.ClientRequest
import com.chachadev.heathinfoapp.domain.entity.EnrollmentRequest
import com.chachadev.heathinfoapp.domain.entity.ProgramRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HealthApiService {
    // 1. Create Health Program
    @POST("programs/")
    suspend fun createProgram(@Body program: Program): Program

    // 2. Get All Programs
    @GET("programs/")
    suspend fun getPrograms(): List<Program>

    // 3. Register New Client
    @POST("clients/")
    suspend fun registerClient(@Body client: ClientRequest): ClientResponse

    // 4. Get All Clients
    @GET("clients/")
    suspend fun getAllClients(): List<ClientResponse>

    // 5. Search Clients by Name
    @GET("clients/")
    suspend fun searchClients(@Query("name") name: String): List<ClientResponse>

    // 6. Get Client Profile by ID
    @GET("clients/{id}")
    suspend fun getClient(@Path("id") id: Int): ClientResponse

    // 7. Enroll Client in a Program
    @POST("enroll/")
    suspend fun enrollClient(@Body request: EnrollRequest): ClientResponse
}

data class Program(val name: String)






