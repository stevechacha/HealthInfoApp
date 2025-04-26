package com.chachadev.heathinfoapp.data.network.reponses

//
//data class PatientResponse(
//    val patient_id: String,
//    val national_id: String,
//    val full_name: String,
//    val date_of_birth: String,
//    val blood_type: String?,
//    val enrolled_programs: List<String>,
//    val medical_history: List<String>,
//    val created_at: String
//)

data class PatientResponse(
    val patient_id: String,
    val national_id: String,
    val full_name: String,
    val date_of_birth: String,
    val blood_type: String?,
    val enrolled_programs: List<String>,
    val created_at: String
)

data class ClientResponse(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val programIds: List<Int>
)


