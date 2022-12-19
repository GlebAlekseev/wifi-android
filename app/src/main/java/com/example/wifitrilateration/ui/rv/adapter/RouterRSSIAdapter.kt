package com.glebalekseevjk.yasmrhomework.ui.rv.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wifitrilateration.data.framework.entity.RouterRssi
import com.example.wifitrilateration.ui.fragment.MapsFragment
import com.example.wifitrilateration.ui.rv.callback.RouterRssiDiffCallBack
import com.glebalekseevjk.wifitrilateration.R

class RouterRSSIAdapter :
    ListAdapter<RouterRssi, RouterRSSIAdapter.RouterRSSIViewHolder>(RouterRssiDiffCallBack()) {
    var editClickListener: ((bssid: Bundle) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouterRSSIViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.router_config_item_rv,
            parent,
            false
        )
        return RouterRSSIViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouterRSSIViewHolder, position: Int) {
        val routerRssi = getItem(position)
        holder.rssi.text = routerRssi.rssi.toString()
        holder.bssid.text = routerRssi.bssid.toString()
        holder.bssid.setOnClickListener {
            // запустить

            val bundle = bundleOf(
                MapsFragment.BSSID to routerRssi.bssid
            )
            editClickListener?.invoke(bundle)

        }
    }

    override fun onViewRecycled(holder: RouterRSSIViewHolder) {
        super.onViewRecycled(holder)
//        holder.infoIv.setOnClickListener(null)
    }


    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

    class RouterRSSIViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bssid: TextView = view.findViewById(R.id.bssid)
        val rssi: TextView = view.findViewById(R.id.rssi)
    }

    companion object {
        const val VIEW_TYPE = 1
    }
}