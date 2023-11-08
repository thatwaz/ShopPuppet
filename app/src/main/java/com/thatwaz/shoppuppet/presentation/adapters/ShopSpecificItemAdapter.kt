package com.thatwaz.shoppuppet.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.domain.model.Item

class ShopSpecificItemAdapter : RecyclerView.Adapter<ShopSpecificItemAdapter.ItemViewHolder>() {

    private val items: MutableList<Item> = mutableListOf()

    // Submit a list of shop-specific items to display
    fun submitItems(newItems: List<Item>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_shop_specific,
            parent,
            false
        )
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        Log.i("DOH!","There are ${items.size} items in this shop")
        return items.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.tv_item_for_shop)
        // Define other views you want to bind here

        fun bind(item: Item) {
            itemNameTextView.text = item.name
            Log.i("DOH!","Items are ${item.name}")
            // Bind other item properties to corresponding views here
        }
    }
}