package com.example.royok.finalproject

import android.annotation.SuppressLint
import android.app.*
import android.app.Notification.*
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.readPendingIntentOrNullFromParcel
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.app.NotificationChannel
import android.app.NotificationManager
import kotlinx.android.synthetic.main.tab_1.view.*
import java.util.*
import android.R.string.cancel
import android.content.Context.ALARM_SERVICE
import android.app.AlarmManager
import android.app.PendingIntent



/**
 * Created by royok on 06/03/2018.
 */
class Tab3 : android.support.v4.app.Fragment (){
//    var mTimePicker : timePicker ?= null
    var saveTime : Button?= null
    var switch1 : Switch?= null
    var tp: TimePicker ?= null
    var noteTxt: TextView?= null
    var widgetTxt = null

    @SuppressLint("WrongViewCast")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.tab_3, container, false)
        saveTime = rootView.findViewById(R.id.setTime)
        switch1 = rootView.findViewById(R.id.switch1)
        tp = rootView.findViewById(R.id.timePicker)
        noteTxt = rootView.findViewById(R.id.selectTimeTxt2)

        switch1!!.setOnClickListener(View.OnClickListener {
            if(switch1!!.isChecked) {
                saveTime!!.isEnabled = true
            }
            else {
                saveTime!!.isEnabled = false
                val intent = Intent()
                val sender = PendingIntent.getBroadcast(this.activity, 0, intent, 0)
                val alarmManager =  this.activity.getSystemService(ALARM_SERVICE) as AlarmManager

                alarmManager.cancel(sender)
            }
        })


        saveTime?.setOnClickListener()  //save time notification
        {
            val hour: Int = tp!!.hour
            val min: Int = tp!!.minute
            var text = "" + hour + ":" + min
            var calendar: Calendar = Calendar.getInstance()

            //calendar.set(Calendar.DAY_OF_WEEK,1)
            //calendar.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY)
            calendar.set(Calendar.HOUR_OF_DAY,17)
            calendar.set(Calendar.MINUTE,26)
            calendar.set(Calendar.SECOND, 0);

            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(this.activity,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            var alarmManager = this.activity.getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis, AlarmManager.INTERVAL_DAY,pendingIntent)

            /*val notification = Notification.Builder(this.activity)
                    .setContentTitle("lighting candle:")
                    .setContentText(text)
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setContentIntent(pendingIntent)

            val nm:NotificationManager = this.activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.notify(0,notification.build())*/
            saveTime!!.setBackgroundColor(Color.YELLOW)
        }

        return rootView
    }
}



