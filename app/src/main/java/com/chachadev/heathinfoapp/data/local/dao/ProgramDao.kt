package com.chachadev.heathinfoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chachadev.heathinfoapp.data.local.entity.HealthProgramEntity

@Dao
interface ProgramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(programs: List<HealthProgramEntity>)

    @Query("SELECT * FROM programs")
    suspend fun getAllPrograms(): List<HealthProgramEntity>
}
