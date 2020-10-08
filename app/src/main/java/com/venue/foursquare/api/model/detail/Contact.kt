package com.venue.foursquare.api.model.detail

import com.google.gson.annotations.SerializedName

data class Contact(
    @SerializedName("phone")
    val phone: String?= null,
    @SerializedName("formattedPhone")
    val formattedPhone: String?= null
)