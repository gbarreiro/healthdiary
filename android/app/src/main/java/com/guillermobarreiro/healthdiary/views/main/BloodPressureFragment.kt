package com.guillermobarreiro.healthdiary.views.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.guillermobarreiro.healthdiary.R
import com.guillermobarreiro.healthdiary.database.entities.AverageBloodPressure
import com.guillermobarreiro.healthdiary.database.entities.BloodPressureReading
import com.guillermobarreiro.healthdiary.database.viewmodels.BloodPressureViewModel
import com.guillermobarreiro.healthdiary.database.viewmodels.BodyMeasuresViewModel
import com.guillermobarreiro.healthdiary.views.detail.BloodPressureDetailActivity

/**
 * Fragment for registering new blood pressure readings and see the average values.
 * The data is held in a [BodyMeasuresViewModel]
 * This fragment is embedded into the [MainActivity].
 */
class BloodPressureFragment : Fragment(), TextWatcher, TextView.OnEditorActionListener {

    //region Activity views
    private lateinit var randomSwitch: Switch
    private lateinit var insertedSystolic: EditText
    private lateinit var insertedDiastolic: EditText
    private lateinit var registerButton: Button
    private lateinit var averageCard: CardView
    private lateinit var averageDiastolic: TextView
    private lateinit var averageSystolic: TextView
    //endregion

    // region Datasources
    private val viewModel: BloodPressureViewModel by viewModels()
    //endregion

    private val riskColors = mutableMapOf<BloodPressureReading.RiskLevel, Int>()

    //region Fragment lifecycle
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflates the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood_pressure, container, false)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sets up the view
        randomSwitch = view.findViewById(R.id.random_pressure)
        insertedSystolic = view.findViewById(R.id.inserted_systolic)
        insertedDiastolic = view.findViewById(R.id.inserted_diastolic)
        registerButton = view.findViewById(R.id.register_blood_pressure)
        averageCard = view.findViewById(R.id.average_blood_pressure_card)
        averageDiastolic = view.findViewById(R.id.avg_diastolic)
        averageSystolic = view.findViewById(R.id.avg_systolic)

        // Sets up the risk colors dictionary
        riskColors[BloodPressureReading.RiskLevel.NORMAL] = resources.getColor(android.R.color.holo_green_dark)
        riskColors[BloodPressureReading.RiskLevel.ELEVATED] = resources.getColor(android.R.color.holo_orange_light)
        riskColors[BloodPressureReading.RiskLevel.HIGH] = resources.getColor(android.R.color.holo_orange_dark)
        riskColors[BloodPressureReading.RiskLevel.HYPERTENSIVE] = resources.getColor(android.R.color.holo_red_dark)

        // Sets up the onClick listeners
        randomSwitch.setOnClickListener { this.switchChanged() }
        registerButton.setOnClickListener { this.registerPressure() }
        averageCard.setOnClickListener { this.showRecords() }

        // Sets up a text changed listener to the blood pressure input text fields (for enabling/disabling the insert button)
        insertedDiastolic.addTextChangedListener(this)
        insertedSystolic.addTextChangedListener(this)
        insertedSystolic.setOnEditorActionListener(this)

        // Sets up the observer for the ViewModel data
        viewModel.averageSystolicDiastolic.observe(viewLifecycleOwner, Observer<AverageBloodPressure>{ avgValues ->
            // Update the systolic and diastolic mean
            this.updateMean(avgValues.systolic, avgValues.diastolic)
        })

    }

    //endregion

    //region Data reading and writing

    /**
     * Register the blood pressure of the user into the DB.
     * Launched when the "Register value" button is clicked.
     */
    private fun registerPressure(){
        val newBloodPressureReading = if(randomSwitch.isChecked){
            // Inserts a random pressure reading
            BloodPressureReading()

        }else {
            // Registers the inserted weight
            val systolic = insertedSystolic.text.toString().toInt()
            val diastolic = insertedDiastolic.text.toString().toInt()
            BloodPressureReading(
                systolic,
                diastolic
            )

        }

        // Clears the input
        insertedDiastolic.text.clear()
        insertedSystolic.text.clear()

        // Registers the new record in the DB
        viewModel.insertRecord(newBloodPressureReading)

        // The mean will be automatically updated by the ViewModel

    }

    /**
     * Update the displayed mean values.
     * This method is automatically called by the ViewModel observer.
     */
    private fun updateMean(avgSystolic: Int, avgDiastolic: Int){
        val riskLevel = BloodPressureReading.riskLevel(avgSystolic, avgDiastolic)

        // Update the UI with the new values
        val riskColor = riskColors[riskLevel] ?: resources.getColor(android.R.color.holo_purple)
        averageSystolic.text = avgSystolic.toString()
        averageDiastolic.text = avgDiastolic.toString()
        averageDiastolic.setTextColor(riskColor)
        averageSystolic.setTextColor(riskColor)

    }

    /**
     * Open a [.views.detail.BodyMeasuresDetailActivity] for showing a list with all the records.
     */
    private fun showRecords(){
        val intent = Intent(context, BloodPressureDetailActivity::class.java)
        startActivity(intent)
    }

    //endregion

    //region User interaction
    /**
     * Enables or disables the input text fields.
     * Launched when the "random values" switch is toggled.
     */
    private fun switchChanged(){
        insertedDiastolic.isEnabled = !(randomSwitch.isChecked)
        insertedSystolic.isEnabled = !(randomSwitch.isChecked)
        registerButton.isEnabled = randomSwitch.isChecked || (insertedSystolic.text.isNotEmpty() && insertedDiastolic.text.isNotEmpty())

    }

    override fun afterTextChanged(s: Editable?) {
        // Nothing to do
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Nothing to do
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Check if there is something typed on the text field, to enable or disable the register button
        registerButton.isEnabled = randomSwitch.isChecked || (insertedDiastolic.text.isNotEmpty() && insertedSystolic.text.isNotEmpty())
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if(actionId == EditorInfo.IME_ACTION_GO){
            // Registers the blood pressure
            registerPressure()
        }
        return true
    }

    //endregion
}
