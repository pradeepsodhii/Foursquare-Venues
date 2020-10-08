package com.venue.foursquare.api.model.explore


import com.google.gson.annotations.SerializedName

data class ItemPhotoGroup(
    @SerializedName("id")
    val id: String?= null,
    @SerializedName("prefix")
    val prefix: String?= null,
    @SerializedName("suffix")
    val suffix: String?= null
)