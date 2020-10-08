package com.venue.foursquare.api.model.detail


import com.google.gson.annotations.SerializedName

data class Venue(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("location")
    val location: Location? = null,
    @SerializedName("categories")
    val categories: List<Category>? = null,
    @SerializedName("likes")
    val likes: Likes? = null,
    @SerializedName("dislike")
    val dislike: Boolean? = null,
    @SerializedName("hereNow")
    val hereNow: HereNow? = null,
    @SerializedName("contact")
    val contact: Contact? = null,
    @SerializedName("rating")
    val rating: Double? = null,
    @SerializedName("canonicalUrl")
    val canonicalUrl: String? = null,
    @SerializedName("photos")
    val photos: Photos? = null
)