package com.venue.foursquare.inject.module

import com.venue.foursquare.ui.MainActivity
import dagger.Module

@Module(
    includes = [
        MainActivity.BindingModule::class
    ]
)
abstract class ActivityListingModule
