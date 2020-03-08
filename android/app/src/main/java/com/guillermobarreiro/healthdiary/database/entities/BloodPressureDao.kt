package com.guillermobarreiro.healthdiary.database.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * DAO (data access object) for the [BloodPressureReading].
 * Allows inserting, deleting and retrieving the blood pressure records, as well as get the mean values.
 * This DAO wraps the SQL queries into public methods.
 */
@Dao
interface BloodPressureDao {

    @Insert
    fun insertRecord(record: BloodPressureReading)

    @Delete
    fun deleteRecord(record: BloodPressureReading)

    @Query("SELECT * FROM bloodpressurereading ORDER BY timestamp DESC")
    fun getRecords(): List<BloodPressureReading>

    @Query("SELECT avg(systolic) FROM bloodpressurereading")
    fun getAverageSystolic(): Int

    @Query("SELECT avg(diastolic) FROM bloodpressurereading")
    fun getAverageDiastolic(): Int

}