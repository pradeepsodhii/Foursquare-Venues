package com.venue.foursquare.api

import com.venue.foursquare.api.model.detail.VenueDetailResponse
import com.venue.foursquare.api.model.explore.VenuesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Apis {

    @GET("venues/explore")
    suspend fun getNearByVenue(
        @Query("ll") latLng: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("sortByDistance") sort: Int = 1
    ): VenuesResponse

    @GET("venues/{id}")
    suspend fun getVenueDetail(
        @Path("id") id: String
    ): VenueDetailResponse

}