package com.venue.foursquare.app

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.venue.foursquare.BuildConfig
import com.venue.foursquare.R
import com.venue.foursquare.app.pemission.MyPermission
import com.venue.foursquare.app.pemission.PermissionResultListener
import com.venue.foursquare.location.LocationService
import com.venue.foursquare.util.Utils
import dagger.android.support.DaggerAppCompatActivity
import kotlin.system.exitProcess

open class BaseActivity : DaggerAppCompatActivity() {
    var mAttachedFragment: Fragment? = null
    private var permissionResultListener: PermissionResultListener? = null
    private val myPermissionsList = ArrayList<MyPermission>()
    private lateinit var mSettingsClient: SettingsClient
    private lateinit var mLocationSettingsRequest: LocationSettingsRequest
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onStart() {
        super.onStart()
        mSettingsClient = LocationServices.getSettingsClient(this)
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000.toLong()
        locationRequest.fastestInterval = 2 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)
        registerReceiver()
        if (!isProviderEnabled()) {
            turnGPSOn()
        } else if (!Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions()
            } else {
                serviceStart(Intent(this, LocationService::class.java))
            }
        }
    }

    override fun onStop() {
        unregisterReceiver()
        super.onStop()
    }

    private fun showGPSAlert(text: String) {
        AlertDialog.Builder(this)
            .setMessage(text)
            .setCancelable(false)
            .setPositiveButton(R.string.ok) { _, _ ->
                if (!isProviderEnabled()) {
                    turnGPSOn()
                }
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                exitProcess(0)
            }.show()
    }

    fun navToFragment(fragment: Fragment,  tag: String? = fragment.toString(), layoutId: Int? = R.id.fragment_holder , callingFragment: Fragment? = null, fragmentRequestCode: Int = -1, bundleData: Bundle? = null) {
        mAttachedFragment = fragment
        if(callingFragment != null && fragmentRequestCode != -1) {
            fragment.setTargetFragment(callingFragment, fragmentRequestCode)
        }

        if(bundleData != null) {
            fragment.arguments = bundleData
        }
        supportFragmentManager.beginTransaction().addToBackStack(tag)
            .replace(layoutId!!, fragment).commitAllowingStateLoss()
    }

    fun navToBlankFragment(fragment: Fragment) {
        mAttachedFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, fragment).commitAllowingStateLoss()
    }

    private fun unregisterReceiver() {
        unregisterReceiver(gpsReceiver)
    }

    private fun registerReceiver() {
        registerReceiver(gpsReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
    }

    fun enableGPS(state: Boolean) {
        if (state) {
            turnGPSOn()
        }
    }

    fun isProviderEnabled(): Boolean {
        val manager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private val gpsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                if (!isProviderEnabled()) {
                    if (firstConnect) {
                        enableGPS(true)
                        firstConnect = false
                    }
                } else {
                    if (!firstConnect) {
                        enableGPS(false)
                        firstConnect = true
                    }
                }
            }
        }
    }

    fun checkPermissions(): Boolean {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                return (PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) &&
                        PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ))
            }
            else -> {
                return PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
            }
        }
    }

     fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        when {
            shouldProvideRationale -> {
                Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.ok) {
                        ActivityCompat.requestPermissions(
                            this,
                            when {
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                    )
                                }
                                else -> {
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                }
                            },
                            REQUEST_PERMISSIONS_REQUEST_CODE
                        )
                    }
                    .show()
            }
            else -> {
                ActivityCompat.requestPermissions(
                    this,
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            )
                        }
                        else -> {
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        }
                    },
                    REQUEST_PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GPS_REQUEST) {
            if (isProviderEnabled()) {
                if (!Utils.requestingLocationUpdates(applicationContext)) {
                    if (!checkPermissions()) {
                        requestPermissions()
                    }
                }
            } else {
                if (!isProviderEnabled()) {
                    showGPSAlert(resources.getString(R.string.gps_text))
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (myPermissionsList.isNotEmpty()) {
            if (requestCode <= myPermissionsList.size) {
                myPermissionsList[requestCode].isAsked = true
                myPermissionsList[requestCode].isGranted =
                    (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                processMyPermissionList()
            }
        }

        when (requestCode) {
            REQUEST_PERMISSIONS_REQUEST_CODE -> {
                when {
                    grantResults.isEmpty() -> {
                    }
                    grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                        serviceStart(Intent(this, LocationService::class.java))
                    }
                    else -> {
                        Snackbar.make(
                            findViewById(R.id.activity_main),
                            R.string.permission_denied_explanation,
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction(R.string.settings) {
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts(
                                    "package",
                                    BuildConfig.APPLICATION_ID, null
                                )
                                intent.data = uri
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                            .show()
                    }
                }
            }
        }
    }

    private fun serviceStart(i: Intent) {
        startService(i)
    }

    private fun turnGPSOn() {
        mSettingsClient
            .checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(this) {
                when {
                    !Utils.requestingLocationUpdates(this) -> {
                        if (!checkPermissions()) {
                            requestPermissions()
                        }
                    }
                }
            }
            .addOnFailureListener(this) { e ->
                val statusCode =
                    (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val rae =
                            e as ResolvableApiException
                        rae.startResolutionForResult(
                            this, GPS_REQUEST
                        )
                    } catch (sie: IntentSender.SendIntentException) {
                        sie.printStackTrace()
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = getString(R.string.location_settings_inadequate)
                        Toast.makeText(
                            this,
                            errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    fun requestMyPermissions(
        permissionStringList: ArrayList<String>,
        permissionResultListener: PermissionResultListener
    ) {
        this.permissionResultListener = permissionResultListener

        for ((index, permission) in permissionStringList.withIndex()) {
            val myPermission = MyPermission()
            myPermission.isGranted = (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED)
            myPermission.requestCode = index
            myPermission.permission = permission
            myPermission.isAsked = myPermission.isGranted
            myPermissionsList.add(myPermission)
        }
        processMyPermissionList()
    }

    private fun processMyPermissionList() {
        for ((index, myPermission) in myPermissionsList.withIndex()) {
            if (!myPermission.isAsked && !myPermission.isGranted) {
                return ActivityCompat.requestPermissions(
                    this,
                    arrayOf(myPermission.permission),
                    index
                )
            }
        }
        for ((index, myPermission) in myPermissionsList.withIndex()) {
            if (!myPermission.isGranted) {
                permissionResultListener?.onPermissionsDenied()
                permissionResultListener?.onPermissionDenied(myPermission.permission)
                return
            }
        }
        permissionResultListener?.onPermissionsGranted()
    }

    companion object {
        private var firstConnect = true
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        const val GPS_REQUEST = 1001
    }
}