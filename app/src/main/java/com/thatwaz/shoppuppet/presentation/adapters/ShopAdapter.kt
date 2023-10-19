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
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.domain.model.Shop


class ShopAdapter(private val shops: List<Shop>) :
    RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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


//        @RequiresApi(Build.VERSION_CODES.P)
//        fun bind(shop: Shop) {
//            shopNameTextView.text = shop.name
//            shopIconImageView.setImageResource(shop.iconRef)
//            val cardView: MaterialCardView = itemView.findViewById(R.id.cv_shop)
//
//            val color = ContextCompat.getColor(itemView.context, shop.colorResId)
//            shopIconImageView.setColorFilter(color)
//
//            val shadowDrawable = ContextCompat.getDrawable(
//                itemView.context,
//                R.drawable.custom_shadow
//            ) as LayerDrawable
//            val shadowLayer = shadowDrawable.getDrawable(0) as GradientDrawable
//            shadowLayer.setColor(color)
//            cardView.background = shadowDrawable
//            cardView.outlineSpotShadowColor = color
//            cardView.outlineAmbientShadowColor = color
//
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(shops[position])

    }

    override fun getItemCount() = shops.size
}


//class ShopAdapter(private var shops: List<Shop>) :
//    RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {
//
//    // ViewHolder class that represents individual list items
//    class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val shopName: TextView = itemView.findViewById(R.id.shop_name)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
//        return ShopViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
//        val shop = shops[position]
//        holder.shopName.text = shop.name
//    }
//
//    override fun getItemCount() = shops.size
//
//    fun updateShops(newShops: List<Shop>) {
//        shops = newShops
//        notifyDataSetChanged()
//    }
//}
