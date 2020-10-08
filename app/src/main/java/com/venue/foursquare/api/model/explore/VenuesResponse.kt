package com.venue.foursquare.api.model.explore

import com.google.gson.annotations.SerializedName
import com.venue.foursquare.api.model.detail.Meta

data class VenuesResponse(
    @SerializedName("meta")
    val meta: Meta?= null,
    @SerializedName("response")
    val response: Response?= null
)