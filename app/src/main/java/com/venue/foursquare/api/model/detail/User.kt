package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("photo")
    val photo: Photo? = null
)