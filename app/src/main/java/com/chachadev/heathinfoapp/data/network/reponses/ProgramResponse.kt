package com.chachadev.heathinfoapp.data.network.reponses

data class ProgramResponse(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val programs: List<HealthProgramResponse>
)
