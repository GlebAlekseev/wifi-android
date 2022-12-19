package com.example.wifitrilateration.ui.rv.callback

import androidx.recyclerview.widget.DiffUtil
import com.example.wifitrilateration.data.framework.entity.RouterRssi
import com.example.wifitrilateration.domain.entity.Router

class RouterRssiDiffCallBack : DiffUtil.ItemCallback<RouterRssi>() {
    override fun areItemsTheSame(oldItem: RouterRssi, newItem: RouterRssi): Boolean {
        return oldItem.bssid == newItem.bssid
    }

    override fun areContentsTheSame(oldItem: RouterRssi, newItem: RouterRssi): Boolean {
        return oldItem == newItem
    }
}