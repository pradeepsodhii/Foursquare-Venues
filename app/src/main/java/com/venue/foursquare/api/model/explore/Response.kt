package com.venue.foursquare.api.model.explore


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("totalResults")
    val totalResults: Int?= null,
    @SerializedName("groups")
    val groups: ArrayList<Group>?= null
)