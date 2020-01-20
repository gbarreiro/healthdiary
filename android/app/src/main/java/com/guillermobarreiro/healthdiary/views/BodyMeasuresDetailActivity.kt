package com.guillermobarreiro.healthdiary.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guillermobarreiro.healthdiary.R
import com.guillermobarreiro.healthdiary.database.BloodPressureReadingsAdapter
import com.guillermobarreiro.healthdiary.database.BodyMeasuresReadingsAdapter
import com.guillermobarreiro.healthdiary.database.HealthDatabase

/**
 * Detail activity for displaying all the stored body measures records.
 * The records are displayed in a [RecyclerView] using the [BodyMeasuresReadingsAdapter].
 */
class BodyMeasuresDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var db: HealthDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_measures_detail)

        // Shows up button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Sets up the DB connection
        db = HealthDatabase(this)

        // Sets up the recycler view
        recyclerView = findViewById<RecyclerView>(R.id.body_measures_recycler).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(baseContext)
            adapter = BodyMeasuresReadingsAdapter(baseContext, db)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
