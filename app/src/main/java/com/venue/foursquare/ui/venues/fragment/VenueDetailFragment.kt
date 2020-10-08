package com.venue.foursquare.ui.venues.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.venue.foursquare.R
import com.venue.foursquare.api.model.detail.Venue
import com.venue.foursquare.api.model.detail.VenueDetailResponse
import com.venue.foursquare.arch.viewmodel.ProviderAdaptedFactory
import com.venue.foursquare.arch.viewmodel.ViewModelHolder
import com.venue.foursquare.db.VenueItem
import com.venue.foursquare.ui.venues.viewmodel.VenuesViewModel
import com.venue.foursquare.util.Utils
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_venue_detail.*
import javax.inject.Inject


class VenueDetailFragment: DaggerFragment() {

    @Inject
    lateinit var viewModelHolder: ViewModelHolder<VenuesViewModel>

    private val viewModel: VenuesViewModel
        get() {
            return viewModelHolder.viewModel
        }

    private var venueId: String? = null
    private var venue: Venue? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_venue_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        venueId = arguments?.getString("id")
        initViews()
        initViewModels()
    }

    private fun initViews() {
        toolbar.menu.clear()
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        when {
            Utils.isOnline(requireContext()) -> {
                venueId?.let {
                    viewModel.getVenueDetail(it)
                }
            }
            else -> {
                venueId?.let {
                    setVenueDetail(viewModel.venue(it))
                }
            }
        }
    }

    private fun initViewModels() {
        viewModel.isLoading.observe(viewLifecycleOwner, isViewLoadingObserver)
        viewModel.onError.observe(viewLifecycleOwner, onErrorObserver)
        viewModel.venueDetailObserver.observe(viewLifecycleOwner, onVenueDetailObserver)
        viewModel.imageObserver.observe(viewLifecycleOwner, onImageObserver)
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

    private val onVenueDetailObserver = Observer<VenueDetailResponse> {
        if( it!= null){
            venue = it.response?.venue
            val item = venue?.photos?.groups?.first()?.items?.first()
            val url = item?.prefix + item?.width + "x" + item?.height + item?.suffix
            viewModel.imageByteArray(Glide.with(requireContext()), url)
        }
    }

    private val onImageObserver = Observer<ByteArray> {
       val item =  VenueItem(
            id = venue?.id!!,
            name = venue?.name,
            address = venue?.location?.address,
            mobile = venue?.contact?.phone,
            rating = venue?.rating.toString(),
            image = it
        )
        setVenueDetail(item)
        viewModel.updateVenue(item)
    }

    private fun setVenueDetail(item: VenueItem) {
        titleValue.text = item.name
        contactInfoValue.text = item.mobile
        addressValue.text = item.address
        ratingValue.text = item.rating.toString()
        item.image?.let {
            venueImage.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    it,
                    0,
                    it.size
                )
            )
        }
    }

    @Module
    class ProvidesModule {
        @Provides
        fun viewModelHolder(
            fragment: VenueDetailFragment,
            adaptedFactory: ProviderAdaptedFactory<VenuesViewModel>
        ) = ViewModelHolder.moduleMethod(fragment, adaptedFactory)
    }

    @Module
    abstract class BindingModule {
        @ContributesAndroidInjector(
            modules = [ProvidesModule::class]
        )
        abstract fun contributeInjector(): VenueDetailFragment
    }

}