package com.guillermobarreiro.healthdiary.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BodyMeasuresDao {

    @Insert
    fun insertRecord(record: BodyMeasuresReading)

    @Delete
    fun deleteRecord(record: BodyMeasuresReading)

    @Query("SELECT * FROM bodymeasuresreading ORDER BY timestamp DESC")
    fun getRecords(): List<BodyMeasuresReading>

    @Query("SELECT avg(weight) FROM bodymeasuresreading")
    fun getAverageWeight(): Float

    @Query("SELECT avg(height) FROM bodymeasuresreading")
    fun getAverageHeight(): Int

}