package com.venue.foursquare.api.model.explore


import com.google.gson.annotations.SerializedName
import com.venue.foursquare.api.model.detail.Category

data class Venue(
    @SerializedName("id")
    val id: String?= null,
    @SerializedName("name")
    val name: String?= null,
    @SerializedName("location")
    val location: Location?= null,
    @SerializedName("categories")
    val categories: List<Category>?= null,
    @SerializedName("photos")
    val photos: Photos?= null
)