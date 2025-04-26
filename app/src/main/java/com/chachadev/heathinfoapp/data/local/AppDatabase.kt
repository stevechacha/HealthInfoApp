package com.chachadev.heathinfoapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chachadev.heathinfoapp.data.local.dao.ClientDao
import com.chachadev.heathinfoapp.data.local.dao.ProgramDao
import com.chachadev.heathinfoapp.data.local.entity.ClientEntity
import com.chachadev.heathinfoapp.data.local.entity.ClientProgramCrossRef
import com.chachadev.heathinfoapp.data.local.entity.HealthProgramEntity

@Database(
    entities = [
        ClientEntity::class,
        HealthProgramEntity::class,
        ClientProgramCrossRef::class
               ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun programDao(): ProgramDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "health-info-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}