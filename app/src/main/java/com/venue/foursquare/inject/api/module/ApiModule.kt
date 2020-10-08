package com.venue.foursquare.inject.api.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module(
    includes = [
        ApisModule::class,
        JsonModule::class,
        HttpClientModule::class,
        InterceptorsModule::class
    ]
)
class ApiModule {
    @Provides
    fun retroBuilder(
        client: OkHttpClient
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
    }
}
