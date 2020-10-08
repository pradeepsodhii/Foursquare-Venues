package com.venue.foursquare.ui.venues.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Location
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.venue.foursquare.R
import com.venue.foursquare.api.model.detail.VenueDetailResponse
import com.venue.foursquare.api.model.explore.VenuesResponse
import com.venue.foursquare.arch.viewmodel.BaseViewModel
import com.venue.foursquare.db.VenueItem
import com.venue.foursquare.location.LocationService
import com.venue.foursquare.ui.venues.repo.VenuesRepository
import com.venue.foursquare.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VenuesViewModel @Inject constructor(
    private val repository: VenuesRepository
) : BaseViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _onError = MutableLiveData<Any>()
    val onError: LiveData<Any> = _onError

    private val _venueListObserver = MutableLiveData<VenuesResponse>()
    val venueListObserver: LiveData<VenuesResponse> = _venueListObserver

    private val _venueDetailObserver = MutableLiveData<VenueDetailResponse>()
    val venueDetailObserver: LiveData<VenueDetailResponse> = _venueDetailObserver

    private val _location = MutableLiveData<Location>()
    var location = _location

    private val _imageObserver = MutableLiveData<ByteArray>()
    val imageObserver: LiveData<ByteArray> = _imageObserver

    fun getVenueList(
        latLong: String,
        limit: Int,
        offset: Int
    ) {
        launch {
            _isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                repository.getNearByVenues(
                    latLong,
                    limit,
                    offset
                )
            }
            _isLoading.value = false
            _venueListObserver.value = result
        }
    }

    fun getVenueDetail(id: String) {
        launch {
            _isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                repository.getVenueDetail(
                    id
                )
            }
            _isLoading.value = false
            _venueDetailObserver.value = result
        }
    }

    val myLocationChangedListener = object : LocationService.MyLocationChangeListener {
        override fun onLocationChanged(location: Location) {
            _isLoading.postValue(false)
            this@VenuesViewModel.location.value = location
        }
    }

    fun imageByteArray(request: RequestManager, url: String?){
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        request.asBitmap().load(url).apply(options).into(object : CustomTarget<Bitmap?>() {
            override fun onLoadCleared(placeholder: Drawable?) {}
            override fun onResourceReady(bitmap: Bitmap, p1: Transition<in Bitmap?>?) {
                _imageObserver.postValue(Utils.getByteArray(bitmap))
            }
        })
    }

    fun updateVenue(item:VenueItem){
        repository.update(item)
    }

    fun addAllVenues(list:List<VenueItem>){
        repository.addAll(list)
    }

    fun deleteAll(){
        repository.deleteAll()
    }

    fun venue(id: String): VenueItem {
        return repository.getVenue(id)
    }

    fun allVenues(): List<VenueItem> {
        return repository.getAll()
    }
}