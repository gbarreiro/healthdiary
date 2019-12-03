package com.guillermobarreiro.healthdiary.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import com.guillermobarreiro.healthdiary.R
import com.guillermobarreiro.healthdiary.database.HealthDatabase
import com.guillermobarreiro.healthdiary.database.WeightReading

class WeightFragment : Fragment(), TextWatcher, TextView.OnEditorActionListener{

    //region Activity views
    private lateinit var randomSwitch: Switch
    private lateinit var insertedWeight: EditText
    private lateinit var registerButton: Button
    private lateinit var averageWeight: TextView
    //endregion

    private lateinit var meanValues: WeightReading
    private lateinit var db: HealthDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflates the layout for this fragment
        return inflater.inflate(R.layout.fragment_weight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sets up the view
        randomSwitch = view.findViewById(R.id.random_weight)
        insertedWeight = view.findViewById(R.id.inserted_weight)
        registerButton = view.findViewById(R.id.register_weight)
        averageWeight = view.findViewById(R.id.avg_weight)

        // Sets the onClick listeners
        registerButton.setOnClickListener { this.registerWeight() }
        randomSwitch.setOnClickListener { this.switchChanged() }

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
        val lastWeight: WeightReading?
        if(randomSwitch.isChecked){
            // Inserts a random weight
            lastWeight = WeightReading()
        }else {
            // Registers the inserted weight
            val myWeight = insertedWeight.text.toString().toFloat()
            lastWeight = WeightReading(myWeight)
        }

        // Clears the input
        insertedWeight.text.clear()

        // Compares the record with the mean
        val weightComparison = if(lastWeight.weight > meanValues.weight) R.string.weight_higher else R.string.weight_lower

        // Registers the new record in the DB
        db.insertRecord(lastWeight, HealthDatabase.DatabaseTables.WEIGHT)

        // Updates the mean values
        updateMean()

        // Notifies the user about the comparison of the introduced weight with the average
        Toast.makeText(context, weightComparison, Toast.LENGTH_LONG).show()
    }

    private fun updateMean(){
        meanValues = db.calculateWeightMean()
        val avgWeight = if(meanValues.weight > 0) "%.2f".format(meanValues.weight) else "–"
        averageWeight.text = avgWeight

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
