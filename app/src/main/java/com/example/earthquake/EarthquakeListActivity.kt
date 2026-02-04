package com.example.earthquake

import android.os.Bundle
import android.util.Log
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

class EarthquakeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEarthquakeListBinding
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

                    var customAdapter = EarthquakeAdapter(featureCollection.features)
                    binding.recyclerViewEarthquakeList.layoutManager = LinearLayoutManager(this@EarthquakeListActivity)
                    binding.recyclerViewEarthquakeList.adapter = customAdapter

                    val recyclerView: RecyclerView = findViewById(R.id.recyclerView_earthquakeList)
                    recyclerView.layoutManager = LinearLayoutManager(this@EarthquakeListActivity)
                    recyclerView.adapter = customAdapter
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
}