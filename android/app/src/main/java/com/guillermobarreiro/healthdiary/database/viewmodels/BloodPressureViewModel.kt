package com.guillermobarreiro.healthdiary.database.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.guillermobarreiro.healthdiary.database.HealthDatabase
import com.guillermobarreiro.healthdiary.database.entities.AverageBloodPressure
import com.guillermobarreiro.healthdiary.database.entities.BloodPressureReading
import com.guillermobarreiro.healthdiary.views.main.BloodPressureFragment
import com.guillermobarreiro.healthdiary.views.detail.BloodPressureDetailActivity

/**
 * ViewModel which binds all the Blood Pressure data from the [HealthDatabase]
 * with the UI components [BloodPressureFragment] and
 * [BloodPressureDetailActivity].
 */
class BloodPressureViewModel(application: Application): AndroidViewModel(application) {
    private val db: HealthDatabase = HealthDatabase.getDatabase(application)
    val bloodPressureRecords: LiveData<List<BloodPressureReading>> = db.bloodPressureDao().getRecords()
    val averageSystolicDiastolic: LiveData<AverageBloodPressure> = db.bloodPressureDao().getAverageSystolicDiastolic()

    fun insertRecord(reading: BloodPressureReading) {
        db.bloodPressureDao().insertRecord(reading)
    }

}