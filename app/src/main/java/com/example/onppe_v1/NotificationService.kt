package com.example.onppe_v1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat

fun sendNotification(context: Context, title:String, message:String) {
    val NOTIFICATION_CHANNEL_ID = "10001"
    val default_notification_channel_id = "default"
    val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    val mBuilder =  NotificationCompat.Builder(context,default_notification_channel_id)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setSound(sound)
        .setAutoCancel(true)
        .setContentText( message )
    //  .setContentIntent(pendingIntent)

    val mNotificationManager =  context.getSystemService(Context. NOTIFICATION_SERVICE ) as NotificationManager
    if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
        val audioAttributes =  AudioAttributes.Builder()
            .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
            .setUsage(AudioAttributes. USAGE_ALARM )
            .build() ;
        val importance = NotificationManager. IMPORTANCE_HIGH ;
        val notificationChannel = NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
        notificationChannel.enableLights( true ) ;
        notificationChannel.setLightColor(Color. RED ) ;
        notificationChannel.enableVibration( true ) ;
        notificationChannel.setSound(sound , audioAttributes) ;
        mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        mNotificationManager.createNotificationChannel(notificationChannel) ;
    }

    mNotificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())


}
