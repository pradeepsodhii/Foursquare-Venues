package com.venue.foursquare.ui.venues.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.venue.foursquare.R
import com.venue.foursquare.db.VenueItem
import com.venue.foursquare.ui.MainActivity
import com.venue.foursquare.ui.venues.fragment.VenueDetailFragment
import kotlinx.android.synthetic.main.venue_nearby_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class VenueNearByAdapter(
    private val context: Context,
    private var venueItemList: ArrayList<VenueItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var venueItemFilterList = ArrayList<VenueItem>()

    init {
        venueItemFilterList = venueItemList
    }

    fun setDataList(dataList: ArrayList<VenueItem>) {
        venueItemList = dataList
        venueItemFilterList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView =
            LayoutInflater.from(parent.context).inflate(R.layout.venue_nearby_item, parent, false)
        return object : RecyclerView.ViewHolder(rootView) {}
    }

    override fun getItemCount() = venueItemFilterList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.venueTitle.text = venueItemFilterList[position].name

        holder.itemView.venueLocation.text =
            venueItemFilterList[position].address

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", venueItemFilterList[position].id)
            (context as MainActivity).navToFragment(
                VenueDetailFragment(),
                bundleData = bundle
            )
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    venueItemFilterList = venueItemList
                } else {
                    val resultList = ArrayList<VenueItem>()
                    for (row in venueItemList) {
                        if (row.name?.toLowerCase(Locale.ROOT)
                                ?.contains(charSearch.toLowerCase(Locale.ROOT))!!
                        ) {
                            resultList.add(row)
                        }
                    }
                    venueItemFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = venueItemFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                venueItemFilterList = results?.values as ArrayList<VenueItem>
                notifyDataSetChanged()
            }

        }
    }

}
