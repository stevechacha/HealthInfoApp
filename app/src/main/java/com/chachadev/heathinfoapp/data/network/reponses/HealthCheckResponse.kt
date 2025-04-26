package com.chachadev.heathinfoapp.data.network.reponses


data class HealthCheckResponse(
    val status: String,
    val patients: Int,
    val programs: Int
)