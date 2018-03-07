package com.example.royok.finalproject

import android.app.ProgressDialog
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

/**
 * Created by Michael Gabbay on 07/03/2018.
 */
class Info {
    var candleTime: String = ""
    var parasha: String = ""
    var havdala: String = ""


    fun getData() {

        val result = URL("\t\n" + "http://www.hebcal.com/shabbat/?cfg=json&geo=pos&latitude=31.771959&longitude=35.217018&tzid=Asia/Jerusalem&m=50&b=35").readText()
        val stringBuilder = StringBuilder(result)
        val parser = Parser()
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        val items: JsonArray<JsonObject> = json.get("items") as JsonArray<JsonObject>
        var i = 0
        for (i in 0..items.size - 1) {
            if (items[i].containsValue("candles")) {
                candleTime = items[i].getValue("title") as String
            } else if (items[i].containsValue("parashat")) {
                parasha = items[i].getValue("hebrew") as String
            } else if (items[i].containsValue("havdalah")) {
                havdala = items[i].getValue("title") as String
            }
        }

    }
}
