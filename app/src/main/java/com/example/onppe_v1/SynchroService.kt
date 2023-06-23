package com.example.onppe_v1

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SynchroService() : FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.v("token", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.getData().size > 0) {
            val id = remoteMessage.getData().get("id").toString().toInt()
            val statut = remoteMessage.getData().get("statut").toString()

            if(id!=null && statut!=null ) {
                // Change in SQL Lite

                var sign = AppDatabase.buildDatabase(this)?.getSignalementDao()?.getSignalementById(id)
                if (sign != null) {
                    sign.statut = statut
                    AppDatabase.buildDatabase(this)?.getSignalementDao()?.updateSynchronize(sign)
                }

                // send notification
                //sendNotification(this,"Mise à jour","Update signalement" + "nv id:"+id  )
                sendNotification(this,"Mise à jour","sign" + (sign?.signalementId ?: 0) + "/nv id:"+id   )

            }
        }
    }
}