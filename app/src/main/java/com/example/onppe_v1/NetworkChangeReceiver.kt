package com.example.onppe_v1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast

/*
class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (isNetworkAvailable(context)) {
            Toast.makeText(context, "Network Available", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Network Unavailable", Toast.LENGTH_LONG).show()
        }
    }

    interface ConnexionReceiverListener {
        fun onNetworkConnectionChanged(isConnected:Boolean)
    }
    companion object{
        var  connexionReceiverListener : ConnexionReceiverListener? = null
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
 */