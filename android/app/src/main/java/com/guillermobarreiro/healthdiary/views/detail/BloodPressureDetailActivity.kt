package com.guillermobarreiro.healthdiary.views.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guillermobarreiro.healthdiary.R
import com.guillermobarreiro.healthdiary.database.adapters.BloodPressureReadingsAdapter
import com.guillermobarreiro.healthdiary.database.entities.BloodPressureReading
import com.guillermobarreiro.healthdiary.database.viewmodels.BloodPressureViewModel

/**
 * Detail activity for displaying all the stored blood pressure records.
 * The records are displayed in a RecyclerView using the [BloodPressureReadingsAdapter].
 * The data is held in a [BloodPressureViewModel]
 */
class BloodPressureDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val viewModel: BloodPressureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_pressure_detail)

        // Shows up button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Sets up the recycler view
        val readingsAdapter = BloodPressureReadingsAdapter(baseContext)
        recyclerView = findViewById<RecyclerView>(R.id.blood_pressure_recycler).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(baseContext)
            adapter = readingsAdapter
        }

        // Sets up the observer for the ViewModel data
        viewModel.bloodPressureRecords.observe(this, Observer<List<BloodPressureReading>> { readings ->
            readingsAdapter.setDataSource(readings)
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
