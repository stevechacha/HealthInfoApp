package com.chachadev.heathinfoapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.chachadev.heathinfoapp.data.network.reponses.ProgramType

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey val patient_id: String,
    val national_id: String,
    val full_name: String,
    val date_of_birth: String,
    val blood_type: String?,
    val created_at: String,
    val last_updated: Long = System.currentTimeMillis()
)

