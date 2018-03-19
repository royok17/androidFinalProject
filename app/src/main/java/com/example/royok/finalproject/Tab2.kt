package com.example.royok.finalproject

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.net.http.*; //added this import statement

import kotlinx.android.synthetic.main.tab_1.view.*
import android.view.MotionEvent



/**
 * Created by royok on 06/03/2018.
 */
class Tab2 : android.support.v4.app.Fragment ()
{
    var web:WebView ?= null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_2, container, false)

        web = rootView.findViewById(R.id.webViewer)
        web!!.setWebViewClient(WebViewClient())
        web!!.settings.builtInZoomControls

        var str = "http://he.wikipedia.org/wiki/פרשת_"
        var curPar = "במדבר"
        web!!.loadUrl(str + curPar)

        return rootView
    }


}