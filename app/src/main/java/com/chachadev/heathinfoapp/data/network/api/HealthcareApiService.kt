package com.chachadev.heathinfoapp.data.network.api

import com.chachadev.heathinfoapp.data.network.reponses.HealthCheckResponse
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.data.network.reponses.RecommendationResponse
import com.chachadev.heathinfoapp.data.network.utils.ApiResponse
import com.chachadev.heathinfoapp.domain.entity.EnrollmentRequest
import com.chachadev.heathinfoapp.domain.entity.PatientRequest
import com.chachadev.heathinfoapp.domain.entity.ProgramRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HealthcareApiService {
    // Patient Endpoints
    @GET("patients")
    suspend fun getAllPatients(
        @Header("X-API-Key") apiKey: String
    ): ApiResponse<List<PatientResponse>>

    @POST("patients")
    suspend fun registerPatient(
        @Header("X-API-Key") apiKey: String,
        @Body patient: PatientRequest
    ): ApiResponse<PatientResponse>

    @GET("patients/search")
    suspend fun searchPatients(
        @Header("X-API-Key") apiKey: String,
        @Query("name") name: String?,
        @Query("program_id") programId: String?
    ): ApiResponse<List<PatientResponse>>

    @GET("patients/{patient_id}")
    suspend fun getPatient(
        @Header("X-API-Key") apiKey: String,
        @Path("patient_id") patientId: String
    ): ApiResponse<PatientResponse>

    @GET("patients/{patient_id}/recommendations")
    suspend fun getRecommendations(
        @Header("X-API-Key") apiKey: String,
        @Path("patient_id") patientId: String
    ): ApiResponse<List<RecommendationResponse>>

    // Program Endpoints
    @GET("programs")
    suspend fun getAllPrograms(
        @Header("X-API-Key") apiKey: String
    ): ApiResponse<List<ProgramResponse>>

    @POST("programs")
    suspend fun createProgram(
        @Header("X-API-Key") apiKey: String,
        @Body program: ProgramRequest
    ): ApiResponse<ProgramResponse>

    // Enrollment Endpoints
    @POST("enroll")
    suspend fun enrollPatient(
        @Header("X-API-Key") apiKey: String,
        @Body request: EnrollmentRequest
    ): ApiResponse<PatientResponse>

    // System Endpoints
    @GET("health")
    suspend fun healthCheck(): ApiResponse<HealthCheckResponse>
}