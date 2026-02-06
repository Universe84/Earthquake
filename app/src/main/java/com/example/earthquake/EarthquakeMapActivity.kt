package com.example.earthquake
import java.text.SimpleDateFormat
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

import java.util.ArrayList
import java.util.Date
import java.util.Locale

class EarthquakeMapActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    companion object{
        val EXTRA_EARTHQUAKE = "earthquake"
    }
    private lateinit var map : MapView
    private lateinit var firstMarker : Marker
    private lateinit var geoPoint : GeoPoint

    private lateinit var textViewTitle: TextView
    private lateinit var textViewUrl: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val earthquaked = intent.getParcelableExtra(EXTRA_EARTHQUAKE) ?: Feature(Properties(20.0, "hi","apples",100,"abc.com"), Geometry(listOf(1.0,2.0,3.0)))

        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        // This won't work unless you have imported this: org.osmdroid.config.Configuration.*
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, if you abuse osm's
        //tile servers will get you banned based on this string.

        //inflate and create the map
        setContentView(R.layout.activity_earthquake_map)
        wireWidgets()
        map = findViewById<MapView>(R.id.map)

        map.setTileSource(TileSourceFactory.MAPNIK)

        textViewTitle.text = "${earthquaked.properties.place}"
        textViewUrl.text = "${earthquaked.properties.url}"


        geoPoint = GeoPoint(earthquaked.geometry.coordinates[1], earthquaked.geometry.coordinates[0])
        firstMarker = Marker(map)
        val mapController = map.controller
        mapController.setZoom(9.5)
        mapController.setCenter(geoPoint);
        firstMarker.position = geoPoint

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }
        firstMarker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_CENTER)
        firstMarker.icon = ContextCompat.getDrawable(this, R.drawable.marker_icon)

        firstMarker.title = "Magnitude: ${earthquaked.properties.mag} Location: ${earthquaked.properties.place} Time:${convertTimestamp(earthquaked.properties.time)}"
        map.overlays.add(firstMarker)
        map.invalidate()
    }

    fun convertTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }

    private fun wireWidgets(){
        textViewUrl = findViewById(R.id.textView_earthquakeMap_url)
        textViewTitle = findViewById(R.id.textView_earthquakeMap_title)

    }

    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause()  //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }


    /*private fun requestPermissionsIfNecessary(String[] permissions) {
        val permissionsToRequest = ArrayList<String>();
        permissions.forEach { permission ->
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            permissionsToRequest.add(permission);
        }
    }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }*/
}