package com.guillermobarreiro.healthdiary.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.math.pow

/**
 * Represents a body measure reading, this means, the sampling of the user's weight and height in a specific moment.
 * The weight is stored as a decimal number in kg, and the height as an integer in cm.
 */

@Entity
data class BodyMeasuresReading(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Date
) {

    companion object {
        val randomGenerator = Random() // needed for generating gaussian random values

        fun bmi(weight: Float, height: Int): Double{
            return weight / (height.toFloat()/100).pow(2).toDouble()
        }

        fun bmiLevel(bmi: Double): BMILevel{
            return when {
                bmi < 18.5 -> BMILevel.UNDERWEIGHT
                bmi < 24.9 -> BMILevel.HEALTHY
                bmi < 29.9 -> BMILevel.OVERWEIGHT
                else -> BMILevel.OBESE
            }
        }
    }

    /**
     * Body measure index is calculated as kg / (m)².
     * This index gives a ratio of how low or high is the weight of a person.
     *
     * Risk levels:
     * - <18.5: Underweight
     * - 18.5 - 24.9: healthy
     * - 25 - 29.9: overweight
     * - 30 - 39.9: obese
     */
    val bmi = bmi(this.weight, this.height)

    enum class BMILevel {UNDERWEIGHT, HEALTHY, OVERWEIGHT, OBESE}

    val bmiLevel = bmiLevel(this.bmi)

    /**
     * Constructor for creating a weight reading with the current date
     */
    constructor(weight: Float, height: Int): this(null, weight, height, Date())

    /**
     * Constructor for creating a random test weight reading
     */
    constructor(): this(null, (randomGenerator.nextGaussian()*15+60).toFloat(), (randomGenerator.nextGaussian()*10+170).toInt(), Date())

}