package com.thatwaz.shoppuppet.presentation.adapters

import android.content.res.ColorStateList
import android.util.Log
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


class ListAdapter(
    private val itemClickListener: ItemClickListener
) : androidx.recyclerview.widget.ListAdapter<ItemUiModel, ListAdapter.ShoppingViewHolder>(
    DiffCallback
) {

    interface ItemClickListener {
        fun onEditItem(item: ItemUiModel)
        fun onDeleteItem(item: ItemUiModel)
    }

    class ShoppingViewHolder(
        private val binding: ShoppingItemBinding,
        private val itemClickListener: ItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(itemUiModel: ItemUiModel) {
            binding.tvItemName.text = itemUiModel.itemName

            if (itemUiModel.isPriorityItem) {
                binding.imgStar.visibility = View.VISIBLE
                Log.i("Goats","Priority set as ${itemUiModel.itemName} ${itemUiModel.isPriorityItem}")
            } else {
                binding.imgStar.visibility = View.INVISIBLE
                Log.i("Goats","NOT PRIORITY set as ${itemUiModel.itemName} ${itemUiModel.isPriorityItem}")
            }

            // Clear any existing chips
            binding.chipGroupShops.removeAllViews()

            // Populate chips based on the shops
            itemUiModel.shopNames.forEach { shop ->
                val chip = Chip(itemView.context)

                // Get the color resource ID from the shop's color name
                val colorResId = itemView.context.resources.getIdentifier(
                    shop.colorResName, "color", itemView.context.packageName
                )
                chip.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                // Use the shop's color for the chip
                if (colorResId != 0) {
                    val chipColor = ContextCompat.getColor(itemView.context, colorResId)
                    chip.chipBackgroundColor = ColorStateList.valueOf(chipColor)
                } else {
                    // Fallback color if resource not found
                    chip.chipBackgroundColor = ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.context, R.color.black)
                    )
                }

                // Set other chip properties
                chip.text = shop.name
                chip.isClickable = false
                chip.isCheckable = false

                // Add chip to the group
                binding.chipGroupShops.addView(chip)
            }


            binding.ivArrowDown.setOnClickListener {
                if (binding.chipGroupShops.visibility == View.VISIBLE) {
                    binding.apply {
                        ivDeleteItem.visibility = View.VISIBLE
                        ivEditItem.visibility = View.VISIBLE
                    }
                    binding.chipGroupShops.visibility = View.GONE
                    binding.apply {
                        ivDeleteItem.visibility = View.GONE
                        ivEditItem.visibility = View.GONE
                    }
                    binding.ivArrowDown.setImageDrawable(
                        ContextCompat.getDrawable(
                            it.context,
                            R.drawable.ic_arrow_down
                        )
                    )
                } else {
                    binding.chipGroupShops.visibility = View.VISIBLE
                    binding.apply {
                        ivDeleteItem.visibility = View.VISIBLE
                        ivEditItem.visibility = View.VISIBLE
                    }
                    binding.ivArrowDown.setImageDrawable(
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
            // Ensure all relevant fields are compared
            Log.i(
                "DiffJardin",
                "areContentsTheSame:  -- OLD ITEM Priority: ${oldItem.isPriorityItem}, NEW ITEM Priority: ${newItem.isPriorityItem}"
            )
            return oldItem.itemId == newItem.itemId &&
                    oldItem.isPriorityItem == newItem.isPriorityItem &&
                    oldItem.itemName == newItem.itemName &&
                    oldItem.shopNames == newItem.shopNames

        }


//        override fun areContentsTheSame(oldItem: ItemUiModel, newItem: ItemUiModel): Boolean {
//            // Implement this based on your needs
//            return oldItem == newItem
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ShoppingItemBinding.inflate(layoutInflater, parent, false)
        return ShoppingViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}



