package com.venue.foursquare.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.venue.foursquare.R
import com.venue.foursquare.app.BaseActivity
import com.venue.foursquare.ui.venues.fragment.VenueDetailFragment
import com.venue.foursquare.ui.venues.fragment.VenueNearByFragment
import com.venue.foursquare.util.Utils
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector


class MainActivity :  BaseActivity(), MainNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Utils.requestingLocationUpdates(this)) {
            navToBlankFragment(VenueNearByFragment())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mAttachedFragment?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
       if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
           navToBlankFragment(VenueNearByFragment())
       }
    }

    @Module
    abstract class TypeBindingModule {
        @Binds
        abstract fun makeListener(impl: MainActivity): MainNavigator
    }

    @Module
    class ProvidesModule {
        @Provides
        fun fragmentMan(activity: MainActivity): FragmentManager {
            return activity.supportFragmentManager
        }
    }

    @Module
    abstract class BindingModule {
        @ContributesAndroidInjector(
            modules = [
                ProvidesModule::class,
                TypeBindingModule::class,
                VenueNearByFragment.BindingModule::class,
                VenueDetailFragment.BindingModule::class
            ]
        )
        abstract fun contributeInjector(): MainActivity
    }

    override fun showVenueList() {
        navToFragment(
            VenueNearByFragment()
        )
    }

    override fun showVenueDetail() {
        navToFragment(
            VenueNearByFragment()
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}