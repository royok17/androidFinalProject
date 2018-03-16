package com.example.royok.finalproject


import android.app.Activity;

import android.os.Bundle
import android.support.v4.app.*
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*

import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.tab_1.view.*
import kotlinx.android.synthetic.main.tab_3.*
import android.view.View.OnFocusChangeListener

class Main2Activity : FragmentActivity(){

    companion object {

    }
//    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var pagerAdapter: mPagerAdapter


    lateinit var parasha :String
    lateinit var havdala :String
    lateinit var candletime :String
    lateinit var city :String
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        pagerAdapter = mPagerAdapter(supportFragmentManager)
        // Set up the ViewPager with the sections adapter.
        container.adapter =  pagerAdapter

//        pagerAdapter = mPagerAdapter(FragmentManager)
//        viewPager.adapter = pagerAdapter

        parasha = intent.getStringExtra("parasha")
        candletime = intent.getStringExtra("candleTime")
        havdala = intent.getStringExtra("havdala")
        city = intent.getStringExtra("city")
    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main2, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        val id = item.itemId
//
//        if (id == R.id.action_settings) {
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }
//
//
//    /**
//     * A [FragmentPagerAdapter] that returns a fragment corresponding to
//     * one of the sections/tabs/pages.
//     */
//    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
//
//        override fun getItem(position: Int): Fragment {
//            when (position) {
//                0 -> return Tab1() as Fragment
//                1 -> return Tab2() as Fragment
//                2 -> return Tab3() as Fragment
//            }
//            return Tab1() as Fragment
//        }
//
//        override fun getCount(): Int {
//            // Show 3 total pages.
//            return 3
//        }
//
//        override fun getPageTitle(position: Int): CharSequence? {
//            when (position) {
//                0 -> return "TAB 1"
//                1 -> return "TAB 2"
//                2 -> return "TAB 3"
//            }
//            return null
//        }
//    }

    // 1
    class mPagerAdapter(fragmentManager: FragmentManager) :
            FragmentStatePagerAdapter(fragmentManager) {


        // 2
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 ->return Tab1() as Fragment
                1 -> return Tab2() as Fragment
                2 -> return Tab3() as Fragment
            }
            return Tab1() as Fragment
        }

        // 3
        override fun getCount(): Int {
            return 3
        }
    }

}

