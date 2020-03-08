package com.guillermobarreiro.healthdiary.database

import android.content.Context
import androidx.room.*
import com.guillermobarreiro.healthdiary.database.entities.BloodPressureDao
import com.guillermobarreiro.healthdiary.database.entities.BloodPressureReading
import com.guillermobarreiro.healthdiary.database.entities.BodyMeasuresDao
import com.guillermobarreiro.healthdiary.database.entities.BodyMeasuresReading
import java.util.*

/**
 * [RoomDatabase] for storing the blood pressure records ([BloodPressureReading]) and the body measures records ([BodyMeasuresReading]).
 * CRUD operations are done through the respective DAOs, accessible through the methods [bloodPressureDao] and [bodyMeasuresDao].
 * The database object is a singleton, managed from this class. To get the singleton instance, use the [getDatabase] method.
 */
@TypeConverters(DateConverter::class)
@Database(entities = [BloodPressureReading::class, BodyMeasuresReading::class], version = 1)
abstract class HealthDatabase: RoomDatabase() {
    abstract fun bloodPressureDao(): BloodPressureDao
    abstract fun bodyMeasuresDao(): BodyMeasuresDao

    companion object{
        const val DATABASE_NAME = "healthdiary"
        private var database: HealthDatabase? = null
        fun getDatabase(context:Context): HealthDatabase {
            if(database==null){
                database = Room.databaseBuilder(context, HealthDatabase::class.java, HealthDatabase.DATABASE_NAME).allowMainThreadQueries().build()
            }
            return database!!
        }
    }
}