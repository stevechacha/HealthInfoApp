package com.chachadev.heathinfoapp.data.mapper

import com.chachadev.heathinfoapp.data.local.entity.ClientEntity
import com.chachadev.heathinfoapp.data.local.entity.EnrollmentEntity
import com.chachadev.heathinfoapp.data.local.entity.PatientEntity
import com.chachadev.heathinfoapp.data.local.entity.ProgramEntity
import com.chachadev.heathinfoapp.data.network.reponses.ClientResponse
import com.chachadev.heathinfoapp.data.network.reponses.PatientResponse
import com.chachadev.heathinfoapp.data.network.reponses.ProgramResponse
import com.chachadev.heathinfoapp.domain.entity.ClientRequest
import com.chachadev.heathinfoapp.domain.entity.PatientRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Patient Mappings
fun PatientEntity.toDomainModel(): PatientResponse {
    return PatientResponse(
        patient_id = this.patient_id,
        national_id = this.national_id,
        full_name = this.full_name,
        date_of_birth = this.date_of_birth,
        blood_type = this.blood_type,
        enrolled_programs = emptyList(), // This will be populated separately from enrollments
        created_at = this.created_at
    )
}

fun PatientResponse.toEntity(): PatientEntity {
    return PatientEntity(
        patient_id = this.patient_id,
        national_id = this.national_id,
        full_name = this.full_name,
        date_of_birth = this.date_of_birth,
        blood_type = this.blood_type,
        created_at = this.created_at,
        last_updated = System.currentTimeMillis()
    )
}

// Program Mappings
fun ProgramEntity.toDomainModel(): ProgramResponse {
    return ProgramResponse(
        program_id = this.program_id,
        name = this.name,
        program_type = this.type,
        target_age_group = this.target_age_group,
        risk_factors = this.risk_factors.fromJsonToList(), // Convert JSON string to List<String>
        created_at = this.created_at
    )
}

fun ProgramResponse.toEntity(): ProgramEntity {
    return ProgramEntity(
        program_id = this.program_id,
        name = this.name,
        type = this.program_type,
        target_age_group = this.target_age_group,
        risk_factors = this.risk_factors.toJsonString(), // Convert List<String> to JSON string
        created_at = this.created_at,
        last_updated = System.currentTimeMillis()
    )
}

// JSON Conversion Extensions
private fun String.fromJsonToList(): List<String> {
    return try {
        Gson().fromJson(this, object : TypeToken<List<String>>() {}.type)
    } catch (e: Exception) {
        emptyList()
    }
}

private fun List<String>.toJsonString(): String {
    return Gson().toJson(this)
}



// Enrollment Mappings (if needed)
fun EnrollmentEntity.toDomainModel(): EnrollmentResponse {
    return EnrollmentResponse(
        patient_id = this.patient_id,
        program_id = this.program_id
    )
}

fun EnrollmentResponse.toEntity(): EnrollmentEntity {
    return EnrollmentEntity(
        patient_id = this.patient_id,
        program_id = this.program_id
    )
}
data class EnrollmentResponse(
    val patient_id: String,
    val program_id: String
)

data class PatientWithPrograms(
    val patient: PatientResponse,
    val enrolledPrograms: List<ProgramResponse>
)