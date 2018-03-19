package com.example.royok.finalproject

import android.Manifest
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.app.ProgressDialog.show
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.coroutines.experimental.async
import java.io.IOException
import java.net.URL;
import java.util.*

class MainActivity : Activity(){

    val locationHandler : locationHandler = locationHandler()

    enum class reqType {NONE,FromCity,FromDistrict}
    // data values
    var sData : ShabatData = ShabatData()
    var mReqType : reqType = reqType.NONE
    var reqCity :String =""
    var countryCod = ""
    var permission = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationHandler.initContext(this)
        requestPermission()
        locationHandler.initLocation(this)

        startBtn.setOnClickListener()
        {
            requestPermission()
            if (! permission)
                Toast.makeText(this,"location permission invalid!\ndefault location will be loaded",Toast.LENGTH_SHORT).show()
            val dialog = show(this, "",
                    "Loading Data...", true)
            async {
                var req = buildRequest()

                Log.v("requestCity: " ,reqCity)

                if(req == "") {
                    dialog.cancel()
                }
                try
                {
                    val result = URL("\t\n" + req).readText()
                    val stringBuilder = StringBuilder(result)
                    val parser = Parser()
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                    val dateStr : String = json.get("date") as String
                    var dateReq = buildHebrewDateReq(dateStr)

                    val items : JsonArray<JsonObject> = json.get("items") as JsonArray <JsonObject>
                    for (i in 0..items.size-1)
                    {
                        if(items[i].containsValue("candles"))
                        {
                            sData.candletime = items[i].getValue("title") as String
                            sData.candletime = parseStrings(sData.candletime)
                        }
                        else if (items[i].containsValue("parashat"))
                        {
                            sData.parasha = items[i].getValue("hebrew") as String
                        }
                        else if (items[i].containsValue("havdalah"))
                        {
                            sData.havdala = items[i].getValue("title") as String
                            sData.havdala = parseStrings(sData.havdala)
                        }
                    }
                    textView.post(
                            {
                                dialog.cancel()
                                if(sData.candletime != "" && sData.havdala != "" && sData.parasha != "")
                                    startTabMenu()
                            })
                    }

                catch (err:Exception){
                    dialog.cancel()
                    Log.v("req","parsing json failed, bad request")}
            }
        }
    }

    // init the next activity with the requerred information
    fun startTabMenu()
    {
        var mIntent = Intent(this,Main2Activity::class.java)
        sData.city = getHebrewCity()
        mIntent.putExtra("sData",sData)
        startActivity(mIntent)
    }


    private fun setGeoLocationValues()
    {
        var gcd: Geocoder = Geocoder(baseContext, Locale.ENGLISH)
        try {
            var isGeoCoderExist = Geocoder.isPresent()
            var addresses = gcd.getFromLocation(locationHandler.latitude, locationHandler.longtitude, 10)


            if (addresses.size > 0) {
                countryCod = addresses.get(0).countryCode
                if(countryCod == "IL")
                    getValuesFromAddress(addresses)
                else
                    mReqType = reqType.NONE
            }
            locationHandler.mGoogleApiClient?.disconnect()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    //Life Cycle Functions /////////////////////////////////////////////////////
    //*************************************************************************
    override fun onStart() {
        super.onStart()
        locationHandler.mGoogleApiClient?.connect()
    }

    override fun onStop()
    {
        if(locationHandler.mGoogleApiClient!=null)
        {
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this)
            locationHandler.mGoogleApiClient?.disconnect()
        }
        super.onStop()
    }


    // LOCAL HELPERS.........................
    //......................................
    // builds the correct request according to the location taken
    fun buildRequest() :String {
        var lat = locationHandler.latitude
        var long = locationHandler.longtitude
        setGeoLocationValues()

        Log.v("reqTYPE",mReqType.toString())
        if(mReqType == reqType.NONE )
        {
            Log.v("REQUEST","location data or country cod missing")
            sData.city = getString(R.string.defaultLocation)
            reqCity  = "IL-"+sData.city
            mReqType = reqType.FromCity
//            return "http://www.hebcal.com/shabbat/?cfg=json&geo=city&city="+reqCity+"&m=50&b=18"
        }

        if(mReqType == reqType.FromCity || mReqType == reqType.FromDistrict)
        {
            return "http://www.hebcal.com/shabbat/?cfg=json&geo=city&city="+reqCity+"&m=50&b=18"
        }
        else
            return ""
    }

    private fun getValuesFromAddress( addresses : List <Address>)
    {
        var tmpCity :String
        var tmpRegion : String

        var j: Int =0
        sData.city = convertCityName(addresses.get(0).locality)// check if city name is equal to API DB
        markCityExist()

        if (reqCity != "")
            return
        else     // get city from geographical district
        {
            var distritcts = application.resources.getStringArray(R.array.districtNames)
            var districtName : String = ""
            for (i in 0..addresses.size-1)  // set city
            {
                if (addresses.get(i)?.adminArea != null) {
                    districtName = addresses.get(i)?.adminArea
                    break;
                }
            }
            for (j in 0..distritcts.size - 1)
            {
                if (distritcts.get(j).equals(districtName))
                {
                    mReqType = reqType.FromDistrict
                    reqCity = countryCod + "-" + distritcts.get(j + 1)       // distritcts get(i+1) returns the city according to the district
                    break
                }
            }

        }
    }

//        if(mReqType == reqType.NONE)
//        {
//            sData.city = Resources.getSystem().getString(R.string.defaultLocation)
//            reqCity  = countryCod+"-"+sData.city
//            mReqType = reqType.FromCity
//        }
//    }

    // set boolean flag to mark that city is exist
    private fun markCityExist()
    {
        var cities = application.resources.getStringArray(R.array.citiesDB)
        for (i in 0..cities.size-1)
        {
            if(cities.get(i) == sData.city)
            {
                mReqType = reqType.FromCity
                reqCity = countryCod+"-"+sData.city
                return
            }
        }
    }





    // adjustments for the API accepted strings
    private fun convertCityName(city : String):String
    {
        when (city){
            "Modi'in-Maccabim-Re'ut" -> return "Modiin"
            "Bet Shemesh" ->  return "Beit Shemesh"
            "Kefar Sava" -> return "Kfar Saba"
            "Petah Tikva" -> return "Petach Tikvah"
            "Rishon LeTsiyon" -> return "Rishon LeZion"
        }

        return city
    }

    // extract the time values from the answere recieved from the API
    private fun parseStrings(src : String ) : String
    {
        var tmp : String = ""
        var x =0;
        var end = src.length -2
        if(src.contains("Candle lighting"))
        {
            x = src.indexOf(":")+2
            return src.substring(x,end)
        }
        if (src.contains("Havdalah"))
        {
            x= src.indexOf(":")+2
            return src.substring(x,end)
        }
        return ""
    }

    // this function converts the city name to hebrew in case the city is from Israel
    fun getHebrewCity() : String
    {
        when (sData.city){
            "Jerusalem" -> return "ירושלים"
            "Tel Aviv" -> return  "תל אביב"
            "Haifa" -> "חיפה"
            "Modiin" -> return "מודיעין"
            "Beit Shemesh" -> return "בית שמש"
            "Kfar Saba" -> return "כפר סבא"
            "Petach Tikvah" -> return "פתח תקווה"
            "Rishon LeZion" -> return "ראשון לציון"
            "Ashdod" -> return "אשדוד"
            "Ashkelon"-> return "אשקלון"
            "Bat Yam"-> return "בת ים"
            "Be'er Sheva"-> return "באר שבע"
            "Bnei Brak"-> return "בני ברק"
            "Eilat"-> return "אילת"
            "Hadera"-> return "חדרה"
            "Herzliya"-> return "הרצליה"
            "Holon"-> return "חולון"
            "Kfar Saba"-> return "כפר סבא"
            "Lod"-> return "לוד"
            "Nazareth"-> return "נצרת"
            "Netanya"-> return "נתניה"
            "Ra'anana"-> return "רעננה"
            "Ramat Gan"-> return "רמת גן"
            "Ramla"-> return "רמלה"
            "Tiberias"-> return "טבריה"
        }
        return sData.city
    }

    private fun buildHebrewDateReq(dateStr:String) : String
    {


        return ""
    }


    // Permission ask functions
    fun requestPermission()
    {
        // request location permission
        if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, locationHandler.GPS_LOCATION, locationHandler.MY_PERMISSIONS_REQUEST_LOCATION)
        }
        else
            permission = true

    }

    // handle permission answere
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        when (requestCode)
        {
            locationHandler.MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    locationHandler.initLocation(this)
                    setGeoLocationValues()
                    permission = true
                } else
                {

                }
                return
            }
        }
    }

}
