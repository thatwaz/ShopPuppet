package com.thatwaz.shoppuppet.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.databinding.ItemRecentlyPurchasedBinding
import com.thatwaz.shoppuppet.domain.model.Item

class RecentlyPurchasedItemAdapter :
    ListAdapter<Item, RecentlyPurchasedItemAdapter.ItemViewHolder>(DiffCallback) {

    var onItemClick: ((Item) -> Unit)? = null
    var onItemLongPress: ((Item) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemRecentlyPurchasedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick, onItemLongPress)
    }



    class ItemViewHolder(private var binding: ItemRecentlyPurchasedBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: Item, onItemClick: ((Item) -> Unit)?, onItemLongPress: ((Item) -> Unit)?) {
            binding.tvRecentlyPurchasedItems.text = item.name
            itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
            itemView.setOnLongClickListener {
                onItemLongPress?.invoke(item)
                true  // Return true to indicate that the callback consumed the long press
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
