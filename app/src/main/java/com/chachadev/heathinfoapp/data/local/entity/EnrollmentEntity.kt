package com.chachadev.heathinfoapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "enrollments",
    primaryKeys = ["patient_id", "program_id"],
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["patient_id"],
            childColumns = ["patient_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProgramEntity::class,
            parentColumns = ["program_id"],
            childColumns = ["program_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EnrollmentEntity(
    val patient_id: String,
    val program_id: String,
    val enrolled_at: String = ""
)