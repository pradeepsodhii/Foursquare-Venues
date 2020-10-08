package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("prefix")
    val prefix: String? = null,
    @SerializedName("suffix")
    val suffix: String? = null
)