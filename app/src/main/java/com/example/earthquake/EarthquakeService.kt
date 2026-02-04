package com.example.earthquake

import retrofit2.Call
import retrofit2.http.GET

interface EarthquakeService {
    //list out diff endpoints in api you want to call
    //function returns Call<type> where type is the data returned in the json
    //in the @get("blah"), "blah" is the path to the file (endpoint)

    @GET("summary/all_day.geojson")
    fun getEarthquakeDataPastDay() : Call<FeatureCollection>
}