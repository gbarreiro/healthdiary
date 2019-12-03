package com.guillermobarreiro.healthdiary.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.util.*

private const val CREATE_WEIGHT_TABLE = "CREATE TABLE ${WeightReading.DatabaseEntry.TABLE_NAME} (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${WeightReading.DatabaseEntry.COLUMN_WEIGHT} REAL, ${WeightReading.DatabaseEntry.COLUMN_TIMESTAMP} INTEGER)"
private const val CREATE_BLOOD_PRESSURE_TABLE = "CREATE TABLE ${BloodPressureReading.DatabaseEntry.TABLE_NAME} (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${BloodPressureReading.DatabaseEntry.COLUMN_SYSTOLIC} INTEGER, ${BloodPressureReading.DatabaseEntry.COLUMN_DIASTOLIC} INTEGER, ${BloodPressureReading.DatabaseEntry.COLUMN_TIMESTAMP} INTEGER)"

/**
 * Helper for connecting and working with the DB.
 */
class HealthDatabase(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * Names of the two tables existing in the DB
     */
    enum class DatabaseTables(val table: String) {
        WEIGHT(WeightReading.DatabaseEntry.TABLE_NAME),
        BLOOD_PRESSURE(BloodPressureReading.DatabaseEntry.TABLE_NAME)
    }

    /**
     * Initializes the DB
     */
    override fun onCreate(db: SQLiteDatabase?) {
        // Creates the weight and blood pressure tables
        db?.execSQL(CREATE_WEIGHT_TABLE)
        db?.execSQL(CREATE_BLOOD_PRESSURE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // do nothing
    }

    /**
     * Inserts a new record in the DB.
     * @param record object implementing DatabaseEntity
     * @param table Table where the record will be stored
     */
    fun insertRecord(record: DatabaseEntity, table: DatabaseTables){
        val db = this.writableDatabase
        db?.insert(table.table, null, record.values)
    }

    /**
     * Returns an array with all the blood pressure readings stored in the DB
     */
    fun getBloodPressureRecords(): Array<BloodPressureReading> {
        val db = this.readableDatabase
        val cursor = db.query(DatabaseTables.BLOOD_PRESSURE.table, null, null, null, null, null, null)

        // Iterates through the cursor, for converting each entry into an object and store it into the array
        val records = mutableListOf<BloodPressureReading>()
        with(cursor){
            while(moveToNext()) {
                val diastolic = getInt(getColumnIndex(BloodPressureReading.DatabaseEntry.COLUMN_DIASTOLIC))
                val systolic = getInt(getColumnIndex(BloodPressureReading.DatabaseEntry.COLUMN_SYSTOLIC))
                val timestamp = getLong(getColumnIndex(BloodPressureReading.DatabaseEntry.COLUMN_TIMESTAMP))
                val reading = BloodPressureReading(systolic, diastolic, Date(timestamp))
                records.add(reading)
            }
        }

        return records.toTypedArray() // returns the array


    }

    /**
     * Calculates the mean systolic and diastolic values of the readings stored in the DB, and returns it as a new BloodPressureReading object
     * @return BloodPressureReading object with the mean values and the current timestamp
     */
    fun calculateBloodPressureMean(): BloodPressureReading {
        val records = getBloodPressureRecords()
        var systolic = 0
        var diastolic = 0

        for(record in records){
            systolic += record.systolic
            diastolic += record.diastolic
        }

        if(records.size>1){
            systolic /= records.size
            diastolic /= records.size
        }

        return BloodPressureReading(systolic, diastolic)

    }

    /**
     * Calculates the mean weight value of the readings stored in the DB, and returns it as a new WeightReading object
     * @return WeightReading object with the mean value and the current timestamp
     */
    fun calculateWeightMean(): WeightReading {
        val records = getWeightRecords()
        var weight = 0.0

        for(record in records){
            weight += record.weight
        }

        if(records.size>1){
            weight /= records.size
        }

        return WeightReading(weight.toFloat())

    }

    /**
     * Returns an array with all the blood pressure readings stored in the DB
     */
    fun getWeightRecords(): Array<WeightReading>{
        val db = this.readableDatabase
        val cursor = db.query(DatabaseTables.WEIGHT.table, null, null, null, null, null, null)

        // Iterates through the cursor, for converting each entry into an object and store it into the array
        val records = mutableListOf<WeightReading>()
        with(cursor){
            while(moveToNext()) {
                val weight = getFloat(getColumnIndex(WeightReading.DatabaseEntry.COLUMN_WEIGHT))
                val timestamp = getLong(getColumnIndex(WeightReading.DatabaseEntry.COLUMN_TIMESTAMP))
                val reading = WeightReading(weight, Date(timestamp))
                records.add(reading)
            }
        }

        return records.toTypedArray() // returns the array
    }


    /**
     * Static parameters for creating the DB
     */
    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "HealthDiary.db"
    }

}