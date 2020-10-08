package com.venue.foursquare.api.model.explore


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("address")
    val address: String?= null,
    @SerializedName("lat")
    val lat: Double?= null,
    @SerializedName("lng")
    val lng: Double?= null,
    @SerializedName("distance")
    val distance: Int?= null,
    @SerializedName("formattedAddress")
    val formattedAddress: List<String>?= null
)