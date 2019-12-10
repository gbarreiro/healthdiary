package com.guillermobarreiro.healthdiary.database

import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import java.util.*
import kotlin.random.Random

/**
 * Represents a body measure reading, this means, the sampling of the user's weight and height in a specific moment.
 * The weight is stored as a decimal number in kg, and the height as an integer in cm.
 */
class BodyMeasureReading(val weight: Float, val height: Int, val timestamp: Date) : DatabaseEntity {

    /**
     * Object representation as a ContentValues key-value dictionary
     */
    override val values = ContentValues().apply {
        put(DatabaseEntry.COLUMN_WEIGHT, weight)
        put(DatabaseEntry.COLUMN_HEIGHT, height)
        put(DatabaseEntry.COLUMN_TIMESTAMP, timestamp.time)
    }

    /**
     * Object representation as a ContentValues key-value dictionary
     */
    override val bundle = Bundle().apply {
        putFloat(DatabaseEntry.COLUMN_WEIGHT, weight)
        putInt(DatabaseEntry.COLUMN_HEIGHT, height)
        putLong(DatabaseEntry.COLUMN_TIMESTAMP, timestamp.time)
    }

    /**
     * Constructor for creating a weight reading with the current date
     */
    constructor(weight: Float, height: Int): this(weight, height, Date())

    /**
     * Constructor for creating a random test weight reading
     */
    constructor(): this(40 + Random.nextFloat()*60, Random.nextInt(120, 200), Date())

    /**
     * Constructor for creating a weight reading from a ContentValues object
     */
    constructor(values: ContentValues):this(values.getAsFloat(DatabaseEntry.COLUMN_WEIGHT),
        values.getAsInteger(DatabaseEntry.COLUMN_HEIGHT),
        Date(values.getAsLong(DatabaseEntry.COLUMN_TIMESTAMP)))

    /**
     * Constructor for creating a weight reading from a Bundle object
     */
    constructor(bundle: Bundle):this(bundle.getFloat(DatabaseEntry.COLUMN_WEIGHT),
        bundle.getInt(DatabaseEntry.COLUMN_HEIGHT),
        Date(bundle.getLong(DatabaseEntry.COLUMN_TIMESTAMP)))

    /**
     * Contract for transforming the weight readings from and into the database
     */
    object DatabaseEntry: BaseColumns {
        const val TABLE_NAME = "weight"
        const val COLUMN_WEIGHT = "weight"
        const val COLUMN_HEIGHT = "height"
        const val COLUMN_TIMESTAMP = "timestamp"
    }
}