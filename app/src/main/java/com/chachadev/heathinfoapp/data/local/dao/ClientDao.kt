package com.chachadev.heathinfoapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.chachadev.heathinfoapp.data.local.entity.ClientEntity
import com.chachadev.heathinfoapp.data.local.entity.ClientProgramCrossRef
import com.chachadev.heathinfoapp.data.local.entity.ClientWithPrograms

@Dao
interface ClientDao {

    @Insert
    suspend fun insertClient(client: ClientEntity): Long

    @Transaction
    @Query("SELECT * FROM clients")
    suspend fun getAllClientsWithPrograms(): List<ClientWithPrograms>

    @Transaction
    @Query("SELECT * FROM clients WHERE id = :clientId")
    suspend fun getClientWithPrograms(clientId: Int): ClientWithPrograms?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun enrollClientInPrograms(crossRefs: List<ClientProgramCrossRef>)

    @Insert
    suspend fun insertClients(clients: List<ClientEntity>)

    @Query("SELECT * FROM clients")
    suspend fun getAllClients(): List<ClientEntity>


    @Query("SELECT * FROM clients WHERE name LIKE '%' || :query || '%'")
    suspend fun searchClients(query: String): List<ClientEntity>


}