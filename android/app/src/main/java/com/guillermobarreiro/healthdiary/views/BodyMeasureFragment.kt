package com.guillermobarreiro.healthdiary.views

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
import com.guillermobarreiro.healthdiary.R
import com.guillermobarreiro.healthdiary.database.HealthDatabase
import com.guillermobarreiro.healthdiary.database.BodyMeasureReading

class BodyMeasureFragment : Fragment(), TextWatcher, TextView.OnEditorActionListener{

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
    private var meanValues: BodyMeasureReading? = null
    private lateinit var db: HealthDatabase
    //endregion

    private val riskColors = mutableMapOf<BodyMeasureReading.BMILevel, Int>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflates the layout for this fragment
        return inflater.inflate(R.layout.fragment_body_measure, container, false)
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
        riskColors[BodyMeasureReading.BMILevel.UNDERWEIGHT] = resources.getColor(android.R.color.holo_orange_dark)
        riskColors[BodyMeasureReading.BMILevel.HEALTHY] = resources.getColor(android.R.color.holo_green_dark)
        riskColors[BodyMeasureReading.BMILevel.OVERWEIGHT] = resources.getColor(android.R.color.holo_orange_dark)
        riskColors[BodyMeasureReading.BMILevel.OBESE] = resources.getColor(android.R.color.holo_red_dark)

        // Sets the onClick listeners
        registerButton.setOnClickListener { this.registerWeight() }
        randomSwitch.setOnClickListener { this.switchChanged() }
        averageCard.setOnClickListener { this.showRecords() }

        // Sets up a text changed listener to the weight input text field (for enabling/disabling the insert button)
        insertedWeight.addTextChangedListener(this)
        insertedWeight.setOnEditorActionListener(this)

        // Sets up the DB connection
        db = HealthDatabase(context!!)

        // Gets the last mean values and shows them in the UI
        updateMean()

    }

    // Launched when the "Register value" button is clicked
    private fun registerWeight(){
        val lastBodyMeasure: BodyMeasureReading?
        if(randomSwitch.isChecked){
            // Inserts a random weight
            lastBodyMeasure = BodyMeasureReading()
        }else {
            // Registers the inserted weight
            val myWeight = insertedWeight.text.toString().toFloat()
            val myHeight = insertedHeight.text.toString().toInt()
            lastBodyMeasure = BodyMeasureReading(myWeight, myHeight)
        }

        // Clears the input
        insertedWeight.text.clear()
        insertedHeight.text.clear()

        // Compares the record with the mean
        if(meanValues != null){
            val weightComparison = if(lastBodyMeasure.weight > meanValues!!.weight) R.string.weight_higher else R.string.weight_lower

            // Notifies the user about the comparison of the introduced weight with the average
            Toast.makeText(context, weightComparison, Toast.LENGTH_LONG).show()
        }

        // Registers the new record in the DB
        db.insertRecord(lastBodyMeasure, HealthDatabase.DatabaseTables.WEIGHT)

        // Updates the mean values
        updateMean()


    }

    private fun updateMean(){
        meanValues = db.calculateBodyMeasureMean()
        if(meanValues!=null){
            val avgWeight = "%.1f".format(meanValues!!.weight)
            val avgHeight = meanValues!!.height.toString()
            val avgBmi = "%.1f".format(meanValues!!.bmi)
            val bmiColor = riskColors[meanValues!!.bmiLevel] ?: resources.getColor(android.R.color.holo_purple)
            averageWeight.text = avgWeight
            averageHeight.text = avgHeight
            averageBmi.text = avgBmi
            averageBmi.setTextColor(bmiColor)
        }

    }

    private fun showRecords(){
        val intent = Intent(context, BodyMeasuresDetailActivity::class.java)
        startActivity(intent)
    }

    // Launched when the "random values" switch is toggled
    private fun switchChanged(){
        // Enables or disables the insertedWeight text field
        insertedWeight.isEnabled = !(randomSwitch.isChecked)
        registerButton.isEnabled = randomSwitch.isChecked || insertedWeight.text.isNotEmpty()

    }

    override fun onDestroy() {
        // Removes the connection to the DB
        db.close()
        super.onDestroy()
    }


    //region TextWatcher & EditorAction methods

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
            registerWeight()
        }
        return true
    }

    //endregion
}
