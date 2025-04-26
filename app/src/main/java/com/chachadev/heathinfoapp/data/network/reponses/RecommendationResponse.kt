package com.chachadev.heathinfoapp.data.network.reponses

data class RecommendationResponse(
    val program_id: String,
    val program_name: String,
    val match_reasons: List<String>
)