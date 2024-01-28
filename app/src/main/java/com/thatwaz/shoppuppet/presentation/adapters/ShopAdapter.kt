package com.thatwaz.shoppuppet.presentation.adapters


import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.ItemShopBinding
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.domain.model.ShopWithItemCount
import com.thatwaz.shoppuppet.util.ResourceCache


class ShopAdapter(
    private val resourceCache: ResourceCache
) : ListAdapter<ShopWithItemCount, ShopAdapter.ShopViewHolder>(ShopDiffCallback()) {

    interface OnShopItemClickListener {
        fun onShopItemClick(shop: Shop)
    }

    interface OnShopItemLongClickListener {
        fun onShopItemLongClick(shop: Shop)
    }


    var onShopItemClickListener: OnShopItemClickListener? = null
    var onShopItemLongClickListener: OnShopItemLongClickListener? = null


    class ShopViewHolder(
        private val binding: ItemShopBinding,
        private val resourceCache: ResourceCache,
        private val onShopItemClickListener: OnShopItemClickListener?,
        private val onShopItemLongClickListener: OnShopItemLongClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentShopWithItemCount: ShopWithItemCount? = null

        init {
            itemView.setOnClickListener {
                currentShopWithItemCount?.let {
                    onShopItemClickListener?.onShopItemClick(it.shop)
                }
            }
            itemView.setOnLongClickListener {
                currentShopWithItemCount?.let {
                    onShopItemLongClickListener?.onShopItemLongClick(it.shop)
                    true // Return true to indicate that the long press event is consumed.
                } ?: false
            }
        }

        fun bind(shopWithItemCount: ShopWithItemCount) {
            currentShopWithItemCount = shopWithItemCount
            val shop = shopWithItemCount.shop
            binding.shopName.text = shop.name

            // Fetch color and set default if not found
            val color = resourceCache.getColorResId(shop.colorResName).let { colorResId ->
                if (colorResId != 0) ContextCompat.getColor(binding.root.context, colorResId)
                else {
                    // Log the error or handle it as per your application's need
                    Log.e("ShopAdapter", "Color resource not found: ${shop.colorResName}")
                    ContextCompat.getColor(binding.root.context, R.color.inactive_grey) // Default color
                }
            }

            // Set card background color
            binding.cvShop.setCardBackgroundColor(color)

            // Use outlineSpotShadowColor for Android P and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                binding.cvShop.outlineSpotShadowColor = color
            }

            // Set badge background based on priority
            binding.badgeCount.setBackgroundResource(
                if (shopWithItemCount.hasPriorityItem) R.drawable.badge_background_priority
                else R.drawable.badge_background
            )

            // Handle shop icon and initials display
            if (!shop.initials.isNullOrEmpty()) {
                binding.shopIconInitials.text = shop.initials
                binding.shopIconInitials.setTextColor(color)
                binding.shopIcon.visibility = View.INVISIBLE
                binding.shopIconInitials.visibility = View.VISIBLE
            } else {
                val iconResId = resourceCache.getDrawableResId(shop.iconResName)
                if (iconResId != 0) {
                    binding.shopIcon.setImageResource(iconResId)
                    binding.shopIcon.setColorFilter(color)
                    binding.shopIcon.visibility = View.VISIBLE
                } else {
                    // Log the error or handle it as per your application's need
                    Log.e("ShopAdapter", "Icon resource not found: ${shop.iconResName}")
                    binding.shopIcon.visibility = View.GONE // Or set a default icon
                }

            }

            // Set custom shadow for the card view
            binding.cvShop.background = getCustomShadowDrawable(color)

            // Set item count
            binding.badgeCount.text = shopWithItemCount.itemCount.toString()
        }

        private fun getCustomShadowDrawable(color: Int): Drawable {
            val shadowDrawable = ContextCompat.getDrawable(
                itemView.context,
                R.drawable.custom_shadow
            ) as LayerDrawable
            (shadowDrawable.getDrawable(0) as GradientDrawable).setColor(color)
            return shadowDrawable
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemShopBinding.inflate(layoutInflater, parent, false)
        return ShopViewHolder(
            binding,
            resourceCache,
            onShopItemClickListener,
            onShopItemLongClickListener
        )
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shopWithItemCount = getItem(position)
        holder.bind(shopWithItemCount)
    }

}

class ShopDiffCallback : DiffUtil.ItemCallback<ShopWithItemCount>() {
    override fun areItemsTheSame(oldItem: ShopWithItemCount, newItem: ShopWithItemCount): Boolean {
        return oldItem.shop.id == newItem.shop.id
    }

    override fun areContentsTheSame(
        oldItem: ShopWithItemCount,
        newItem: ShopWithItemCount
    ): Boolean {
        return oldItem == newItem
    }
}




