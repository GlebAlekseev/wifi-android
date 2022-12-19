package com.glebalekseevjk.yasmrhomework.ui.rv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wifitrilateration.domain.entity.Router
import com.example.wifitrilateration.ui.rv.callback.RouterConfigDiffCallBack
import com.glebalekseevjk.wifitrilateration.R

class ConfigRouterAdapter :
    ListAdapter<Router, ConfigRouterAdapter.ConfigRouterViewHolder>(RouterConfigDiffCallBack()) {
    var editClickListener: ((id: Long) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfigRouterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.router_config_item_rv,
            parent,
            false
        )
        return ConfigRouterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConfigRouterViewHolder, position: Int) {
        val router = getItem(position)
        holder.level.text = router.level.toString()
        holder.bssid.text = router.bssid.toString()
    }

    override fun onViewRecycled(holder: ConfigRouterViewHolder) {
        super.onViewRecycled(holder)
//        holder.infoIv.setOnClickListener(null)
    }


    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

    class ConfigRouterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bssid: TextView = view.findViewById(R.id.bssid)
        val level: TextView = view.findViewById(R.id.level)
    }

    companion object {
        const val VIEW_TYPE = 1
    }
}