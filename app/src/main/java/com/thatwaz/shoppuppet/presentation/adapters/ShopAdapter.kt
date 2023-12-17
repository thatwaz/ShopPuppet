package com.thatwaz.shoppuppet.presentation.adapters


import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.databinding.ItemShopBinding
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.domain.model.ShopWithItemCount


class ShopAdapter : ListAdapter<ShopWithItemCount, ShopAdapter.ShopViewHolder>(ShopDiffCallback()) {


     interface OnShopItemClickListener {
         fun onShopItemClick(shop: Shop)
     }

     interface OnShopItemLongClickListener {
         fun onShopItemLongClick(shop: Shop)
     }


     var onShopItemClickListener: OnShopItemClickListener? = null
     var onShopItemLongClickListener: OnShopItemLongClickListener? = null



    inner class ShopViewHolder(private val binding: ItemShopBinding) : RecyclerView.ViewHolder(binding.root) {

//        val priorityImageView: ImageView = itemView.findViewById(R.id.iv_priority)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val shopWithItemCount = getItem(position)
                    val shop = shopWithItemCount.shop
                    onShopItemClickListener?.onShopItemClick(shop)
                }
            }
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val shopWithItemCount = getItem(position)
                    val shop = shopWithItemCount.shop
                    onShopItemLongClickListener?.onShopItemLongClick(shop)
                    true  // Return true to indicate that the long press event is consumed.
                } else {
                    false
                }
            }
        }



//        val shopIconImageView: ImageView = itemView.findViewById(R.id.shop_icon)
//        val shopNameTextView: TextView = itemView.findViewById(R.id.shop_name)
//        val shopIconInitials: TextView = itemView.findViewById(R.id.shop_icon_initials)
//        val badgeCountTextView: TextView = itemView.findViewById(R.id.badge_count)

        @RequiresApi(Build.VERSION_CODES.P)
        fun bind(shopWithItemCount: ShopWithItemCount) {
            val shop = shopWithItemCount.shop
            binding.shopName.text = shop.name
//            val color = ContextCompat.getColor(itemView.context, shop.colorResName)
            // Convert the resource name to an actual color resource ID

//            var color: Int = Color.TRANSPARENT
            val colorResId = itemView.context.resources.getIdentifier(shop.colorResName, "color", itemView.context.packageName)
            val color = if (colorResId != 0) ContextCompat.getColor(binding.root.context, colorResId) else Color.BLACK // Fallback to a default color if not found

            // Check if the resource ID is valid
            if (colorResId != 0) {
                val color = ContextCompat.getColor(itemView.context, colorResId)
                // Use the color as needed, for example, setting it on a CardView
//                val cardView: CardView = binding.cvShop
                binding.cvShop.setCardBackgroundColor(color)
            } else {
                // Handle the case where the color resource is not found
                // For example, you might want to set a default color
            }

            val cardView: CardView = itemView.findViewById(R.id.cv_shop)

//            if (shopWithItemCount.hasPriorityItem) {
//                priorityImageView.visibility = View.VISIBLE
//            } else {
//                priorityImageView.visibility = View.INVISIBLE
//            }

            val backgroundResId = if (shopWithItemCount.hasPriorityItem) {
                R.drawable.badge_background_priority // Replace with your priority drawable resource ID
            } else {
                R.drawable.badge_background // Replace with your non-priority drawable resource ID
            }
            binding.badgeCount.setBackgroundResource(backgroundResId)

            // If the shop has initials, display them and hide the icon
            if (!shop.initials.isNullOrEmpty()) {
                binding.shopIconInitials.text = shop.initials
                binding.shopIconInitials.setTextColor(color)
                binding.shopIconInitials.visibility = View.VISIBLE
                binding.shopIcon.visibility = View.INVISIBLE
            } else {  // Else, display the icon and hide the initials
                val iconResId = itemView.context.resources.getIdentifier(shop.iconResName, "drawable", itemView.context.packageName)
                if (iconResId != 0) {
                    binding.shopIcon.apply {
                        setImageResource(iconResId)
                        setColorFilter(color)
                        visibility = View.VISIBLE
                    }

                } else {
                    // Handle the case where the icon resource is not found
                    // For example, hide the ImageView or set a default icon
                    binding.shopIcon.visibility = View.GONE
                }
                binding.shopIconInitials.visibility = View.GONE
            }

// For the card view background and shadow
            val shadowDrawable = ContextCompat.getDrawable(
                itemView.context,
                R.drawable.custom_shadow
            ) as LayerDrawable
            val shadowLayer = shadowDrawable.getDrawable(0) as GradientDrawable
            shadowLayer.setColor(color)
            cardView.background = shadowDrawable
            cardView.outlineSpotShadowColor = color
            binding.badgeCount.text = shopWithItemCount.itemCount.toString()

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemShopBinding.inflate(layoutInflater, parent, false)
        return ShopViewHolder(binding)
    }



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shopWithItemCount = getItem(position)
        holder.bind(shopWithItemCount)
    }}


    class ShopDiffCallback : DiffUtil.ItemCallback<ShopWithItemCount>() {
        override fun areItemsTheSame(oldItem: ShopWithItemCount, newItem: ShopWithItemCount): Boolean {
            return oldItem.shop.id == newItem.shop.id
        }

        override fun areContentsTheSame(oldItem: ShopWithItemCount, newItem: ShopWithItemCount): Boolean {
            return oldItem == newItem
        }
    }




