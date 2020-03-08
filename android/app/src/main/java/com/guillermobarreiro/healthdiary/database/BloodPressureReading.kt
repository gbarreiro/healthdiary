package com.guillermobarreiro.healthdiary.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date
import java.util.Random

/**
 * Represents a blood pressure reading, this means,
 * the sampling of the user's systolic and diastolic blood pressure values in a specific moment.
 *
 * Both systolic and diastolic values are stored as Integer values, expressed in mmHg.
 *
 */
@Entity
data class BloodPressureReading(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "systolic") val systolic: Int,
    @ColumnInfo(name = "diastolic") val diastolic: Int,
    @ColumnInfo(name = "timestamp")  val timestamp: Date
) {

    companion object {
        val randomGenerator = Random() // needed for creating random gaussian values
        fun riskLevel(systolic: Int, diastolic: Int): RiskLevel{
            return when {
                systolic < 120 && diastolic < 80 -> RiskLevel.NORMAL
                systolic < 129  && diastolic < 80 -> RiskLevel.ELEVATED
                systolic < 180 || diastolic < 120 -> RiskLevel.HIGH
                else -> RiskLevel.HYPERTENSIVE
            }
        }
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
    @Ignore val riskLevel = riskLevel(this.systolic, this.diastolic)

    /**
     * Constructor for creating a pressure reading with the current date
     */
    constructor(systolic: Int, diastolic: Int): this(null, systolic, diastolic, Date())

    /**
     * Constructor for creating a random test pressure reading
     */
    constructor(): this(null, (randomGenerator.nextGaussian()*20+120).toInt(), (randomGenerator.nextGaussian()*20+70).toInt(), Date())

}