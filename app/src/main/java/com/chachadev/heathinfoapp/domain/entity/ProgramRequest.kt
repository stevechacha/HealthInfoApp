package com.chachadev.heathinfoapp.domain.entity

import com.chachadev.heathinfoapp.data.network.reponses.ProgramType


data class ProgramRequest(
    val name: String,
    val program_type: ProgramType,
    val target_age_group: String?,
    val risk_factors: List<String>
)

