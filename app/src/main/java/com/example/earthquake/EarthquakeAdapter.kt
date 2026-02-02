package com.example.earthquake

import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout



class EarthquakeAdapter(var earthquakeList : List<Feature>) :
    RecyclerView.Adapter<EarthquakeAdapter.ViewHolder>(){

    companion object{
        val EXTRA_EARTHQUAKE = "earthquake"
    }
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

        // Get element from your heroList at this position and replace the
        // contents of the view with that element
        val earthquake = earthquakeList[position]
        val context = viewHolder.layout.context

        viewHolder.textViewMagnitude.text = earthquake.properties.mag.toString()
        viewHolder.textViewTime.text = earthquake.properties.time.toString()
        viewHolder.textViewLocation.text = earthquake.properties.place
        viewHolder.layout.setOnClickListener {
            val intent = Intent(context, EarthquakeMapActivity::class.java)
            intent.putExtra(EarthquakeMapActivity.EXTRA_EARTHQUAKE, earthquake)
            Toast.makeText(context, "${hero.name} clicked!", Toast.LENGTH_SHORT).show()
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
