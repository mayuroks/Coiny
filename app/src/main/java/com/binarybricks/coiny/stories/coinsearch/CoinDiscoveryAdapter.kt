package com.binarybricks.coiny.stories.coinsearch

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.binarybricks.coiny.adapterdelegates.CarousalAdapterDelegate
import com.binarybricks.coiny.adapterdelegates.DiscoveryNewsAdapterDelegate
import com.binarybricks.coiny.adapterdelegates.LabelCoinAdapterDelegate
import com.binarybricks.coiny.components.ModuleItem
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager

/**
Created by Pranay Airan 1/18/18.
 *
 * based on http://hannesdorfmann.com/android/adapter-delegates
 */

class CoinDiscoveryAdapter(
        toCurrency: String,
        var coinDiscoverList: MutableList<ModuleItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DISCOVERY_CAROUSAL = 0
    private val DISCOVERY_NEWS = 1
    private val LABEL = 2

    private val delegates: AdapterDelegatesManager<List<ModuleItem>> = AdapterDelegatesManager()

    init {
        delegates.addDelegate(DISCOVERY_CAROUSAL, CarousalAdapterDelegate(toCurrency))
        delegates.addDelegate(DISCOVERY_NEWS, DiscoveryNewsAdapterDelegate())
        delegates.addDelegate(LABEL, LabelCoinAdapterDelegate())

    }

    override fun getItemViewType(position: Int): Int {
        return delegates.getItemViewType(coinDiscoverList, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        delegates.onBindViewHolder(coinDiscoverList, position, viewHolder)
    }

    override fun getItemCount(): Int {
        return coinDiscoverList.size
    }
}