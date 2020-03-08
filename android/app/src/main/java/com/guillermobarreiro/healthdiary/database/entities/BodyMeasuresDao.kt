package com.guillermobarreiro.healthdiary.database.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * DAO (data access object) for the [BodyMeasuresReading].
 * Allows inserting, deleting and retrieving the body measures records, as well as get the mean values.
 * This DAO wraps the SQL queries into public methods.
 */
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