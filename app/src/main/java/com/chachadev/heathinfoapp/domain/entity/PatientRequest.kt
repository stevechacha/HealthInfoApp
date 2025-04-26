package com.chachadev.heathinfoapp.domain.entity


data class ClientRequest(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val programs: List<HealthProgram>
)


data class PatientRequest(
    val national_id: String,
    val full_name: String,
    val date_of_birth: String,
    val blood_type: String?
)



data class HealthProgram(
    val id: Int,
    val name: String,
    val description: String
)

