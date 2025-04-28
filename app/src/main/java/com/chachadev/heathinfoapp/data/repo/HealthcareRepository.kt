package com.chachadev.heathinfoapp.data.repo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.chachadev.heathinfoapp.data.local.HealthcareDatabase
import com.chachadev.heathinfoapp.data.local.entity.EnrollmentEntity
import com.chachadev.heathinfoapp.data.mapper.toDomainModel
import com.chachadev.heathinfoapp.data.mapper.toEntity
import com.chachadev.heathinfoapp.data.network.api.HealthcareApiService
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.network.reponses.PatientWithPrograms
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.data.network.reponses.RecommendationResponse
import com.chachadev.heathinfoapp.domain.entity.EnrollmentRequest
import com.chachadev.heathinfoapp.domain.entity.PatientRequest
import com.chachadev.heathinfoapp.domain.entity.ProgramRequest
import com.chachadev.heathinfoapp.domain.entity.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class HealthcareRepository(
    private val apiService: HealthcareApiService,
    private val db: HealthcareDatabase,
    private val apiKey: String,
    private val context: Context
) {
    private val patientDao = db.patientDao()
    private val programDao = db.programDao()
    private val enrollmentDao = db.enrollmentDao()

    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    // Region: Patient Operations
    fun getPatients(refresh: Boolean = false): Flow<Resource<List<PatientResponse>>> {
        return flow {
            // Always emit cached data first
            emit(Resource.Loading())
            patientDao.getAllPatients().collect { cachedPatients ->
                emit(Resource.Success(cachedPatients.map { it.toDomainModel() }))
            }

            // Fetch from network if needed
            if (refresh && isNetworkAvailable()) {
                try {
                    emit(Resource.Loading())
                    val response = apiService.getAllPatients(apiKey)
                    if (response.status == "success") {
                        response.data?.let { patients ->
                            // Update local database
                            patientDao.insertAll(patients.map { it.toEntity() })
                            emit(Resource.Success(patients))
                        } ?: emit(Resource.Error(message = "No data received"))
                    } else {
                        emit(Resource.Error(message = response.message ?: "Unknown error"))
                    }
                } catch (e: Exception) {
                    emit(Resource.Error(message = e.message ?: "Network error"))
                }
            }
        }.catch { e ->
            emit(Resource.Error(message = e.message ?: "Unknown error"))
        }
    }

    suspend fun getPatient(patientId: String, refresh: Boolean = false): Resource<PatientResponse> {
        // Return cached data if not forcing refresh
        if (!refresh) {
            patientDao.getPatient(patientId)?.let {
                return Resource.Success(it.toDomainModel())
            }
        }

        // Try network if available
        return if (isNetworkAvailable()) {
            try {
                val response = apiService.getPatient(apiKey, patientId)
                if (response.status == "success") {
                    response.data?.let { patient ->
                        // Update cache
                        patientDao.insert(patient.toEntity())
                        Resource.Success(patient)
                    } ?: Resource.Error(message = "Empty response data")
                } else {
                    Resource.Error(message = response.message ?: "API error")
                }
            } catch (e: Exception) {
                Resource.Error(message = e.message ?: "Network error")
            }
        } else {
            Resource.Error(message = "No network connection")
        }
    }

    suspend fun registerPatient(patient: PatientRequest): Flow<Resource<PatientResponse>> {
        return flow<Resource<PatientResponse>> {
            val response = apiService.registerPatient(apiKey, patient)
            if (response.status == "success") {
                response.data?.let { registeredPatient ->
                    // Cache the new patient
                    patientDao.insert(registeredPatient.toEntity())
                    Resource.Success(registeredPatient)
                } ?: emit(Resource.Error(message = "Registration failed"))
            } else {
               emit(Resource.Error(message = response.message ?: "Registration error"))
            }
        }.catch { e->
            emit(Resource.Error(message = e.message ?: "Registration failed"))
        }
    }

    // Region: Program Operations
    fun getPrograms(refresh: Boolean = false): Flow<Resource<List<ProgramResponse>>> {
        return flow {
            // Emit cached programs first
            emit(Resource.Loading())
            programDao.getAllPrograms().collect { cachedPrograms ->
                emit(Resource.Success(cachedPrograms.map { it.toDomainModel() }))
            }

            // Fetch from network if needed
            if (refresh && isNetworkAvailable()) {
                try {
                    emit(Resource.Loading())
                    val response = apiService.getAllPrograms(apiKey)
                    if (response.status == "success") {
                        response.data?.let { programs ->
                            // Update local database
                            programDao.insertAll(programs.map { it.toEntity() })
                            emit(Resource.Success(programs))
                        } ?: emit(Resource.Error(message = "No data received"))
                    } else {
                        emit(Resource.Error(message = response.message ?: "API error"))
                    }
                } catch (e: Exception) {
                    emit(Resource.Error(message = e.message ?: "Network error"))
                }
            }
        }.catch { e ->
            emit(Resource.Error(message = e.message ?: "Unknown error"))
        }
    }

    suspend fun createProgram(program: ProgramRequest): Resource<ProgramResponse> {
        if (!isNetworkAvailable()) {
            return Resource.Error(message = "No internet connection")
        }

        return try {
            val response = apiService.createProgram(apiKey, program)
            if (response.status == "success") {
                response.data?.let { createdProgram ->
                    // Cache the new program
                    programDao.insert(createdProgram.toEntity())
                    Resource.Success(createdProgram)
                } ?: Resource.Error(message = "Program creation failed")
            } else {
                Resource.Error(message = response.message ?: "Creation error")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Creation failed")
        }
    }

    // Region: Enrollment Operations
    suspend fun getEnrolledPrograms(patientId: String): Resource<List<ProgramResponse>> {
        return try {
            // First get the program IDs from enrollments
            val programIds = enrollmentDao.getEnrolledPrograms(patientId)

            // Then get the full program details
            val programs = programIds.mapNotNull { programId ->
                programDao.getProgram(programId)?.toDomainModel()
            }

            Resource.Success(programs)
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Failed to load programs")
        }
    }

    suspend fun enrollPatient(patientId: String, programId: String): Flow<Resource<PatientResponse>> {
        return flow<Resource<PatientResponse>> {
            val response = apiService.enrollPatient(
                apiKey,
                EnrollmentRequest(patientId, programId)
            )

            if (response.status == "success") {
                // Update local enrollment
                enrollmentDao.insert(EnrollmentEntity(patientId, programId))
                Resource.Success(response.data!!)
            } else {
                Resource.Error(message = response.message ?: "Enrollment failed")
            }
        } .catch { e ->
            emit(Resource.Error(message = e.message ?: "Unknown error"))
        }
    }

    // Region: Recommendations
    suspend fun getPatientRecommendations(patientId: String): Resource<List<RecommendationResponse>> {
        if (!isNetworkAvailable()) {
            return Resource.Error(message = "No internet connection")
        }

        return try {
            val response = apiService.getRecommendations(apiKey, patientId)
            if (response.status == "success") {
                Resource.Success(response.data!!)
            } else {
                Resource.Error(message = response.message ?: "Recommendation error")
            }
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Failed to get recommendations")
        }
    }

    suspend fun getPatientWithPrograms(patientId: String): Resource<PatientWithPrograms> {
        return try {
            val patient = patientDao.getPatient(patientId)?.toDomainModel()
                ?: return Resource.Error(message = "Patient not found")

            val programIds = enrollmentDao.getEnrolledPrograms(patientId)
            val programs = programIds.mapNotNull { programDao.getProgram(it)?.toDomainModel() }

            Resource.Success(PatientWithPrograms(patient, programs))
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Failed to load patient data")
        }
    }



    // Region: System Health
    suspend fun checkHealth(): Resource<Boolean> {
        return try {
            val response = apiService.healthCheck()
            Resource.Success(response.status == "success")
        } catch (e: Exception) {
            Resource.Error(message = e.message ?: "Health check failed", data = false)
        }
    }

    suspend fun searchPatient(name: String?, programId: String?): Flow<Resource<List<PatientResponse>>>{
       return flow<Resource<List<PatientResponse>>> {
            val searchQueryResponse = apiService.searchPatients(apiKey,name,programId)
            if (searchQueryResponse.status =="success"){
                searchQueryResponse.data?.let { patientResponses ->
                    // Cache the new program
                    emit(Resource.Success(patientResponses))
                } ?: emit(Resource.Error(message = "Patient creation failed"))
            } else {
                emit(Resource.Error(message = searchQueryResponse.message ?: "Search Patient error"))
            }

       }.catch { e->
           emit(Resource.Error(message = e.message ?: "Failed to load patient data"))

        }
    }

    suspend fun syncPrograms() {
        if (!isNetworkAvailable()) return

        try {
            val response = apiService.getAllPrograms(apiKey)
            if (response.status == "success") {
                response.data?.let { programs ->
                    // Use the new insertAll function
                    programDao.insertAll(programs.map { it.toEntity() })
                }
            }
        } catch (e: Exception) {
            // Handle error
        }
    }
}