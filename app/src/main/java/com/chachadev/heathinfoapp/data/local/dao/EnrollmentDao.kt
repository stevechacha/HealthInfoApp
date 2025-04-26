package com.chachadev.heathinfoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chachadev.heathinfoapp.data.local.entity.EnrollmentEntity

@Dao
interface EnrollmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(enrollment: EnrollmentEntity)

    @Query("SELECT program_id FROM enrollments WHERE patient_id = :patientId")
    suspend fun getEnrolledPrograms(patientId: String): List<String>
}