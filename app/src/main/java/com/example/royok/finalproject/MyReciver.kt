package com.example.royok.finalproject

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager

class MyReciver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val timeAlert = System.currentTimeMillis()
        val notificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = Notification.Builder(context)
                .setContentTitle("lighting candle:")
                .setContentText("text")
                .setSmallIcon(R.drawable.notification_icon_background)
                .setAutoCancel(true).setWhen(timeAlert)
                .setContentIntent(pendingIntent)
        notificationManager.notify(0, notification.build())
    }
}
