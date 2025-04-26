package com.chachadev.heathinfoapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chachadev.heathinfoapp.data.network.reponses.ProgramType

@Entity(tableName = "programs")
data class ProgramEntity(
    @PrimaryKey val program_id: String,
    val name: String,
    @ColumnInfo(name = "program_type") val type: ProgramType,
    val target_age_group: String?,
    val risk_factors: String, // Stored as JSON string
    val created_at: String,
    val last_updated: Long = System.currentTimeMillis()
)

