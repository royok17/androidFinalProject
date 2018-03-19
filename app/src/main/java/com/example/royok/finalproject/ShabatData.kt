package com.example.royok.finalproject

import android.util.Log
import java.io.Serializable

/**
 * Created by Michael Gabbay on 16/03/2018.
 */
class ShabatData : Serializable {
    lateinit var parasha :String
    lateinit var havdala :String
    lateinit var candletime :String
    lateinit var city :String
    lateinit var hebrewDate : String

    fun initData(par:String,hav:String,can:String,cit:String,date:String)
    {
        parasha = par
        havdala = hav
        candletime = can
        city = cit
        hebrewDate = date
    }


}