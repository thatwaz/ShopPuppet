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



class ShopAdapter : ListAdapter<Shop, ShopAdapter.ShopViewHolder>(ShopDiffCallback())
 {

     interface OnShopItemClickListener {
         fun onShopItemClick(shop: Shop)
     }

     var onShopItemClickListener: OnShopItemClickListener? = null


     inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


         init {
             itemView.setOnClickListener {
                 val position = adapterPosition
                 if (position != RecyclerView.NO_POSITION) {
                     val shop = getItem(position)
                     onShopItemClickListener?.onShopItemClick(shop)
                 }
             }
         }
        val shopIconImageView: ImageView = itemView.findViewById(R.id.shop_icon)
        val shopNameTextView: TextView = itemView.findViewById(R.id.shop_name)
        val shopIconInitials: TextView = itemView.findViewById(R.id.shop_icon_initials)

        @RequiresApi(Build.VERSION_CODES.P)
        fun bind(shop: Shop) {
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
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(getItem(position))
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



