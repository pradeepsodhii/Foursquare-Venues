package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class Likes(
    @SerializedName("count")
    val count: Int
)