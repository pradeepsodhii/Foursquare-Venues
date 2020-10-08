package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("code")
    val code: Int?= null,
    @SerializedName("requestId")
    val requestId: String?= null
)