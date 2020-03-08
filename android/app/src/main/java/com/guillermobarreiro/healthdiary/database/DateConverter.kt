package com.guillermobarreiro.healthdiary.database

import androidx.room.TypeConverter
import java.util.*

/**
 * [TypeConverter] for converting [Date] objects into a primitive type compatible with Android Room.
 */
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}