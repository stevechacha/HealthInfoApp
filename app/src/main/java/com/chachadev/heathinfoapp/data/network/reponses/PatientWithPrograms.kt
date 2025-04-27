package com.chachadev.heathinfoapp.data.network.reponses

data class PatientWithPrograms(
        val patient: PatientResponse,
        val enrolledPrograms: List<ProgramResponse>
    )