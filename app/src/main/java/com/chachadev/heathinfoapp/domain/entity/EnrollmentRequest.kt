package com.chachadev.heathinfoapp.domain.entity

data class EnrollmentRequest(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val programs: List<HealthProgram>
)

//data class Client(
//    val id: Int,
//    val name: String,
//    val age: Int,
//    val gender: String,
//    val programs: List<HealthProgram>
//)

