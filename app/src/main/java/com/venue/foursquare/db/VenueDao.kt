package com.venue.foursquare.db

import androidx.room.*

@Dao
abstract  class VenueDao {

    @Query("SELECT * from venue_table")
    abstract suspend fun allVenues(): List<VenueItem>

    @Query("SELECT * from venue_table where id=:id")
    abstract suspend fun venue(id: String): VenueItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addAllVenues(myVenueEntities: List<VenueItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(venueEntity: VenueItem)

    @Update
    abstract suspend fun update(venueEntity: VenueItem)

    @Delete
    abstract suspend fun delete(venueEntity: VenueItem)

    @Query("Delete from venue_table ")
    abstract suspend fun deleteAll()
}