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


/**
 * Adapter for displaying items specific to a shop.
 * It handles the layout and binding of individual items, reflecting their purchased state and priority status.
 *
 * @property colorStateList ColorStateList used to tint the checkbox based on the item's state.
 * @property onItemCheckedListener Lambda function that is called when an item's checked state changes.
 */
class ShopSpecificItemAdapter(
    private val colorStateList: ColorStateList,
    private val onItemCheckedListener: (Item) -> Unit
) : ListAdapter<Item, ShopSpecificItemAdapter.ItemViewHolder>(ShopSpecificItemDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemShopSpecificBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, colorStateList, onItemCheckedListener)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ItemViewHolder(
        private val binding: ItemShopSpecificBinding,
        private val colorStateList: ColorStateList,
        private val onItemCheckedListener: (Item) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
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








