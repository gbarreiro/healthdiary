package com.guillermobarreiro.healthdiary.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.util.*

/**
 * SQLite database that stores the blood pressure and body measures readings.
 * This is a helper for connecting with the DB and reading and writing readings from/to it.
 */
class HealthDatabase(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //region Database creation

    /**
     * Creates the database. This is run if no database exists yet.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        // Creates the weight and blood pressure tables
        db?.execSQL(CREATE_BODY_MEASURES_TABLE)
        db?.execSQL(CREATE_BLOOD_PRESSURE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // do nothing
    }

    //endregion

    //region Write into database

    /**
     * Inserts a new record in the database. The record must be an object implementing the interface DatabaseEntity,
     * this is, a [BloodPressureReading] or [BodyMeasuresReading] object.
     * @param record Blood pressure or body measures reading we want to store on the database.
     * @param table Table where the record will be stored: [DatabaseTables.BODY_MEASURES] for body measures, or [DatabaseTables.BLOOD_PRESSURE] for blood pressure readings.
     */
    fun insertRecord(record: DatabaseEntity, table: DatabaseTables){
        val db = this.writableDatabase
        db?.insert(table.table, null, record.values)
    }

    //endregion

    //region Read from database

    /**
     * Returns an array with all the blood pressure readings stored in the database.
     * The readings are returned as [BloodPressureReading] objects.
     */
    fun getBloodPressureRecords(): Array<BloodPressureReading> {
        val db = this.readableDatabase
        val cursor = db.query(DatabaseTables.BLOOD_PRESSURE.table, null, null, null, null, null, "${BloodPressureReading.DatabaseEntry.COLUMN_TIMESTAMP} DESC")

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
     * Calculates the mean systolic and diastolic values of the readings stored in the DB, and returns it as a new [BloodPressureReading] object
     * @return [BloodPressureReading] object with the mean values and the current timestamp, or null if there are no records
     */
    fun calculateBloodPressureMean(): BloodPressureReading? {
        val records = getBloodPressureRecords()
        var systolic = 0
        var diastolic = 0

        if(records.size>0){
            for(record in records){
                systolic += record.systolic
                diastolic += record.diastolic
            }

            systolic /= records.size
            diastolic /= records.size

            return BloodPressureReading(systolic, diastolic)

        }else{
            return null
        }

    }

    /**
     * Returns an array with all the body measures readings stored in the database.
     * The readings are returned as [BodyMeasuresReading] objects.
     */
    fun getBodyMeasureRecords(): Array<BodyMeasuresReading>{
        val db = this.readableDatabase
        val cursor = db.query(DatabaseTables.BODY_MEASURES.table, null, null, null, null, null, "${BodyMeasuresReading.DatabaseEntry.COLUMN_TIMESTAMP} DESC")

        // Iterates through the cursor, for converting each entry into an object and store it into the array
        val records = mutableListOf<BodyMeasuresReading>()
        with(cursor){
            while(moveToNext()) {
                val weight = getFloat(getColumnIndex(BodyMeasuresReading.DatabaseEntry.COLUMN_WEIGHT))
                val height = getInt(getColumnIndex(BodyMeasuresReading.DatabaseEntry.COLUMN_HEIGHT))
                val timestamp = getLong(getColumnIndex(BodyMeasuresReading.DatabaseEntry.COLUMN_TIMESTAMP))
                val reading = BodyMeasuresReading(weight, height, Date(timestamp))
                records.add(reading)
            }
        }

        return records.toTypedArray() // returns the array
    }

    /**
     * Calculates the mean weight and height values of the readings stored in the DB, and returns it as a new [BodyMeasuresReading] object
     * @return [BodyMeasuresReading] object with the mean value and the current timestamp
     */
    fun calculateBodyMeasureMean(): BodyMeasuresReading? {
        val records = getBodyMeasureRecords()
        var weight = 0.0
        var height = 0

        if(records.size>0){
            for(record in records){
                weight += record.weight
                height += record.height
            }

            weight /= records.size
            height /= records.size

            return BodyMeasuresReading(weight.toFloat(), height)
        }else{
            return null
        }


    }

    //endregion

    //region Database structure

    /**
     * Names of the two tables existing in the DB
     */
    enum class DatabaseTables(val table: String) {
        BODY_MEASURES(BodyMeasuresReading.DatabaseEntry.TABLE_NAME),
        BLOOD_PRESSURE(BloodPressureReading.DatabaseEntry.TABLE_NAME)
    }

    /**
     * Static parameters for creating the DB
     */
    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "HealthDiary.db"
        private const val CREATE_BODY_MEASURES_TABLE = "CREATE TABLE ${BodyMeasuresReading.DatabaseEntry.TABLE_NAME} (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${BodyMeasuresReading.DatabaseEntry.COLUMN_WEIGHT} REAL, ${BodyMeasuresReading.DatabaseEntry.COLUMN_HEIGHT} INTEGER, ${BodyMeasuresReading.DatabaseEntry.COLUMN_TIMESTAMP} INTEGER)"
        private const val CREATE_BLOOD_PRESSURE_TABLE = "CREATE TABLE ${BloodPressureReading.DatabaseEntry.TABLE_NAME} (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${BloodPressureReading.DatabaseEntry.COLUMN_SYSTOLIC} INTEGER, ${BloodPressureReading.DatabaseEntry.COLUMN_DIASTOLIC} INTEGER, ${BloodPressureReading.DatabaseEntry.COLUMN_TIMESTAMP} INTEGER)"
    }

    //endregion

}