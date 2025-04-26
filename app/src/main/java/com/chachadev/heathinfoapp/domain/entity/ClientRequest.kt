package com.chachadev.heathinfoapp.domain.entity

data class ClientRequest(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val programs: List<HealthProgram>
)

