package com.venue.foursquare.api.model.explore


import com.google.gson.annotations.SerializedName

data class PhotoGroup(
    @SerializedName("type")
    val type: String?= null,
    @SerializedName("name")
    val name: String?= null,
    @SerializedName("count")
    val count: Int?= null,
    @SerializedName("items")
    val items: List<ItemPhotoGroup>?= null
)