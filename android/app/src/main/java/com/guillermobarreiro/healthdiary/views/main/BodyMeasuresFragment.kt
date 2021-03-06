package com.guillermobarreiro.healthdiary.views.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.guillermobarreiro.healthdiary.R
import com.guillermobarreiro.healthdiary.database.entities.AverageBodyMeasures
import com.guillermobarreiro.healthdiary.database.entities.BodyMeasuresReading
import com.guillermobarreiro.healthdiary.database.viewmodels.BodyMeasuresViewModel
import com.guillermobarreiro.healthdiary.views.detail.BodyMeasuresDetailActivity

/**
 * Fragment for registering new body measures readings and see the average values.
 * The data is held in a [BodyMeasuresViewModel]
 * This fragment is embedded into the [MainActivity].
 */
class BodyMeasuresFragment : Fragment(), TextWatcher, TextView.OnEditorActionListener{

    //region Activity views
    private lateinit var randomSwitch: Switch
    private lateinit var insertedWeight: EditText
    private lateinit var insertedHeight: EditText
    private lateinit var registerButton: Button
    private lateinit var averageCard: CardView
    private lateinit var averageWeight: TextView
    private lateinit var averageHeight: TextView
    private lateinit var averageBmi: TextView
    //endregion

    //region Datasources
    private val viewModel: BodyMeasuresViewModel by viewModels()
    //endregion

    private val riskColors = mutableMapOf<BodyMeasuresReading.BMILevel, Int>()

    //region Fragment lifecycle
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflates the layout for this fragment
        return inflater.inflate(R.layout.fragment_body_measures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sets up the view
        randomSwitch = view.findViewById(R.id.random_measure)
        insertedWeight = view.findViewById(R.id.inserted_weight)
        insertedHeight = view.findViewById(R.id.inserted_height)
        registerButton = view.findViewById(R.id.register_body_measure)
        averageCard = view.findViewById(R.id.average_body_measures_card)
        averageWeight = view.findViewById(R.id.avg_weight)
        averageHeight = view.findViewById(R.id.avg_height)
        averageBmi = view.findViewById(R.id.avg_bmi)

        // Sets up the risk colors dictionary
        riskColors[BodyMeasuresReading.BMILevel.UNDERWEIGHT] = resources.getColor(android.R.color.holo_orange_dark)
        riskColors[BodyMeasuresReading.BMILevel.HEALTHY] = resources.getColor(android.R.color.holo_green_dark)
        riskColors[BodyMeasuresReading.BMILevel.OVERWEIGHT] = resources.getColor(android.R.color.holo_orange_dark)
        riskColors[BodyMeasuresReading.BMILevel.OBESE] = resources.getColor(android.R.color.holo_red_dark)

        // Sets the onClick listeners
        registerButton.setOnClickListener { this.registerBodyMeasures() }
        randomSwitch.setOnClickListener { this.switchChanged() }
        averageCard.setOnClickListener { this.showRecords() }

        // Sets up a text changed listener to the weight input text field (for enabling/disabling the insert button)
        insertedWeight.addTextChangedListener(this)
        insertedWeight.setOnEditorActionListener(this)

        // Sets up the observer for the ViewModel data
        viewModel.averageWeightHeight.observe(viewLifecycleOwner, Observer<AverageBodyMeasures> { avgValues ->
            // Update the weight and height mean
            this.updateMean(avgValues.weight, avgValues.height)
        })

    }

    //endregion

    //region Data reading and writing

    /**
     * Register the weight and height of the user into the DB.
     * Launched when the "Register value" button is clicked.
     */
    private fun registerBodyMeasures(){
        val newBodyMeasures = if(randomSwitch.isChecked){
            // Inserts a random weight
            BodyMeasuresReading()
        }else {
            // Registers the inserted weight
            val myWeight = insertedWeight.text.toString().toFloat()
            val myHeight = insertedHeight.text.toString().toInt()
            BodyMeasuresReading(
                myWeight,
                myHeight
            )
        }

        // Clears the input
        insertedWeight.text.clear()
        insertedHeight.text.clear()

        // Compares the record with the mean
        val avgWeight = viewModel.averageWeightHeight.value?.weight
        val weightComparison = if(newBodyMeasures.weight > avgWeight!!) R.string.weight_higher else R.string.weight_lower

        // Notifies the user about the comparison of the introduced weight with the average
        Toast.makeText(context, weightComparison, Toast.LENGTH_LONG).show()

        // Registers the new record in the DB
        viewModel.insertRecord(newBodyMeasures)

        // The mean will be automatically updated by the ViewModel

    }

    /**
     * Update the displayed mean values.
     * This method is automatically called by the ViewModel observer.
     */
    @SuppressLint("SetTextI18n")
    private fun updateMean(avgWeight: Float, avgHeight: Int){
        val avgBmi = BodyMeasuresReading.bmi(avgWeight, avgHeight)

        // Update the UI with the new values
        val bmiColor = riskColors[BodyMeasuresReading.bmiLevel(avgBmi)] ?: resources.getColor(android.R.color.holo_purple)
        averageWeight.text = "%.1f".format(avgWeight)
        averageHeight.text = avgHeight.toString()
        averageBmi.text = if (avgBmi.isNaN()) "–" else "%.1f".format(avgBmi)
        averageWeight.setTextColor(bmiColor)
        averageHeight.setTextColor(bmiColor)
        averageBmi.setTextColor(bmiColor)

    }

    /**
     * Open a BodyMeasuresDetailActivity for showing a list with all the records.
     */
    private fun showRecords(){
        val intent = Intent(context, BodyMeasuresDetailActivity::class.java)
        startActivity(intent)
    }

    //endregion


    //region User interaction
    /**
     * Enables or disables the input text fields.
     * Launched when the "random values" switch is toggled.
     */
    private fun switchChanged(){
        insertedWeight.isEnabled = !(randomSwitch.isChecked)
        registerButton.isEnabled = randomSwitch.isChecked || insertedWeight.text.isNotEmpty()

    }

    override fun afterTextChanged(s: Editable?) {
        // Nothing to do
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Nothing to do
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Check if there is something typed on the text field, to enable or disable the register button
        registerButton.isEnabled = randomSwitch.isChecked || s!!.isNotEmpty()
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if(actionId == EditorInfo.IME_ACTION_GO){ // if the user presses enter button
            // Registers the weight
            registerBodyMeasures()
        }
        return true
    }

    //endregion
}
