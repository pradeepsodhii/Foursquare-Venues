package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class Photos(
    @SerializedName("count")
    val count: Int?= null,
    @SerializedName("groups")
    val groups: List<Group>?= null
)