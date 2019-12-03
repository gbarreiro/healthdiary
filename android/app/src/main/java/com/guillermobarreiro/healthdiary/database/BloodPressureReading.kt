package com.guillermobarreiro.healthdiary.database

import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import java.util.*
import kotlin.random.Random

data class BloodPressureReading(val systolic: Int, val diastolic: Int, val timestamp: Date): DatabaseEntity {

    /**
     * Object representation as a ContentValues key-value dictionary
     */
    override val values = ContentValues().apply {
        put(DatabaseEntry.COLUMN_DIASTOLIC, diastolic)
        put(DatabaseEntry.COLUMN_SYSTOLIC, systolic)
        put(DatabaseEntry.COLUMN_TIMESTAMP, timestamp.time)
    }

    /**
     * Object representation as a Bundle key-value dictionary
     */
    override val bundle = Bundle().apply {
        putInt(DatabaseEntry.COLUMN_DIASTOLIC, diastolic)
        putInt(DatabaseEntry.COLUMN_SYSTOLIC, systolic)
        putLong(DatabaseEntry.COLUMN_TIMESTAMP, timestamp.time)
    }

    /**
     * Constructor for creating a pressure reading with the current date
     */
    constructor(systolic: Int, diastolic: Int): this(systolic, diastolic, Date())

    /**
     * Constructor for creating a random test pressure reading
     */
    constructor(): this(Random.nextInt(100, 180), Random.nextInt(60, 105), Date())

    /**
     * Constructor for creating a pressure reading from a ContentValues object
     */
    constructor(values: ContentValues):this(values.getAsInteger(DatabaseEntry.COLUMN_SYSTOLIC), values.getAsInteger(DatabaseEntry.COLUMN_DIASTOLIC),
        Date(values.getAsLong(DatabaseEntry.COLUMN_TIMESTAMP)))

    /**
     * Constructor for creating a pressure reading from a Bundle object
     */
    constructor(bundle: Bundle):this(bundle.getInt(DatabaseEntry.COLUMN_SYSTOLIC), bundle.getInt(DatabaseEntry.COLUMN_DIASTOLIC),
        Date(bundle.getLong(DatabaseEntry.COLUMN_TIMESTAMP)))

    /**
     * Contract for transforming the Blood Pressure readings from and into the database
     */
    object DatabaseEntry: BaseColumns{
        const val TABLE_NAME = "blood_pressure"
        const val COLUMN_DIASTOLIC = "diastolic"
        const val COLUMN_SYSTOLIC = "systolic"
        const val COLUMN_TIMESTAMP = "timestamp"
    }

}