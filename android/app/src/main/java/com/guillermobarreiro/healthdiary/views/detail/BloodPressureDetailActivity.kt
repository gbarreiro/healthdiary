package com.guillermobarreiro.healthdiary.views.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guillermobarreiro.healthdiary.R
import com.guillermobarreiro.healthdiary.database.adapters.BloodPressureReadingsAdapter
import com.guillermobarreiro.healthdiary.database.HealthDatabase

/**
 * Detail activity for displaying all the stored blood pressure records.
 * The records are displayed in a RecyclerView using the [BloodPressureReadingsAdapter].
 */
class BloodPressureDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: HealthDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_pressure_detail)

        // Shows up button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Sets up the DB connection
        db = HealthDatabase.getDatabase(applicationContext)

        // Sets up the recycler view
        recyclerView = findViewById<RecyclerView>(R.id.blood_pressure_recycler).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(baseContext)
            adapter =
                BloodPressureReadingsAdapter(
                    baseContext,
                    db
                )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
