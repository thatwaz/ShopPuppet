package com.thatwaz.shoppuppet.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.presentation.viewmodel.SelectedShopsViewModel

class ShopSelectionAdapter(
    var onItemClick: (Shop) -> Unit,
    private val selectedShopsViewModel: SelectedShopsViewModel
) :
    ListAdapter<Shop, ShopSelectionAdapter.ViewHolder>(ShopAdapter.ShopDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_shops_to_tag, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("DOH!", "onBindViewHolder called for position $position")
        val shop = getItem(position)
        Log.d("DOH!", "Binding shop at position $position, ID: ${shop.id}, Name: ${shop.name}")
        holder.bind(shop)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkbox: CheckBox = itemView.findViewById(R.id.cb_tag_shop)
        private val shopNameTextView: TextView = itemView.findViewById(R.id.tv_shop_to_tag)

        fun bind(shop: Shop) {
            shopNameTextView.text = shop.name


            checkbox.setOnCheckedChangeListener { _, isChecked ->
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
}



//class ShopSelectionAdapter(private val onItemClick: (Shop) -> Unit) :
//    RecyclerView.Adapter<ShopSelectionAdapter.ShopViewHolder>() {
//
//    private var shopList: List<Shop> = emptyList()
//
//    inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val shopNameTextView: TextView = itemView.findViewById(R.id.tv_shop_to_tag)
//        private val checkBoxShop: CheckBox = itemView.findViewById(R.id.cb_tag_shop)
//
//        fun bind(shop: Shop) {
//            shopNameTextView.text = shop.name
//
//            // Set an onClickListener for the checkbox to handle shop selection
//            checkBoxShop.setOnClickListener {
//                onItemClick(shop)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_shops_to_tag, parent, false)
//        return ShopViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
//        val shop = shopList[position]
//        holder.bind(shop)
//    }
//
//    override fun getItemCount(): Int {
//        return shopList.size
//    }
//
//    fun submitList(shops: List<Shop>) {
//        shopList = shops
//        notifyDataSetChanged()
//    }
//}
