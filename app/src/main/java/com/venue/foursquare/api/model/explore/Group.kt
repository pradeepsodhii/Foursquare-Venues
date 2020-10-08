package com.venue.foursquare.api.model.explore


import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("type")
    val type: String?= null,
    @SerializedName("name")
    val name: String?= null,
    @SerializedName("items")
    val items: ArrayList<Item>?= null
)