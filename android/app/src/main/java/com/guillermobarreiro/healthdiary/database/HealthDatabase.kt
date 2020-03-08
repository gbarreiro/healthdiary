package com.guillermobarreiro.healthdiary.database

import android.content.Context
import androidx.room.*
import java.util.*

@TypeConverters(DateConverter::class)
@Database(entities = [BloodPressureReading::class, BodyMeasuresReading::class], version = 1)
abstract class HealthDatabase: RoomDatabase() {
    abstract fun bloodPressureDao(): BloodPressureDao
    abstract fun bodyMeasuresDao(): BodyMeasuresDao

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

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