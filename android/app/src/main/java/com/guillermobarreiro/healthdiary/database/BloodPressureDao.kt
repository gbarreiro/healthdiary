package com.guillermobarreiro.healthdiary.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

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