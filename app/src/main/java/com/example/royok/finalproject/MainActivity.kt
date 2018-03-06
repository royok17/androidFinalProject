package com.example.royok.finalproject

import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.app.ProgressDialog.show
import android.os.Bundle
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.async
import java.net.URL;
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONArray


class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn.setOnClickListener()
        {
            val dialog = show(this, "",
                    "find location", true)
            async {
                val result = URL("\t\n" + "http://www.hebcal.com/shabbat/?cfg=json&geo=pos&latitude=31.771959&longitude=35.217018&tzid=Asia/Jerusalem&m=50&b=35").readText()
                val parser = Parser()
                val stringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                var itemsJson  = json.getValue("items")



                //var candleTimes:JsonObject = itemsJson[0] as JsonObject

               // var times = candleTimes.getValue("title")
                textView.post(
                        {
                            dialog.cancel()
                            textView.text = itemsJson.toString()
                        })
            }

            /*val i = Intent(this,Main2Activity::class.java)           //go to the next activity
            startActivity(i)*/
        }

        /*val stringArray = ArrayList<String>()
        val jsonArray = JSONArray()
        var i = 0
        val count = jsonArray.length()
        while (i < count) {
            try {
                val jsonObject = jsonArray.getJSONObject(i)
                stringArray.add(jsonObject.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            i++
        }*/
    }

}
