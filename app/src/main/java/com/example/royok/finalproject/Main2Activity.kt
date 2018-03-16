package com.example.royok.finalproject

import android.os.Bundle
import android.support.v4.app.*
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.Serializable

class Main2Activity : FragmentActivity(){

    private lateinit var pagerAdapter: mPagerAdapter
    // TODO:: change information to data object

    lateinit var sData :Serializable
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        pagerAdapter = mPagerAdapter(supportFragmentManager)
        // Set up the ViewPager with the sections adapter.
        container.adapter =  pagerAdapter

        sData = intent.getSerializableExtra("sData")

    }

    class mPagerAdapter(fragmentManager: FragmentManager) :
            FragmentStatePagerAdapter(fragmentManager) {



        override fun getItem(position: Int): Fragment {
            when (position) {
                0 ->return Tab1() as Fragment
                1 -> return Tab2() as Fragment
                2 -> return Tab3() as Fragment
            }
            return Tab1() as Fragment
        }


        override fun getCount(): Int {
            return 3
        }
    }

}

