package com.venue.foursquare.location

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.*
import com.google.android.gms.location.*
import com.venue.foursquare.util.Utils

class LocationService : Service() {

    private lateinit var mLocationRequest: LocationRequest

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var mLocationCallback: LocationCallback

    private lateinit var mServiceHandler: Handler

    private lateinit var mLocation: Location

    override fun onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }
        createLocationRequest()
        getLastLocation()
        requestLocationUpdates()
        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        stopForeground(true)
        return binder
    }

    override fun onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null)
    }

    private fun requestLocationUpdates() {
        Utils.setRequestingLocationUpdates(this, true)
        try {
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Utils.setRequestingLocationUpdates(this, false)
        }
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLocation = task.result!!
                        myLocationChangeListener?.onLocationChanged(mLocation)
                    }
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun onNewLocation(location: Location) {
        mLocation = location
        myLocationChangeListener?.onLocationChanged(mLocation)
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval =
            UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.fastestInterval =
            FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    companion object {
        private val TAG = LocationService::class.java.simpleName
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): LocationService = this@LocationService
    }

    private var myLocationChangeListener: MyLocationChangeListener? = null
    fun setLocationChangeListener(locationChangeListener: MyLocationChangeListener) {
        this.myLocationChangeListener = locationChangeListener
    }

    open interface MyLocationChangeListener {
        fun onLocationChanged(location: Location)
    }

}
