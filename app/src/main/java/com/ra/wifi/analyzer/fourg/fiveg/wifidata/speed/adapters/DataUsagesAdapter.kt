package com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.databinding.ItemDailyUsagesBinding
import com.ra.wifi.analyzer.fourg.fiveg.wifidata.speed.modals.UsagesData

class DataUsagesAdapter(private val dataUsages: List<UsagesData>) :
    RecyclerView.Adapter<DataUsagesAdapter.DataUsagesViewHolder>() {

    inner class DataUsagesViewHolder(
        private val binding: ItemDailyUsagesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UsagesData) {
            binding.apply {
                tvDate.text = item.date
                wifiData.text = item.wifi
                mobileData.text = item.mobile
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataUsagesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val dataBinding = ItemDailyUsagesBinding.inflate(
            layoutInflater,
            parent,
            false
        )

        return DataUsagesViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: DataUsagesViewHolder, position: Int) {
        val item = dataUsages[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = dataUsages.size
}