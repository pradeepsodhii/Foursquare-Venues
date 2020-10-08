package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class VenueDetailResponse(
    @SerializedName("meta")
    val meta: Meta?= null,
    @SerializedName("response")
    val response: Response?= null
)