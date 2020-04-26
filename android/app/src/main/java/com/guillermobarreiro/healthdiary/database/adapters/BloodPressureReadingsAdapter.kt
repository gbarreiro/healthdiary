package com.guillermobarreiro.healthdiary.database.adapters

import android.content.Context
import java.text.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guillermobarreiro.healthdiary.R
import com.guillermobarreiro.healthdiary.database.entities.BloodPressureReading


/**
 * [RecyclerView.Adapter] for [BloodPressureReading] objects.
 * Allows the display of the info from a [BloodPressureReading] on a cell in a list ([RecyclerView]).
 */
class BloodPressureReadingsAdapter(private val context: Context): RecyclerView.Adapter<BloodPressureReadingsAdapter.RecordViewHolder>() {

    //region Date formatters
    private val dayFormatter: DateFormat = android.text.format.DateFormat.getMediumDateFormat(context)
    private val hourFormatter: DateFormat = android.text.format.DateFormat.getTimeFormat(context)
    //endregion

    //region Risk colors
    private val riskColors = mutableMapOf<BloodPressureReading.RiskLevel, Int>().apply {
        // Sets up the risk colors dictionary
        this[BloodPressureReading.RiskLevel.NORMAL] = context.resources.getColor(android.R.color.holo_green_dark)
        this[BloodPressureReading.RiskLevel.ELEVATED] = context.resources.getColor(android.R.color.holo_orange_light)
        this[BloodPressureReading.RiskLevel.HIGH] = context.resources.getColor(android.R.color.holo_orange_dark)
        this[BloodPressureReading.RiskLevel.HYPERTENSIVE] = context.resources.getColor(android.R.color.holo_red_dark)
    }
    //endregion

    //region Data source
    private lateinit var bloodPressureRecords: List<BloodPressureReading>

    /**
     * Update the data source and refresh the list.
     * This method is called by the responsible ViewModel observer.
     */
    fun setDataSource(readings: List<BloodPressureReading>){
        this.bloodPressureRecords = readings
        notifyDataSetChanged()
    }

    //endregion


    /*  Provide a reference to the views for each data item
        Complex data items may need more than one view per item, and
        you provide access to all the views for a data item in a view holder
    */
    class RecordViewHolder(view: View): RecyclerView.ViewHolder(view){
        val systolicValue: TextView = view.findViewById(R.id.cell_systolic_value)
        val diastolicValue: TextView = view.findViewById(R.id.cell_diastolic_value)
        val recordDateDay: TextView = view.findViewById(R.id.cell_blood_pressure_day)
        val recordDateHour: TextView = view.findViewById(R.id.cell_blood_pressure_hour)

    }

    //region Adapter methods

    // Create a new view (cell) (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        // create a new cell
        val cellView = LayoutInflater.from(parent.context).inflate(R.layout.cell_blood_pressure_reading, parent, false)
        return RecordViewHolder(
            cellView
        )
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        // Get element from your dataset at this position
        val record = bloodPressureRecords[position]

        // Fill the UI with the reading data
        val diastolic = record.diastolic.toString() + " mmHg"
        val systolic = record.systolic.toString() + " mmHg"
        val day = dayFormatter.format(record.timestamp)
        val hour = hourFormatter.format(record.timestamp)
        val riskColor = riskColors[record.riskLevel] ?: context.resources.getColor(android.R.color.black)

        holder.diastolicValue.text = diastolic
        holder.systolicValue.text = systolic
        holder.diastolicValue.setTextColor(riskColor)
        holder.systolicValue.setTextColor(riskColor)
        holder.recordDateDay.text = day
        holder.recordDateHour.text = hour

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = bloodPressureRecords.size

    //endregion

}