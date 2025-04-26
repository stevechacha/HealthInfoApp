package com.chachadev.heathinfoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "programs")
data class HealthProgramEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String
)

