package com.guillermobarreiro.healthdiary.database

import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import java.util.Date
import java.util.Random

/**
 * Represents a blood pressure reading, this means,
 * the sampling of the user's systolic and diastolic blood pressure values in a specific moment.
 *
 * Both systolic and diastolic values are stored as Integer values, expressed in mmHg.
 *
 */
data class BloodPressureReading(val systolic: Int, val diastolic: Int, val timestamp: Date): DatabaseEntity {

    companion object {
        val randomGenerator = Random() // needed for creating random gaussian values
    }

    enum class RiskLevel {NORMAL, ELEVATED, HIGH, HYPERTENSIVE}

    /**
        Blood pressure risk level, based on <a href="https://www.heart.org/en/health-topics/high-blood-pressure/understanding-blood-pressure-readings">heart.org</a> ranges.

        Risk levels:
         - Systolic < 120 and Diastolic < 80: Normal
         - Systolic < 129 and Diastolic < 80: Elevated
         - Systolic < 180 and Diastolic < 120: High
         - Systolic > 180 and Diastolic > 120: Hypertensive
     */
    val riskLevel = when {
        systolic < 120 && diastolic < 80 -> RiskLevel.NORMAL
        systolic < 129  && diastolic < 80 -> RiskLevel.ELEVATED
        systolic < 180 || diastolic < 120 -> RiskLevel.HIGH
        else -> RiskLevel.HYPERTENSIVE
    }

    /**
     * Object representation as a [ContentValues] key-value dictionary
     */
    override val values = ContentValues().apply {
        put(DatabaseEntry.COLUMN_DIASTOLIC, diastolic)
        put(DatabaseEntry.COLUMN_SYSTOLIC, systolic)
        put(DatabaseEntry.COLUMN_TIMESTAMP, timestamp.time)
    }

    /**
     * Object representation as a [Bundle] key-value dictionary
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
    constructor(): this((randomGenerator.nextGaussian()*20+120).toInt(), (randomGenerator.nextGaussian()*20+70).toInt(), Date())

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