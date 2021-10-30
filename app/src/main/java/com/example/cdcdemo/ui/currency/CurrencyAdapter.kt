package com.example.cdcdemo.ui.currency

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cdcdemo.data.CurrencyInfo
import com.example.cdcdemo.databinding.ItemCurrencyBinding

class CurrencyAdapter : ListAdapter<CurrencyInfo, CurrencyAdapter.CurrencyViewHodler>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHodler {
        val binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHodler(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHodler, position: Int) {
        holder.bind(getItem(position))
    }


    class CurrencyViewHodler(private val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currencyInfo: CurrencyInfo) {
            binding.apply {
                name.text = currencyInfo.name
                symbol.text = currencyInfo.symbol
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<CurrencyInfo>() {
        override fun areItemsTheSame(oldItem: CurrencyInfo, newItem: CurrencyInfo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CurrencyInfo, newItem: CurrencyInfo): Boolean =
            oldItem == newItem

    }
}
