package com.chachadev.heathinfoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chachadev.heathinfoapp.data.local.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {

    @Dao
    interface PatientDao {
        // Insert or update patient
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertPatient(patient: PatientEntity)

        // Single insert
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(patient: PatientEntity)

        // Get all patients
        @Query("SELECT * FROM patients ORDER BY full_name ASC")
        fun getAllPatients(): Flow<List<PatientEntity>>


        // Insert or update multiple patients
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertPatients(patients: List<PatientEntity>)

        // Get single patient by ID
        @Query("SELECT * FROM patients WHERE patient_id = :patientId")
        suspend fun getPatientById(patientId: String): PatientEntity?

        // Get all patients with reactive Flow
        @Query("""
        SELECT * FROM patients 
        ORDER BY 
            CASE WHEN :sortBy = 'name' THEN full_name END COLLATE NOCASE ASC,
            CASE WHEN :sortBy = 'date' THEN date_of_birth END DESC,
            patient_id ASC
    """)
        fun getAllPatients(sortBy: String = "name"): Flow<List<PatientEntity>>

        // Search patients by name with pagination
        @Query("""
        SELECT * FROM patients 
        WHERE full_name LIKE '%' || :query || '%'
        ORDER BY full_name COLLATE NOCASE ASC
        LIMIT :limit OFFSET :offset
    """)
        suspend fun searchPatients(
            query: String,
            limit: Int = 20,
            offset: Int = 0
        ): List<PatientEntity>

        // Get count of all patients
        @Query("SELECT COUNT(*) FROM patients")
        suspend fun getPatientCount(): Int

        // Delete single patient
        @Delete
        suspend fun deletePatient(patient: PatientEntity)

        // Delete all patients
        @Query("DELETE FROM patients")
        suspend fun clearAllPatients()

        // Mark patients as stale (for cache invalidation)
        @Query("UPDATE patients SET last_updated = 0 WHERE patient_id IN (:patientIds)")
        suspend fun markAsStale(patientIds: List<String>)
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(patient: PatientEntity)

    @Update
    suspend fun update(patient: PatientEntity)

    @Query("SELECT * FROM patients WHERE patient_id = :id")
    suspend fun getPatient(id: String): PatientEntity?

    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<PatientEntity>>

    @Query("DELETE FROM patients WHERE patient_id = :id")
    suspend fun delete(id: String)

//    @Query("SELECT * FROM patients ORDER BY full_name ASC")
//    fun getAllPatients(): Flow<List<PatientEntity>>  // Returns a Flow for reactive updates

    @Query("SELECT * FROM patients WHERE full_name LIKE '%' || :query || '%'")
    fun searchPatients(query: String): Flow<List<PatientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(patients: List<PatientEntity>)

//    @Query("SELECT * FROM patients ORDER BY full_name ASC")
//    fun getAllPatients(): Flow<List<PatientEntity>>  // Returns a Flow for reactive updates
//
//    @Query("SELECT * FROM patients WHERE full_name LIKE '%' || :query || '%'")
//    fun searchPatients(query: String): Flow<List<PatientEntity>>
}

