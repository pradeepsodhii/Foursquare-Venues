package com.venue.foursquare.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE_NAME = "venue-database"

@Database(entities = [VenueItem::class], version = 1)
abstract class VenueDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao

    companion object {
        @Volatile private var INSTANCE: VenueDatabase? = null

        fun getDatabase(context: Context): VenueDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): VenueDatabase {
            return Room.databaseBuilder(
                context,
                VenueDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}