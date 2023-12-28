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


class ListAdapter(private val itemClickListener: ItemClickListener
) : androidx.recyclerview.widget.ListAdapter<ItemUiModel, ListAdapter.ShoppingViewHolder>(DiffCallback) {

    interface ItemClickListener {
        fun onEditItem(item: ItemUiModel)
        fun onDeleteItem(item: ItemUiModel)
    }

    class ShoppingViewHolder(private val binding: ShoppingItemBinding, private val itemClickListener: ItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(itemUiModel: ItemUiModel) {
            binding.tvItemName.text = itemUiModel.itemName

            if (itemUiModel.isPriorityItem) {
                binding.imgStar.visibility = View.VISIBLE
            } else {
                binding.imgStar.visibility = View.INVISIBLE
            }

            // Clear any existing chips
            binding.chipGroupShops.removeAllViews()

            // Populate chips based on the shops
            itemUiModel.shopNames.forEach { shop ->
                val chip = Chip(itemView.context)

                // Get the color resource ID from the shop's color name
                val colorResId = itemView.context.resources.getIdentifier(
                    shop.colorResName, "color", itemView.context.packageName)
                chip.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                // Use the shop's color for the chip
                if (colorResId != 0) {
                    val chipColor = ContextCompat.getColor(itemView.context, colorResId)
                    chip.chipBackgroundColor = ColorStateList.valueOf(chipColor)
                } else {
                    // Fallback color if resource not found
                    chip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.context, R.color.black))
                }

                // Set other chip properties
                chip.text = shop.name
                chip.isClickable = false
                chip.isCheckable = false

                // Add chip to the group
                binding.chipGroupShops.addView(chip)
            }



//        fun bind(itemUiModel: ItemUiModel) {
//            binding.tvItemName.text = itemUiModel.itemName
//
//             if (itemUiModel.isPriorityItem) {
//                binding.imgStar.visibility = View.VISIBLE
//            } else {
//               binding.imgStar.visibility = View.INVISIBLE
//            }
//
//            // Clear any existing chips
//            binding.chipGroupShops.removeAllViews()
//
//            // Populate chips based on the shops
//            itemUiModel.shopNames.forEach { shopName ->
//                val chip = Chip(itemView.context)
//                chip.setChipBackgroundColorResource(R.color.off_white)
//                val strokeColor =
//                    ContextCompat.getColor(itemView.context, R.color.shop_blue)
//                chip.chipStrokeColor = ColorStateList.valueOf(strokeColor)
//                chip.chipStrokeWidth = TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP,
//                    1f,
//                    itemView.resources.displayMetrics
//                )
//                chip.text = shopName.name
//                chip.isClickable = false
//                chip.isCheckable = false
//                binding.chipGroupShops.addView(chip)
//            }

            binding.imgDropDown.setOnClickListener {
                if (binding.chipGroupShops.visibility == View.VISIBLE) {
                    binding.chipGroupShops.visibility = View.GONE
                    binding.imgDropDown.setImageDrawable(
                        ContextCompat.getDrawable(
                            it.context,
                            R.drawable.ic_arrow_down
                        )
                    )
                } else {
                    binding.chipGroupShops.visibility = View.VISIBLE
                    binding.imgDropDown.setImageDrawable(
                        ContextCompat.getDrawable(
                            it.context,
                            R.drawable.ic_arrow_up
                        )
                    )
                }
            }
            binding.ivDeleteItem.setOnClickListener {
                itemClickListener.onDeleteItem(itemUiModel)
            }
            binding.ivEditItem.setOnClickListener {
                itemClickListener.onEditItem(itemUiModel)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ItemUiModel>() {
        override fun areItemsTheSame(oldItem: ItemUiModel, newItem: ItemUiModel): Boolean {
            // Implement this based on your needs
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: ItemUiModel, newItem: ItemUiModel): Boolean {
            // Implement this based on your needs
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ShoppingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ShoppingItemBinding.inflate(layoutInflater, parent, false)
        return ListAdapter.ShoppingViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ListAdapter.ShoppingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}



