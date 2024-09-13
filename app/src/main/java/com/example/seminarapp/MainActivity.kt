package com.example.seminarapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationTracker()

    }

    private fun locationTracker() {
        val fusedLocationProvider : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            requestToLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION))

            return
        }

        fusedLocationProvider.lastLocation.addOnSuccessListener { location->
            if (location!=null){
                println("MainActivity FusedLocationProviderClient >>> Latitude >> ${location.latitude}")
                println("MainActivity FusedLocationProviderClient >>> Longitude >> ${location.longitude}")
                getGeoLocation(location.latitude,location.longitude)
            }
        }

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        println("MainActivity FusedLocationProviderClient >>> is Gps Enabled >> $isGPS")
    }

    private fun getGeoLocation(latitude: Double, longitude: Double) {
        val geoCoder = Geocoder(this, Locale.getDefault())
         val address = geoCoder.getFromLocation(latitude,longitude,1)
        println("MainActivity FusedLocationProviderClient >>> address >> $address")
    }

    private val requestToLauncher =  registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ isGrant ->

        println("MainActivity FusedLocationProviderClient >>> Permission >> $isGrant")

    }
}