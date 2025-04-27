package com.chachadev.heathinfoapp.data.local.utils

import androidx.room.TypeConverter
import com.chachadev.heathinfoapp.data.network.reponses.ProgramType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromProgramType(value: ProgramType): String = value.name

    @TypeConverter
    fun toProgramType(value: String): ProgramType = enumValueOf(value)


    @TypeConverter
    fun toStringList(json: String): List<String> =
        Json.decodeFromString(json)
}