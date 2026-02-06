package com.example.earthquake

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.media3.common.Format
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EarthquakeAdapter(var earthquakeList : List<Feature>) :
    RecyclerView.Adapter<EarthquakeAdapter.ViewHolder>(){


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewMagnitude: TextView
        val textViewLocation: TextView
        val textViewTime: TextView
        val layout: ConstraintLayout

        init {
            // Define click listener for the ViewHolder's View
            textViewMagnitude = view.findViewById(R.id.textView_itemEarthquake_magnitude)
            textViewLocation = view.findViewById(R.id.textView_itemEarthquake_location)
            textViewTime = view.findViewById(R.id.textView_itemEarthquake_time)
            layout = view.findViewById(R.id.layout_earthquakeItem)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_earthquake, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val decimalFormat = DecimalFormat("#.#")

        val earthquake = earthquakeList[position]
        val context = viewHolder.layout.context

        viewHolder.textViewMagnitude.text = decimalFormat.format(earthquake.properties.mag)

        viewHolder.textViewTime.text = convertTimestamp(earthquake.properties.time)
        viewHolder.textViewLocation.text = earthquake.properties.place
        viewHolder.layout.setOnClickListener {
            val intent = Intent(context, EarthquakeMapActivity::class.java)
            intent.putExtra(EarthquakeMapActivity.EXTRA_EARTHQUAKE, earthquake)
            //Toast.makeText(context, "${earthquake.name} clicked!", Toast.LENGTH_SHORT).show()
            context.startActivity(intent)
        }

    }

    fun convertTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }

    override fun getItemCount(): Int {
        return earthquakeList.size
    }
}
