package com.example.royok.finalproject


import android.os.Bundle
import android.provider.Settings.Global.getString
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.tab_1.*
import kotlinx.android.synthetic.main.tab_1.view.*

/**
 * Created by royok on 06/03/2018.
 */
class Tab1() : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_1, container, false)
        var mActivity = activity as Main2Activity
        var sData = mActivity.sData as ShabatData

        var parashaTxt : TextView = rootView.findViewById(R.id.parashaTxt)
        parashaTxt.text = sData.parasha
        var curLightCandleTxt : TextView = rootView.findViewById(R.id.curLightCandleTxt)
        curLightCandleTxt.text = sData.candletime
        var havdalahTxt : TextView = rootView.findViewById(R.id.havdalahTxt)
        havdalahTxt.text = sData.havdala
        var city : TextView = rootView.findViewById(R.id.locationTxt)
        city.text = sData.city
        return rootView
    }

}