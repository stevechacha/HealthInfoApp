package com.chachadev.heathinfoapp.presentation.createProgram

import com.chachadev.heathinfoapp.data.network.reponses.ProgramType


// Program.kt (response model)
data class Program(
    val program_id: String,
    val name: String,
    val program_type: ProgramType,
    val target_age_group: String?,
    val risk_factors: List<String>,
    val created_at: String
)

data class ProgramFormState(
    val name: String = "",
    val type: ProgramType? = null,
    val targetAgeGroup: String = "",
    val riskFactors: String = "",
    val nameError: String? = null,
    val typeError: String? = null,
    val ageGroupError: String? = null
)