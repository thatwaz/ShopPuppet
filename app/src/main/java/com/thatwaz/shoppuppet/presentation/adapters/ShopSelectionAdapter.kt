package com.thatwaz.shoppuppet.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.databinding.ItemShopsToTagBinding
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.presentation.viewmodel.SelectedShopsViewModel


class ShopSelectionAdapter(
    var onItemClick: (Shop) -> Unit,
    private val selectedShopsViewModel: SelectedShopsViewModel
) : ListAdapter<Shop, ShopSelectionAdapter.ViewHolder>(ShopDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShopsToTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shop = getItem(position)
        holder.bind(shop, selectedShopsViewModel.isSelected(shop))
    }

    inner class ViewHolder(private val binding: ItemShopsToTagBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shop: Shop, isSelected: Boolean) {
            binding.tvShopToTag.text = shop.name

            // Remove any previous listeners
            binding.cbTagShop.setOnCheckedChangeListener(null)

            // Set the current state
            binding.cbTagShop.isChecked = isSelected

            // Set a new listener
            binding.cbTagShop.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedShopsViewModel.addSelectedShop(shop)
                } else {
                    selectedShopsViewModel.removeSelectedShop(shop)
                }

                // Optional: Notify an external listener about the selection
                onItemClick(shop)
            }
        }
    }
    class ShopDiffCallback : DiffUtil.ItemCallback<Shop>() {
        override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem == newItem
        }
    }
}




