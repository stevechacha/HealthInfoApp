package com.chachadev.heathinfoapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chachadev.heathinfoapp.data.local.dao.EnrollmentDao
import com.chachadev.heathinfoapp.data.local.dao.PatientDao
import com.chachadev.heathinfoapp.data.local.dao.ProgramDao
import com.chachadev.heathinfoapp.data.local.entity.EnrollmentEntity
import com.chachadev.heathinfoapp.data.local.entity.PatientEntity
import com.chachadev.heathinfoapp.data.local.entity.ProgramEntity
import com.chachadev.heathinfoapp.data.local.utils.Converters


// HealthcareDatabase.kt
@Database(
    entities = [PatientEntity::class, ProgramEntity::class, EnrollmentEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HealthcareDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun programDao(): ProgramDao
    abstract fun enrollmentDao(): EnrollmentDao

    companion object {
        @Volatile
        private var INSTANCE: HealthcareDatabase? = null

        fun getDatabase(context: Context): HealthcareDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthcareDatabase::class.java,
                    "healthcare_db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate if needed
                        }
                    })
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

//    companion object {
//        @Volatile private var INSTANCE: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "health-info-db"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}