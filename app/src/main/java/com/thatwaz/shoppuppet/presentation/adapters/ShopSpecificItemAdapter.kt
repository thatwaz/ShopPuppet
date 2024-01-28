package com.thatwaz.shoppuppet.presentation.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.databinding.ItemShopSpecificBinding
import com.thatwaz.shoppuppet.domain.model.Item



class ShopSpecificItemAdapter(
    private val colorStateList: ColorStateList,
    private val onItemCheckedListener: (Item) -> Unit
) : ListAdapter<Item, ShopSpecificItemAdapter.ItemViewHolder>(ShopSpecificItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemShopSpecificBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position) // get item from ListAdapter
        holder.bind(item)
    }

    inner class ItemViewHolder(private val binding: ItemShopSpecificBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.tvItemForShop.text = item.name
            binding.ivShopSpecificStar.visibility = if (item.isPriorityItem) View.VISIBLE else View.INVISIBLE
            binding.cbPurchased.isChecked = item.isPurchased
            binding.cbPurchased.buttonTintList = colorStateList

            // Clear previous listeners and set new ones
            binding.cbPurchased.setOnCheckedChangeListener(null)
            binding.cbPurchased.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onItemCheckedListener(item)
                }
            }
        }
    }

    companion object {
        private val ShopSpecificItemDiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}








