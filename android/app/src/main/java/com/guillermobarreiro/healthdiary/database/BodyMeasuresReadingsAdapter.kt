package com.guillermobarreiro.healthdiary.database

import android.content.Context
import java.text.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guillermobarreiro.healthdiary.R

/**
 * RecyclerViewAdapter for BodyMeasuresReading objects.
 * Allows the display of the info from a BodyMeasuresReading on a cell in a list (RecyclerView).
 */
class BodyMeasuresReadingsAdapter(private val context: Context, private val database: HealthDatabase): RecyclerView.Adapter<BodyMeasuresReadingsAdapter.RecordViewHolder>() {

    //region Date formatters
    private val dayFormatter: DateFormat = android.text.format.DateFormat.getMediumDateFormat(context)
    private val hourFormatter: DateFormat = android.text.format.DateFormat.getTimeFormat(context)
    //endregion

    //region Risk colors
    private val riskColors = mutableMapOf<BodyMeasuresReading.BMILevel, Int>().apply {
        // Sets up the risk colors dictionary
        this[BodyMeasuresReading.BMILevel.HEALTHY] = context.resources.getColor(android.R.color.holo_green_dark)
        this[BodyMeasuresReading.BMILevel.UNDERWEIGHT] = context.resources.getColor(android.R.color.holo_orange_dark)
        this[BodyMeasuresReading.BMILevel.OVERWEIGHT] = context.resources.getColor(android.R.color.holo_orange_dark)
        this[BodyMeasuresReading.BMILevel.OBESE] = context.resources.getColor(android.R.color.holo_red_dark)
    }
    //endregion

    //region Data source
    private val bodyMeasuresRecords: List<BodyMeasuresReading> = database.bodyMeasuresDao().getRecords()
    //endregion


    /*  Provide a reference to the views for each data item
        Complex data items may need more than one view per item, and
        you provide access to all the views for a data item in a view holder
    */
    class RecordViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val weightValue: TextView = view.findViewById(R.id.cell_weight_value)
        val heightValue: TextView = view.findViewById(R.id.cell_height_value)
        val bmiValue: TextView = view.findViewById(R.id.cell_bmi_value)
        val recordDateDay: TextView = view.findViewById(R.id.cell_body_measure_day)
        val recordDateHour: TextView = view.findViewById(R.id.cell_body_measure_hour)

    }

    //region Adapter methods

    // Create a new view (cell) (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        // create a new cell
        val cellView = LayoutInflater.from(parent.context).inflate(R.layout.cell_body_measures_reading, parent, false)
        return RecordViewHolder(cellView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        // Get element from your dataset at this position
        val record = bodyMeasuresRecords[position]

        // Fill the UI with the reading data
        val weight = "%.1f".format(record.weight) + " kg"
        val height = record.height.toString() + " cm"
        val bmi = "%.1f".format(record.bmi)
        val day = dayFormatter.format(record.timestamp)
        val hour = hourFormatter.format(record.timestamp)
        val riskColor = riskColors[record.bmiLevel] ?: context.resources.getColor(android.R.color.black)

        holder.weightValue.text = weight
        holder.heightValue.text = height
        holder.bmiValue.text = bmi
        holder.weightValue.setTextColor(riskColor)
        holder.heightValue.setTextColor(riskColor)
        holder.bmiValue.setTextColor(riskColor)
        holder.recordDateDay.text = day
        holder.recordDateHour.text = hour

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = bodyMeasuresRecords.size

    //endregion

}