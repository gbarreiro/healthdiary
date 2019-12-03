package com.guillermobarreiro.healthdiary.database

import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import java.util.*
import kotlin.random.Random

class WeightReading(val weight: Float, val timestamp: Date) : DatabaseEntity {

    /**
     * Object representation as a ContentValues key-value dictionary
     */
    override val values = ContentValues().apply {
        put(DatabaseEntry.COLUMN_WEIGHT, weight)
        put(DatabaseEntry.COLUMN_TIMESTAMP, timestamp.time)
    }

    /**
     * Object representation as a ContentValues key-value dictionary
     */
    override val bundle = Bundle().apply {
        putFloat(DatabaseEntry.COLUMN_WEIGHT, weight)
        putLong(DatabaseEntry.COLUMN_TIMESTAMP, timestamp.time)
    }

    /**
     * Constructor for creating a weight reading with the current date
     */
    constructor(weight: Float): this(weight, Date())

    /**
     * Constructor for creating a random test weight reading
     */
    constructor(): this(40 + Random.nextFloat()*60, Date())

    /**
     * Constructor for creating a weight reading from a ContentValues object
     */
    constructor(values: ContentValues):this(values.getAsFloat(DatabaseEntry.COLUMN_WEIGHT),
        Date(values.getAsLong(DatabaseEntry.COLUMN_TIMESTAMP)))

    /**
     * Constructor for creating a weight reading from a Bundle object
     */
    constructor(bundle: Bundle):this(bundle.getFloat(DatabaseEntry.COLUMN_WEIGHT),
        Date(bundle.getLong(DatabaseEntry.COLUMN_TIMESTAMP)))

    /**
     * Contract for transforming the weight readings from and into the database
     */
    object DatabaseEntry: BaseColumns {
        const val TABLE_NAME = "weight"
        const val COLUMN_WEIGHT = "weight"
        const val COLUMN_TIMESTAMP = "timestamp"
    }
}