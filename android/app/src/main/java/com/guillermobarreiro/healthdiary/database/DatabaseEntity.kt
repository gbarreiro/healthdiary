package com.guillermobarreiro.healthdiary.database

import android.content.ContentValues
import android.os.Bundle

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