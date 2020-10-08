package com.venue.foursquare.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LocationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent(context, LocationService::class.java)
        if (intent.action == Intent.ACTION_BOOT_COMPLETED){
            context.startService(i)
        }
    }
}