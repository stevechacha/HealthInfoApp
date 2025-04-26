package com.chachadev.heathinfoapp.domain.entity

data class ProgramRequest(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val programs: List<HealthProgram>
)
