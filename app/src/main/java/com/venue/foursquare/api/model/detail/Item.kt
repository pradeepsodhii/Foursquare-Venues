package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("createdAt")
    val createdAt: Int?= null,
    @SerializedName("height")
    val height: Int?= null,
    @SerializedName("id")
    val id: String?= null,
    @SerializedName("prefix")
    val prefix: String?= null,
    @SerializedName("source")
    val source: Source?= null,
    @SerializedName("suffix")
    val suffix: String?= null,
    @SerializedName("user")
    val user: User?= null,
    @SerializedName("visibility")
    val visibility: String?= null,
    @SerializedName("width")
    val width: Int?= null
)