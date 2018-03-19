package com.example.royok.finalproject

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.support.v4.app.ActivityCompat


/**
 * Created by Michael Gabbay on 16/03/2018.
 */
class locationHandler : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{

    lateinit var context:Context


    // permission vars
    public  val MY_PERMISSIONS_REQUEST_LOCATION :Int=1

    public val GPS_LOCATION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)

    //LOCATION VARS
    private var mLocationRequest: LocationRequest?= null
    var mGoogleApiClient : GoogleApiClient?=null

    var latitude :Double =0.0
    var longtitude:Double = 0.0

    fun initContext(ctx:Context){context = ctx}

    override fun onConnected(p0: Bundle?) {

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            requestPermission()
        else
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this)

            var lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
            if(lastLocation == null)
                return
            latitude = lastLocation.latitude
            longtitude = lastLocation.longitude
//            setGeoLocationValues()
        }

    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(location: Location) {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            requestPermission()
        else {
            latitude = location.latitude
            longtitude = location.longitude
//            setGeoLocationValues()
        }
    }


    //....................................................
    // Location functions
    fun initLocation(ctx:Context)
    {
        mGoogleApiClient = GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        createLocationRequest()
    }

    // attach onLocationChanged listener with interval and smallest displacement
    private fun createLocationRequest()
    {
        mLocationRequest = LocationRequest()
                .setInterval(10 * 100)
                .setFastestInterval(30*100)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }



    // Permission ask functions
    fun requestPermission()
    {
        // request location permission
        if(!(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as MainActivity, GPS_LOCATION, MY_PERMISSIONS_REQUEST_LOCATION)
        }

    }




}