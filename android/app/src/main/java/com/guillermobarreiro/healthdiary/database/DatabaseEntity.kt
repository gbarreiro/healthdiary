package com.guillermobarreiro.healthdiary.database

import android.content.ContentValues
import android.os.Bundle

/**
 * Common interface for the BodyMeasuresReading and BloodPressureReading objects.
 * Defines a reading that can be stored on a database.
 */
interface DatabaseEntity {
    /**
     * Object representation as a ContentValues key-value dictionary
     */
    val values: ContentValues
    /**
     * Object representation as a ContentValues key-value dictionary
     */
    val bundle: Bundle

}