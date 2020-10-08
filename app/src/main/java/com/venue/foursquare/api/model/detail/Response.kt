package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("venue")
    val venue: Venue?= null
)