package com.venue.foursquare.db

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venue_table")
data class VenueItem(
    @NonNull
    @PrimaryKey
    val id: String,
    val name: String? = null,
    val address: String? = null,
    val mobile: String? = null,
    val rating: String? = null,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null
)
