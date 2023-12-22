package com.thatwaz.shoppuppet.presentation.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
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
import com.thatwaz.shoppuppet.domain.model.ShopWithSelection
import com.thatwaz.shoppuppet.presentation.viewmodel.SelectedShopsViewModel


class ShopSelectionAdapter(
    var onItemClick: (Shop) -> Unit,
    private val selectedShopsViewModel: SelectedShopsViewModel
) : ListAdapter<ShopWithSelection, ShopSelectionAdapter.ViewHolder>(ShopDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemShopsToTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the current item which includes both the Shop and its selection state
        val shopWithSelection = getItem(position)
//        val isSelected = selectedShopIds.contains(shopWithSelection.shop.id)
        // Use the holder's bind method to set up the shop details and selection state
        holder.bind(shopWithSelection)
    }

    inner class ViewHolder(private val binding: ItemShopsToTagBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(shopWithSelection: ShopWithSelection) {
            binding.tvShopToTag.text = shopWithSelection.shop.name
            binding.cbTagShop.isChecked = shopWithSelection.isSelected
            // Convert the color resource name to an actual color resource ID
            val colorResId = binding.root.context.resources.getIdentifier(
                shopWithSelection.shop.colorResName,
                "color",
                binding.root.context.packageName
            )
            val color = if (colorResId != 0) ContextCompat.getColor(
                binding.root.context,
                colorResId
            ) else Color.BLACK // Fallback to a default color if not found

            // Set checkbox color
            val checkboxColorStateList = getCheckboxColorStateList(binding.root.context, color)
            binding.cbTagShop.buttonTintList = checkboxColorStateList

            // Check if an icon is set
            val iconResId = binding.root.context.resources.getIdentifier(
                shopWithSelection.shop.iconResName,
                "drawable",
                binding.root.context.packageName
            )
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
                shopWithSelection.shop.initials?.let {
                    binding.tvShopInitials.text = it
                    binding.tvShopInitials.setTextColor(color)
                }

                // Hide the ImageView for the icon
                binding.ivShopIcon.visibility = View.GONE
                binding.tvShopInitials.visibility = View.VISIBLE
            }

            // Set text color using color
            binding.tvShopToTag.setTextColor(color)
            binding.cbTagShop.setOnCheckedChangeListener(null) // remove listener
            binding.cbTagShop.isChecked = shopWithSelection.isSelected // change check state
            binding.cbTagShop.setOnCheckedChangeListener { _, isChecked ->
                // Update the shop's selection state
                shopWithSelection.isSelected = isChecked
                Log.i("Pigeon", "Checkbox for shop ${shopWithSelection.shop.id} is now: $isChecked")
                Log.i("Pigeon", "shop with sel is ${shopWithSelection.isSelected}")

                // Update the ViewModel state

                // todo investigate why I had to put !isChecked vs isChecked to get it to work
                if (!isChecked) {
                    selectedShopsViewModel.addSelectedShop(shopWithSelection.shop)
                } else {
                    selectedShopsViewModel.removeSelectedShop(shopWithSelection.shop)
                }

                // Notify about the selection for any external handling
                onItemClick(shopWithSelection.shop)
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

    }

    class ShopDiffCallback : DiffUtil.ItemCallback<ShopWithSelection>() {
        override fun areItemsTheSame(
            oldItem: ShopWithSelection,
            newItem: ShopWithSelection
        ): Boolean {
            return oldItem.shop.id == newItem.shop.id
        }

        override fun areContentsTheSame(
            oldItem: ShopWithSelection,
            newItem: ShopWithSelection
        ): Boolean {
            return oldItem == newItem
        }
    }
}




