package com.thatwaz.shoppuppet.presentation.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.ShoppingItemBinding
import com.thatwaz.shoppuppet.domain.model.ItemUiModel
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.util.ResourceCache


class ListAdapter(
    private val itemClickListener: ItemClickListener,
    private val resourceCache: ResourceCache
) : androidx.recyclerview.widget.ListAdapter<ItemUiModel, ListAdapter.ShoppingViewHolder>(
    DiffCallback
) {

    interface ItemClickListener {
        fun onEditItem(item: ItemUiModel)
        fun onDeleteItem(item: ItemUiModel)
    }

    class ShoppingViewHolder(
        private val binding: ShoppingItemBinding,
        private val itemClickListener: ItemClickListener,
        private val resourceCache: ResourceCache
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(itemUiModel: ItemUiModel) {
            binding.tvItemName.text = itemUiModel.itemName
            binding.imgStar.visibility =
                if (itemUiModel.isPriorityItem) View.VISIBLE else View.INVISIBLE
            binding.chipGroupShops.removeAllViews()

            itemUiModel.shopNames.forEach { shop ->
                val chip = createChipForShop(shop)
                binding.chipGroupShops.addView(chip)
            }

            binding.ivArrowDown.setOnClickListener {
                // Check if chips are currently visible
                val isChipsVisible = binding.chipGroupShops.visibility == View.VISIBLE

                // Toggle chip group's visibility: If visible, hide it; if hidden, show it
                binding.chipGroupShops.visibility = if (isChipsVisible) View.GONE else View.VISIBLE

                // Adjust visibility of delete and edit icons inversely to the chip group's visibility
                val iconVisibility = if (isChipsVisible) View.GONE else View.VISIBLE
                binding.ivDeleteItem.visibility = iconVisibility
                binding.ivEditItem.visibility = iconVisibility

                // Update the arrow icon to indicate the toggle state: Down arrow when chips are
                // hidden, up arrow when visible
                val arrowIconResId = if (isChipsVisible) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
                binding.ivArrowDown.setImageDrawable(ContextCompat.getDrawable(itemView.context, arrowIconResId))
            }


            binding.ivDeleteItem.setOnClickListener {
                itemClickListener.onDeleteItem(itemUiModel)
            }
            binding.ivEditItem.setOnClickListener {
                itemClickListener.onEditItem(itemUiModel)
            }
        }

        private fun createChipForShop(shop: Shop): Chip {
            val chip = Chip(itemView.context)
            val colorResId = resourceCache.getColorResId(shop.colorResName, "color")

            chip.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            val chipColor = if (colorResId != 0) {
                ContextCompat.getColor(itemView.context, colorResId)
            } else {
                ContextCompat.getColor(itemView.context, R.color.black)
            }
            chip.chipBackgroundColor = ColorStateList.valueOf(chipColor)
            chip.text = shop.name
            chip.isClickable = false
            chip.isCheckable = false

            return chip
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ItemUiModel>() {
        override fun areItemsTheSame(oldItem: ItemUiModel, newItem: ItemUiModel): Boolean {
            // Implement this based on your needs
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: ItemUiModel, newItem: ItemUiModel): Boolean {
            return oldItem.itemId == newItem.itemId &&
                    oldItem.isPriorityItem == newItem.isPriorityItem &&
                    oldItem.itemName == newItem.itemName &&
                    oldItem.shopNames == newItem.shopNames
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ShoppingItemBinding.inflate(layoutInflater, parent, false)

        // Assume resourceCache is a property of the adapter
        return ShoppingViewHolder(binding, itemClickListener, resourceCache)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}



