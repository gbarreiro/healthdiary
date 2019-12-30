package com.guillermobarreiro.healthdiary.database

import android.content.Context
import java.text.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guillermobarreiro.healthdiary.R

class BloodPressureReadingsAdapter(private val context: Context, private val database: HealthDatabase): RecyclerView.Adapter<BloodPressureReadingsAdapter.RecordViewHolder>() {

    private val dayFormatter: DateFormat = android.text.format.DateFormat.getMediumDateFormat(context)
    private val hourFormatter: DateFormat = android.text.format.DateFormat.getTimeFormat(context)

    private val bloodPressureRecords: Array<BloodPressureReading> = database.getBloodPressureRecords()
    private val riskColors = mutableMapOf<BloodPressureReading.RiskLevel, Int>().apply {
        // Sets up the risk colors dictionary
        this[BloodPressureReading.RiskLevel.NORMAL] = context.resources.getColor(android.R.color.holo_green_dark)
        this[BloodPressureReading.RiskLevel.ELEVATED] = context.resources.getColor(android.R.color.holo_orange_light)
        this[BloodPressureReading.RiskLevel.HIGH] = context.resources.getColor(android.R.color.holo_orange_dark)
        this[BloodPressureReading.RiskLevel.HYPERTENSIVE] = context.resources.getColor(android.R.color.holo_red_dark)
    }


    /*  Provide a reference to the views for each data item
        Complex data items may need more than one view per item, and
        you provide access to all the views for a data item in a view holder
    */
    class RecordViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val systolicValue: TextView = view.findViewById(R.id.cell_systolic_value)
        val diastolicValue: TextView = view.findViewById(R.id.cell_diastolic_value)
        val recordDateDay: TextView = view.findViewById(R.id.cell_blood_pressure_day)
        val recordDateHour: TextView = view.findViewById(R.id.cell_blood_pressure_hour)

    }

    // Create a new view (cell) (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        // create a new cell
        val cellView = LayoutInflater.from(parent.context).inflate(R.layout.cell_blood_pressure_reading, parent, false)
        return RecordViewHolder(cellView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val record = bloodPressureRecords[position]
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

}