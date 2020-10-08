package com.venue.foursquare.api.model.explore


import com.google.gson.annotations.SerializedName

data class Photos(
    @SerializedName("count")
    val count: Int?= null,
    @SerializedName("groups")
    val groups: List<PhotoGroup>?= null
)