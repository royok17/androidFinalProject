package com.example.royok.finalproject

import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.app.ProgressDialog.show
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.coroutines.experimental.async
import java.io.IOException
import java.net.URL;
import java.util.*

class MainActivity : Activity(){

    val locationHandler : locationHandler = locationHandler()

    // data values
    var sData : ShabatData = ShabatData()
    var countryCod = ""
    var tzID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationHandler.initContext(this)
        locationHandler.requestPermission()
        locationHandler.initLocation(this)

        startBtn.setOnClickListener()
        {
            val dialog = show(this, "",
                    "Loading Data...", true)
            async {
                var req = buildRequest()
                if(req == "") {
//                    locationHandler.initLocation(baseContext)
                    dialog.cancel()
                }
                try
                {
                    val result = URL("\t\n" + req).readText()
                    val stringBuilder = StringBuilder(result)
                    val parser = Parser()
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject
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
//                                textView.text = parasha+"\n"+candleTime+"\n"+havdala
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
            var addresses = gcd.getFromLocation(locationHandler.latitude, locationHandler.longtitude, 1)

            if (addresses.size > 0) {
                System.out.println(addresses.get(0).getLocality())
                countryCod = addresses.get(0).countryCode
                if(countryCod == "IL")
                    sData.city = convertCityName(addresses.get(0).getLocality())
                else
                    sData.city = addresses.get(0).getLocality()
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
        if(lat == 0.0 || long == 0.0)
        {
            Log.v("REQUEST","location data or country cod missing")
            return ""
        }
        setGeoLocationValues()
        if(countryCod == "IL" && sData.city != "")
        {
            var reqCity = countryCod+"-"+sData.city
            return "http://www.hebcal.com/shabbat/?cfg=json&geo=city&city="+reqCity+"&m=50&b=18"
        }
        else
        {
            if (lat != 0.0 && long!= 0.0) {
                getTimezID()
                if (tzID != "")
                    return "http://www.hebcal.com/shabbat/?cfg=json&geo=pos&latitude=" + lat + "&longitude=" + long + "&tzid=" + tzID + "m=50&b=35"
                else
                    return ""
            }
        }
        return ""
    }

    // for global location request
    private fun getTimezID()
    {
        var tzReq = "http://api.geonames.org/timezoneJSON?lat="+locationHandler.latitude+"&lng="+locationHandler.longtitude+"&username=michaelga"
        val result = URL("\t\n" + tzReq).readText()
        val stringBuilder = StringBuilder(result)
        val parser = Parser()
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        tzID = json.get("timezoneId") as String
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
        }
        return sData.city
    }


}
