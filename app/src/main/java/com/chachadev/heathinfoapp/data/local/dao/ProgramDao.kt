package com.chachadev.heathinfoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chachadev.heathinfoapp.data.local.entity.ProgramEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(program: ProgramEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(programs: List<ProgramEntity>)

    @Query("SELECT * FROM programs WHERE program_id = :id")
    suspend fun getProgram(id: String): ProgramEntity?

    @Query("SELECT * FROM programs")
    fun getAllPrograms(): Flow<List<ProgramEntity>>
}