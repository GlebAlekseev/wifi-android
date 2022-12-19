package com.example.wifitrilateration.ui.rv.callback

import androidx.recyclerview.widget.DiffUtil
import com.example.wifitrilateration.domain.entity.Router

class RouterConfigDiffCallBack : DiffUtil.ItemCallback<Router>() {
    override fun areItemsTheSame(oldItem: Router, newItem: Router): Boolean {
        return oldItem.bssid == newItem.bssid
    }

    override fun areContentsTheSame(oldItem: Router, newItem: Router): Boolean {
        return oldItem == newItem
    }
}