package com.thatwaz.shoppuppet.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.domain.model.Shop


class ShopAdapter(private val shops: List<Shop>) : RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shopIconImageView: ImageView = itemView.findViewById(R.id.shop_icon)
        val shopNameTextView: TextView = itemView.findViewById(R.id.shop_name)

        fun bind(shop: Shop) {
            shopNameTextView.text = shop.name
            shopIconImageView.setImageResource(shop.iconRef)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

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
