package com.example.earthquake

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.earthquake.databinding.ActivityEarthquakeListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.app.AlertDialog

class EarthquakeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEarthquakeListBinding
    private lateinit var filteredFeatures : List<Feature>
    companion object{
        val TAG : String = "EarthquakeList"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEarthquakeListBinding.inflate(layoutInflater)


        setContentView(binding.root)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



// [espresso + milk]r's counterpart


        val earthquakeService = RetrofitHelper.getInstance().create(EarthquakeService::class.java)
        val earthquakeCall = earthquakeService.getEarthquakeDataPastDay()



        earthquakeCall.enqueue(object: Callback<FeatureCollection> {
            override fun onResponse(
                call: Call<FeatureCollection?>,
                response: Response<FeatureCollection?>
            ) {
                val featureCollection = response.body()
                Log.d(TAG, "onResponse: ${featureCollection}")
                if(featureCollection != null){
                    filteredFeatures = featureCollection.features.filter { feature ->
                        feature.properties.mag >= 1
                    }

                    var customAdapter = EarthquakeAdapter(filteredFeatures)
                    binding.recyclerViewEarthquakeList.layoutManager = LinearLayoutManager(this@EarthquakeListActivity)
                    binding.recyclerViewEarthquakeList.adapter = customAdapter

                    customAdapter.earthquakeList = customAdapter.earthquakeList.sortedWith(compareBy({-it.properties.time}, {it.properties.mag}))
                    customAdapter.notifyDataSetChanged()


                }

            }



            override fun onFailure(
                call: Call<FeatureCollection?>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })




    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.sorter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection.
        when (item.itemId) {
            R.id.item_listOptions_sortByRecency -> {
                binding.recyclerViewEarthquakeList.adapter = EarthquakeAdapter(filteredFeatures.sortedByDescending { it.properties.time })
                true
            }
            R.id.item_listOptions_sortByMagnitude -> {
                binding.recyclerViewEarthquakeList.adapter = EarthquakeAdapter(filteredFeatures.sortedWith(compareByDescending<Feature> {it.properties.mag ?: 0.0}.thenByDescending {it.properties.time}))
                true
            }
            R.id.item_listOptions_help -> {
                AlertDialog.Builder(this).setMessage("Purple: Significant (> 6.5)\nRed: Large (4.5 - 6.5)\nOrange: Moderate (2.5 - 4.5)\nBlue: Small (1.0 - 2.5)\n\nThe number represents the magnitude.").setPositiveButton("OK", null).show()
            }

            else -> super.onOptionsItemSelected(item)
        }
        return true
    }
}