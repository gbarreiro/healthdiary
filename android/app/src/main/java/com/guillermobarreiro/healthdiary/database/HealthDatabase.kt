package com.guillermobarreiro.healthdiary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BloodPressureReading::class, BodyMeasuresReading::class], version = 1)
abstract class HealthDatabase: RoomDatabase() {
    abstract fun bloodPressureDao(): BloodPressureDao
    abstract fun bodyMeasuresDao(): BodyMeasuresDao

    companion object{
        const val DATABASE_NAME = "healthdiary"
        private var database: HealthDatabase? = null
        fun getDatabase(context:Context): HealthDatabase {
            if(database==null){
                database = Room.databaseBuilder(context, HealthDatabase::class.java, HealthDatabase.DATABASE_NAME).build()
            }
            return database!!
        }
    }
}