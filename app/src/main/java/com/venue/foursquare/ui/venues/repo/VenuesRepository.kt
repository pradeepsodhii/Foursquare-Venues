package com.venue.foursquare.ui.venues.repo

import com.venue.foursquare.api.Apis
import com.venue.foursquare.api.model.detail.VenueDetailResponse
import com.venue.foursquare.api.model.explore.VenuesResponse
import com.venue.foursquare.app.AppController
import com.venue.foursquare.db.VenueDao
import com.venue.foursquare.db.VenueDatabase
import com.venue.foursquare.db.VenueItem
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class VenuesRepository @Inject constructor(
    private val apis: Apis
) {
    var venueDataDao: VenueDao

    suspend fun getNearByVenues(
        latLong: String,
        limit: Int,
        offset: Int
    ): VenuesResponse {
        return apis.getNearByVenue(
            latLong,
            limit,
            offset
        )
    }

    suspend fun getVenueDetail(
        id: String,
    ): VenueDetailResponse {
        return apis.getVenueDetail(id)
    }

    init {
        val database: VenueDatabase = VenueDatabase.getDatabase(AppController.getInstance())
        venueDataDao = database.venueDao()
    }

    fun addAll(list:List<VenueItem>){
        runBlocking {
            venueDataDao.addAllVenues(list)
        }
    }

    fun update(item: VenueItem) {
        runBlocking {
            venueDataDao.update(item)
        }
    }

    fun getVenue(id: String): VenueItem = runBlocking {
        venueDataDao.venue(id)
    }

    fun getAll(): List<VenueItem> = runBlocking {
        venueDataDao.allVenues()
    }

    fun delete(item: VenueItem) {
        runBlocking {
            venueDataDao.delete(item)
        }
    }

    fun deleteAll() {
        runBlocking {
            venueDataDao.deleteAll()
        }
    }
}