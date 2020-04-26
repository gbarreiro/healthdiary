package com.guillermobarreiro.healthdiary.database.entities

/**
 * Model of the average weight and height of all the [BodyMeasuresReading] objects stored in the [com.guillermobarreiro.healthdiary.database.HealthDatabase].
 */
data class AverageBodyMeasures(val weight: Float, val height: Int)