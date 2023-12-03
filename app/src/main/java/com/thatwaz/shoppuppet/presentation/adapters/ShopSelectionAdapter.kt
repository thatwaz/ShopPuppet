package com.thatwaz.shoppuppet.presentation.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
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

            // Set checkbox color
            val checkboxColorStateList = getCheckboxColorStateList(binding.root.context, shop.colorResId)
            binding.cbTagShop.buttonTintList = checkboxColorStateList

            // Check if an icon is set
            if (shop.iconRef != 0) {
                // Get and set the icon drawable
                val iconDrawable = ContextCompat.getDrawable(binding.root.context, shop.iconRef)
                iconDrawable?.let {
                    val wrappedDrawable = DrawableCompat.wrap(it)
                    val iconColor = ContextCompat.getColor(binding.root.context, shop.colorResId)
                    DrawableCompat.setTint(wrappedDrawable, iconColor)
                    binding.ivShopIcon.setImageDrawable(wrappedDrawable)
                }

                // Make sure the TextView for initials is hidden
                binding.tvShopInitials.visibility = View.GONE
                binding.ivShopIcon.visibility = View.VISIBLE
            } else {
                // Use initials if icon is not provided
                shop.initials?.let {
                    binding.tvShopInitials.text = it
                    // Set the text color using colorResId
                    val color = ContextCompat.getColor(binding.root.context, shop.colorResId)
                    binding.tvShopInitials.setTextColor(color)
                }

                // Hide the ImageView for the icon
                binding.ivShopIcon.visibility = View.GONE
                binding.tvShopInitials.visibility = View.VISIBLE
            }

            // Set text color using colorResId
            val color = ContextCompat.getColor(binding.root.context, shop.colorResId)
            binding.tvShopToTag.setTextColor(color)



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

    private fun getCheckboxColorStateList(context: Context, colorResId: Int): ColorStateList {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_enabled), // disabled
            intArrayOf(android.R.attr.state_checked),  // checked
            intArrayOf()                               // default
        )

        val colors = intArrayOf(
            ContextCompat.getColor(context, R.color.black), // Disabled color
            ContextCompat.getColor(context, colorResId),                          // Checked color
            ContextCompat.getColor(context, colorResId)   // Default color
        )

        return ColorStateList(states, colors)
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




