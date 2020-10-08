package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("url")
    val url: String? = null
)