package com.thatwaz.shoppuppet.presentation.adapters


import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R

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



    inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


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



        val shopIconImageView: ImageView = itemView.findViewById(R.id.shop_icon)
        val shopNameTextView: TextView = itemView.findViewById(R.id.shop_name)
        val shopIconInitials: TextView = itemView.findViewById(R.id.shop_icon_initials)
        val badgeCountTextView: TextView = itemView.findViewById(R.id.badge_count)

        @RequiresApi(Build.VERSION_CODES.P)
        fun bind(shopWithItemCount: ShopWithItemCount) {
            val shop = shopWithItemCount.shop
            shopNameTextView.text = shop.name
            val color = ContextCompat.getColor(itemView.context, shop.colorResId)
            val cardView: CardView = itemView.findViewById(R.id.cv_shop)

            // If the shop has initials, display them and hide the icon
            if (!shop.initials.isNullOrEmpty()) {
                shopIconInitials.text = shop.initials
                shopIconInitials.setTextColor(color)
                shopIconInitials.visibility = View.VISIBLE
                shopIconImageView.visibility = View.INVISIBLE
            } else {  // Else, display the icon and hide the initials
                shopIconImageView.setImageResource(shop.iconRef)
                shopIconImageView.setColorFilter(color)
                shopIconImageView.visibility = View.VISIBLE
                shopIconInitials.visibility = View.GONE
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
            badgeCountTextView.text = shopWithItemCount.itemCount.toString()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shopWithItemCount = getItem(position)
        // Bind the shop and item count data to the view holder
        holder.bind(shopWithItemCount)
    }


    class ShopDiffCallback : DiffUtil.ItemCallback<ShopWithItemCount>() {
        override fun areItemsTheSame(oldItem: ShopWithItemCount, newItem: ShopWithItemCount): Boolean {
            return oldItem.shop.id == newItem.shop.id
        }

        override fun areContentsTheSame(oldItem: ShopWithItemCount, newItem: ShopWithItemCount): Boolean {
            return oldItem == newItem
        }
    }
}



