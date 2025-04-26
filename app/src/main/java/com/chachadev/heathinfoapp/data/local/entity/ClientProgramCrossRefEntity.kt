package com.chachadev.heathinfoapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation


@Entity(primaryKeys = ["clientId", "programId"])
data class ClientProgramCrossRef(
    val clientId: Int,
    val programId: Int
)

data class ClientWithPrograms(
    @Embedded val client: ClientEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ClientProgramCrossRef::class,
            parentColumn = "clientId",
            entityColumn = "programId"
        )
    )
    val programs: List<HealthProgramEntity>
)
