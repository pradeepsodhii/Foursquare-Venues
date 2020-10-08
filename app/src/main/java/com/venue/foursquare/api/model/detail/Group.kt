package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("count")
    val count: Int?= null,
    @SerializedName("items")
    val items: List<Item>?= null,
    @SerializedName("name")
    val name: String?= null,
    @SerializedName("type")
    val type: String?= null
)