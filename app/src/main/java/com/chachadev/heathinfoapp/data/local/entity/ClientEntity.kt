package com.chachadev.heathinfoapp.data.local.entity

import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int,
    val gender: String
)

//@Entity(tableName = "client")
//data class ClientEntity(
//    @PrimaryKey val id: Int,
//    val name: String,
//    val age: Int,
//    val gender: String,
//    @Relation(parentColumn = "id", entityColumn = "clientId", associateBy = Junction(
//        ClientProgramCrossRefEntity::class)) val programs: List<HealthProgramEntity>
//)
