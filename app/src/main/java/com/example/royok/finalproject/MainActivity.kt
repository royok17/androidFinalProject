package com.example.royok.finalproject

import android.Manifest
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.app.ProgressDialog.show
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.CellSignalStrength
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.async
import java.io.IOException
import java.net.URL;
import java.util.*
import java.nio.file.Files.size




class MainActivity : Activity(),GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {


    // permission vars
    private val MY_PERMISSIONS_REQUEST_LOCATION :Int=1
    private val GPS_COARSE_LOCATION_REQ : Int = 2

    private val GPS_LOCATION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
    private val GPS_COARSE_LOCATION = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)



    //LOCATION VARS
     var mLocationRequest: LocationRequest ?= null
    var mGoogleApiClient : GoogleApiClient?=null
    lateinit var mCellSignalStrength: CellSignalStrength

    var latitude :Double =0.0
    var longtitude:Double = 0.0
    //.......................................................
    // data values
    var parasha:String=""
    var candleTime:String =""
    var havdala:String =""
    var city = ""
    var reqCity = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()
        initLocation()
        startBtn.setOnClickListener()
        {
            val dialog = show(this, "",
                    "Loading Data...", true)
            async {
                var req = getRequest()
                if(req == "")
                    return@async
                val result = URL("\t\n" + req).readText()
                val stringBuilder = StringBuilder(result)
                val parser = Parser()
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                val items : JsonArray<JsonObject> = json.get("items") as JsonArray <JsonObject>
                for (i in 0..items.size-1)
                {
                    if(items[i].containsValue("candles"))
                    {
                        candleTime = items[i].getValue("title") as String
                    }
                    else if (items[i].containsValue("parashat"))
                    {
                        parasha = items[i].getValue("hebrew") as String
                    }
                    else if (items[i].containsValue("havdalah"))
                    {
                        havdala = items[i].getValue("title") as String
                    }
                }

                textView.post(
                        {
                            dialog.cancel()
                            textView.text = parasha+"\n"+candleTime+"\n"+havdala
                        })
                createLocationRequest()
            }

        }
    }

    // Permission functions
    fun requestPermission()
    {
        // request location permission
        if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, GPS_LOCATION, MY_PERMISSIONS_REQUEST_LOCATION)
        }

    }
    //TODO:: add permission callback function to check if permission is granted



    //....................................................
    // Location functions
    private fun initLocation()
    {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        createLocationRequest()
    }

    // attach onLocationChanged listener with interval and smallest displacement
    fun createLocationRequest()
    {
        mLocationRequest = LocationRequest()
                .setInterval(10 * 100)
                .setFastestInterval(30*100)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }
    // interface functions

    override fun onConnected(p0: Bundle?) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            requestPermission()
        else
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this)

            var lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
            if(lastLocation == null)
                return
            latitude = lastLocation.latitude
            longtitude = lastLocation.longitude
            val tz = TimeZone.getDefault()
            var tzid = tz.id
            var gcd : Geocoder = Geocoder(baseContext, Locale.getDefault())
            try {
                var addresses = gcd.getFromLocation(latitude, longtitude, 1)
                if (addresses.size > 0) {
                    System.out.println(addresses.get(0).getLocality())
                    var cityName = addresses.get(0).getLocality()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(location: Location) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            requestPermission()
        else {

            latitude = location.latitude
            longtitude = location.longitude

            var gcd: Geocoder = Geocoder(baseContext, Locale.getDefault())
            try {
                var addresses = gcd.getFromLocation(latitude, longtitude, 1)
                if (addresses.size > 0) {
                    System.out.println(addresses.get(0).getLocality())
                    city = addresses.get(0).getLocality()
                    var countryCode = addresses.get(0).countryCode
                    reqCity = countryCode+"-"+city
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    //  End Of Location Functions
    //.................................................................................

    //Life Cycle Functions /////////////////////////////////////////////////////
    //*************************************************************************
    override fun onStart() {
        super.onStart()
        mGoogleApiClient?.connect()
    }

    override fun onStop()
    {
        if(mGoogleApiClient!=null)
        {
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this)
            mGoogleApiClient?.disconnect()
        }
        super.onStop()
    }
    fun getRequest() :String{
        if(reqCity != "")
            return "http://www.hebcal.com/shabbat/?cfg=json&geo=city&city="+reqCity+"&m=50&b=18"
        else return ""
    }
}
