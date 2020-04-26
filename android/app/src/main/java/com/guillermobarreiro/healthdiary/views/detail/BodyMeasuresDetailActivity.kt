package com.guillermobarreiro.healthdiary.views.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guillermobarreiro.healthdiary.R
import com.guillermobarreiro.healthdiary.database.adapters.BodyMeasuresReadingsAdapter
import com.guillermobarreiro.healthdiary.database.entities.BodyMeasuresReading
import com.guillermobarreiro.healthdiary.database.viewmodels.BodyMeasuresViewModel

/**
 * Detail activity for displaying all the stored body measures records.
 * The records are displayed in a [RecyclerView] using the [BodyMeasuresReadingsAdapter].
 * The data is held in a [BodyMeasuresViewModel]
 */
class BodyMeasuresDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val viewModel: BodyMeasuresViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_measures_detail)

        // Shows up button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Sets up the recycler view
        val readingsAdapter = BodyMeasuresReadingsAdapter(baseContext)
        recyclerView = findViewById<RecyclerView>(R.id.body_measures_recycler).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(baseContext)
            adapter = readingsAdapter
        }

        // Sets up the observer for the ViewModel data
        viewModel.bodyMeasures.observe(this, Observer<List<BodyMeasuresReading>> { readings ->
            readingsAdapter.setDataSource(readings)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
