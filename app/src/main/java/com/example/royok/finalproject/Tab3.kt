package com.example.royok.finalproject

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.tab_1.view.*

/**
 * Created by royok on 06/03/2018.
 */
class Tab3 : android.support.v4.app.Fragment (){
//    var mTimePicker : timePicker ?= null
    var saveTime : Button?= null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_3, container, false)
        saveTime = rootView.findViewById(R.id.setTime)
        saveTime?.setOnClickListener() {  }
        return rootView
    }
}