package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("address")
    val address: String?= null,
    @SerializedName("lat")
    val lat: Double?= null,
    @SerializedName("lng")
    val lng: Double?= null,
    @SerializedName("country")
    val country: String?= null,
    @SerializedName("formattedAddress")
    val formattedAddress: List<String>?= null
)