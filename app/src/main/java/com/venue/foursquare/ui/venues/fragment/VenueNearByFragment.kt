package com.venue.foursquare.ui.venues.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.venue.foursquare.ui.MainNavigator
import com.venue.foursquare.R
import com.venue.foursquare.api.model.explore.VenuesResponse
import com.venue.foursquare.arch.viewmodel.ProviderAdaptedFactory
import com.venue.foursquare.arch.viewmodel.ViewModelHolder
import com.venue.foursquare.db.VenueItem
import com.venue.foursquare.location.LocationService
import com.venue.foursquare.ui.venues.adapter.VenueNearByAdapter
import com.venue.foursquare.ui.venues.viewmodel.VenuesViewModel
import com.venue.foursquare.util.Utils
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_venue_nearby.*
import timber.log.Timber
import javax.inject.Inject

class VenueNearByFragment: DaggerFragment() {
    @Inject
    lateinit var mainNavigator: MainNavigator

    @Inject
    lateinit var viewModelHolder: ViewModelHolder<VenuesViewModel>

    private val viewModel: VenuesViewModel
        get() {
            return viewModelHolder.viewModel
        }

    lateinit var venueNearByAdapter: VenueNearByAdapter
    private val limitCount = 10
    private var mOffset = 0
    private var isLocationFetch = false
    private var latitude: Double?= null
    private var longitude: Double?= null
    private val venueItemList = arrayListOf<VenueItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_venue_nearby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModels()
        if(Utils.isOnline(requireContext())){
            if(latitude!= null && longitude != null){
                getVenues(latitude, longitude)
            }
        }else{
            venueNearByAdapter.setDataList(viewModel.allVenues() as ArrayList<VenueItem>)
        }
    }

    private fun initViews() {
        venueNearByAdapter = VenueNearByAdapter(requireContext(), arrayListOf())
        venueFeeds.adapter = venueNearByAdapter
        venueSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                venueNearByAdapter.filter.filter(newText)
                return false
            }

        })

    }

    private fun initViewModels() {
        viewModel.isLoading.observe(viewLifecycleOwner, isViewLoadingObserver)
        viewModel.onError.observe(viewLifecycleOwner, onErrorObserver)
        viewModel.venueListObserver.observe(viewLifecycleOwner, onVenueListObserver)
        viewModel.location.observe(viewLifecycleOwner, isLocationObserver)
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        if (it != null) {
            progress_circular.visibility =
                if (it) View.VISIBLE else View.GONE
        }
    }

    private val onErrorObserver = Observer<Any> {
        if (it != null) {
            Toast.makeText(requireContext(), "Error $it", Toast.LENGTH_SHORT).show()
        }
    }

    private val onVenueListObserver = Observer<VenuesResponse> {
        venueItemList.clear()
        it.response?.groups?.first()?.items?.let { itemList ->
            for (item in itemList) {
                venueItemList.add(
                    VenueItem(
                        id = item.venue?.id!!,
                        name = item.venue.name,
                        address = item.venue.location?.address,
                    )
                )
            }
            venueNearByAdapter.setDataList(venueItemList)
            viewModel.deleteAll()
            viewModel.addAllVenues(venueItemList.toList())
        }
    }

    private val isLocationObserver = Observer<Location> {
        if (it != null && !isLocationFetch && Utils.isOnline(requireContext())) {
            isLocationFetch = true
            latitude = it.latitude
            longitude = it.longitude
            getVenues(latitude, longitude)
        }
    }

    private fun getVenues(latitude: Double?, longitude: Double?){
        viewModel.getVenueList(
            "${latitude},${longitude}",
            limitCount,
            mOffset
        )
    }

    override fun onStart() {
        super.onStart()
        Intent(activity, LocationService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    lateinit var locationService: LocationService
    var mBound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.getService()
            locationService.setLocationChangeListener(viewModel.myLocationChangedListener)
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.unbindService(connection)
        mBound = false
    }

    @Module
    class ProvidesModule {
        @Provides
        fun viewModelHolder(
            fragment: VenueNearByFragment,
            adaptedFactory: ProviderAdaptedFactory<VenuesViewModel>
        ) = ViewModelHolder.moduleMethod(fragment, adaptedFactory)
    }

    @Module
    abstract class BindingModule {
        @ContributesAndroidInjector(
            modules = [ProvidesModule::class]
        )
        abstract fun contributeInjector(): VenueNearByFragment
    }
}