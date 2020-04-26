package com.guillermobarreiro.healthdiary.database.entities

/**
 * Model of the average systolic and diastolic values of all the [com.guillermobarreiro.healthdiary.database.BloodPressureReading] objects
 * stored in the [.database.HealthDatabase].
 */
data class AverageBloodPressure(val systolic: Int, val diastolic: Int)