package com.thatwaz.shoppuppet.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.databinding.ItemFrequentlyPurchasedBinding
import com.thatwaz.shoppuppet.domain.model.Item

class FrequentlyPurchasedItemAdapter :
    ListAdapter<Item, FrequentlyPurchasedItemAdapter.ItemViewHolder>(DiffCallback) {

    var onItemClick: ((Item) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemFrequentlyPurchasedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick)
    }


    class ItemViewHolder(private var binding: ItemFrequentlyPurchasedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, onItemClick: ((Item) -> Unit)?) {
            binding.tvFrequentlyPurchasedItems.text = item.name
            itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}
