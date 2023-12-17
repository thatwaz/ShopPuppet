package com.thatwaz.shoppuppet.presentation.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
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

            // Convert the color resource name to an actual color resource ID
            val colorResId = binding.root.context.resources.getIdentifier(shop.colorResName, "color", binding.root.context.packageName)
            val color = if (colorResId != 0) ContextCompat.getColor(binding.root.context, colorResId) else Color.BLACK // Fallback to a default color if not found

            // Set checkbox color
            val checkboxColorStateList = getCheckboxColorStateList(binding.root.context, color)
            binding.cbTagShop.buttonTintList = checkboxColorStateList

            // Check if an icon is set
            val iconResId = binding.root.context.resources.getIdentifier(shop.iconResName, "drawable", binding.root.context.packageName)
            if (iconResId != 0) {
                // Get and set the icon drawable
                val iconDrawable = ContextCompat.getDrawable(binding.root.context, iconResId)
                iconDrawable?.let {
                    val wrappedDrawable = DrawableCompat.wrap(it)
                    DrawableCompat.setTint(wrappedDrawable, color)
                    binding.ivShopIcon.setImageDrawable(wrappedDrawable)
                }

                // Make sure the TextView for initials is hidden
                binding.tvShopInitials.visibility = View.GONE
                binding.ivShopIcon.visibility = View.VISIBLE
            } else {
                // Use initials if icon is not provided
                shop.initials?.let {
                    binding.tvShopInitials.text = it
                    binding.tvShopInitials.setTextColor(color)
                }

                // Hide the ImageView for the icon
                binding.ivShopIcon.visibility = View.GONE
                binding.tvShopInitials.visibility = View.VISIBLE
            }

            // Set text color using color
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


        private fun getCheckboxColorStateList(context: Context, color: Int): ColorStateList {
            val states = arrayOf(
                intArrayOf(-android.R.attr.state_enabled), // disabled
                intArrayOf(android.R.attr.state_checked),  // checked
                intArrayOf()                               // default (unchecked)
            )

            val colors = intArrayOf(
                ContextCompat.getColor(context, R.color.black), // Disabled color
                color,                                                   // Checked color
                color                                                    // Default (unchecked) color
            )

            return ColorStateList(states, colors)
        }


//    private fun getCheckboxColorStateList(context: Context, colorResId: Int): ColorStateList {
//        val states = arrayOf(
//            intArrayOf(-android.R.attr.state_enabled), // disabled
//            intArrayOf(android.R.attr.state_checked),  // checked
//            intArrayOf()                               // default
//        )
////todo figure out color issue
//        val colors = intArrayOf(
//            ContextCompat.getColor(context, R.color.black), // Disabled color
//            ContextCompat.getColor(context, colorResId),                          // Checked color
//            ContextCompat.getColor(context, colorResId)   // Default color
//        )
//
//        return ColorStateList(states, colors)
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




