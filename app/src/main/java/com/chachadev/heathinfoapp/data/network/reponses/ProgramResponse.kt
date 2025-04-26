package com.chachadev.heathinfoapp.data.network.reponses



data class ProgramResponse(
    val program_id: String,
    val name: String,
    val program_type: ProgramType,
    val target_age_group: String?,
    val risk_factors: List<String>,
    val created_at: String
)


enum class ProgramType {
    chronic, infectious, preventive, rehabilitation
}

//enum class ProgramType {
//    CHRONIC,
//    INFECTIOUS,
//    PREVENTIVE,
//    REHABILITATION
//}
