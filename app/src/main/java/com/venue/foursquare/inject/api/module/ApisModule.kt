package com.venue.foursquare.inject.api.module

import com.google.gson.Gson
import com.venue.foursquare.BuildConfig
import com.venue.foursquare.inject.api.ApiSessionScope
import com.venue.foursquare.inject.api.Apis
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
class ApisModule {

    @Apis
    @Provides
    @ApiSessionScope
    fun apis(
        retroBuilder: Retrofit.Builder,
        gson: Gson
    ): Retrofit {
        return retroBuilder
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @ApiSessionScope
    fun loginApi(@Apis retrofit: Retrofit) =
        retrofit.create<com.venue.foursquare.api.Apis>()

}