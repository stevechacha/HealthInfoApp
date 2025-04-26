package com.chachadev.heathinfoapp.domain.entity

//data class EnrollmentRequest(
//    val id: Int,
//    val name: String,
//    val age: Int,
//    val gender: String,
//    val programs: List<HealthProgram>
//)

// RequestModels.kt
data class EnrollmentRequest(
    val patient_id: String,
    val program_id: String
)
