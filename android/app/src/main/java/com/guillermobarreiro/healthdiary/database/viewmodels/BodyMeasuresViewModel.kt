package com.guillermobarreiro.healthdiary.database.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.guillermobarreiro.healthdiary.database.HealthDatabase
import com.guillermobarreiro.healthdiary.database.entities.AverageBodyMeasures
import com.guillermobarreiro.healthdiary.database.entities.BodyMeasuresReading
import com.guillermobarreiro.healthdiary.views.main.BodyMeasuresFragment
import com.guillermobarreiro.healthdiary.views.detail.BodyMeasuresDetailActivity

/**
 * ViewModel which binds all the Body Measures data from the [HealthDatabase]
 * with the UI components [BodyMeasuresFragment] and
 * [BodyMeasuresDetailActivity].
 */
class BodyMeasuresViewModel(application: Application): AndroidViewModel(application) {
    private val db: HealthDatabase = HealthDatabase.getDatabase(application)
    val bodyMeasures: LiveData<List<BodyMeasuresReading>> = db.bodyMeasuresDao().getRecords()
    val averageWeightHeight: LiveData<AverageBodyMeasures> = db.bodyMeasuresDao().getAverageWeightHeight()

    fun insertRecord(reading: BodyMeasuresReading) {
        db.bodyMeasuresDao().insertRecord(reading)
    }

}
