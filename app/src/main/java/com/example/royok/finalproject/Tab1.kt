package com.example.royok.finalproject

import android.app.Fragment
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tab_1.view.*

/**
 * Created by royok on 06/03/2018.
 */
class Tab1 : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_1, container, false)
        return rootView
    }
}