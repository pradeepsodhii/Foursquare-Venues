package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("name")
    val name: String?= null,
    @SerializedName("icon")
    val icon: Icon?= null
)