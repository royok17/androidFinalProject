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

    var curParashaTxt : TextView ?= null
    var curLightCandleTxt : TextView ?= null
    var curHavdalah : TextView ?= null
    var city : TextView ?= null

            override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_1, container, false)
        var mActivity = activity as Main2Activity
        var sData = mActivity.sData as ShabatData

        curParashaTxt = rootView.findViewById(R.id.curParashaTxt)
        curLightCandleTxt = rootView.findViewById(R.id.curLightCandleTxt)
        curHavdalah =  rootView.findViewById(R.id.curHavdalah)
        city = rootView.findViewById(R.id.locationTxt)

        curParashaTxt!!.text = sData.parasha.subSequence(5,sData.parasha.length)
        curLightCandleTxt!!.text = sData.candletime
        curHavdalah!!.text = sData.havdala
        city!!.text = sData.city

        return rootView
    }

}