package com.venue.foursquare.inject.api.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class JsonModule {
    @Provides
    @Singleton
    fun gson() = Gson()
}