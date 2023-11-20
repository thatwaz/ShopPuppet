package com.thatwaz.shoppuppet.presentation.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.databinding.ItemShopSpecificBinding
import com.thatwaz.shoppuppet.domain.model.Item



class ShopSpecificItemAdapter(
    private val colorStateList: ColorStateList,
    private val onItemCheckedListener: (Item) -> Unit
) : ListAdapter<Item, ShopSpecificItemAdapter.ItemViewHolder>(ShopSpecificItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemShopSpecificBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position) // get item from ListAdapter
        holder.bind(item)
    }

    inner class ItemViewHolder(private val binding: ItemShopSpecificBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.tvItemForShop.text = item.name
            binding.cbPurchased.isChecked = item.isPurchased
            binding.cbPurchased.buttonTintList = colorStateList

            // Clear previous listeners and set new ones
            binding.cbPurchased.setOnCheckedChangeListener(null)
            binding.cbPurchased.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onItemCheckedListener(item)
                }
            }
        }
    }

    companion object {
        private val ShopSpecificItemDiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}


//class ShopSpecificItemAdapter(
//    private val onItemCheckedListener: (Item) -> Unit
//) : RecyclerView.Adapter<ShopSpecificItemAdapter.ItemViewHolder>() {
//
//    private val items: MutableList<Item> = mutableListOf()
//
//    fun updateItems(newItems: List<Item>) {
//        Log.d(
//            "AdapterLog",
//            "Updating items. New items: ${newItems.map { it.name + ": " + it.isPurchased }}"
//        )
//        val diffCallback = ShopSpecificItemDiffCallback(items, newItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        items.clear()
//        items.addAll(newItems)
//
//        diffResult.dispatchUpdatesTo(this)
//        Log.d("AdapterLog", "Items after update: ${items.map { it.name + ": " + it.isPurchased }}")
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        val binding =
//            ItemShopSpecificBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ItemViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val item = items[position]
//        Log.i("DOH!", "items are now $item")
//        holder.bind(item)
//    }
//
//    override fun getItemCount(): Int {
////        Log.i("DOH!","Item count is ${items.size}")
//        return items.size
//    }
//
//    inner class ItemViewHolder(private val binding: ItemShopSpecificBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(item: Item) {
//            binding.tvItemForShop.text = item.name
//            binding.cbPurchased.isChecked = item.isPurchased
//
//            // Clear previous listeners and set new ones
//            binding.cbPurchased.setOnCheckedChangeListener(null)
//            binding.cbPurchased.setOnCheckedChangeListener { _, isChecked ->
//                if (isChecked) {
//                    onItemCheckedListener(item)
//                }
//            }
//        }
//    }
//
//    private class ShopSpecificItemDiffCallback(
//        private val oldItems: List<Item>,
//        private val newItems: List<Item>
//    ) : DiffUtil.Callback() {
//
//        override fun getOldListSize(): Int = oldItems.size
//        override fun getNewListSize(): Int = newItems.size
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
//            oldItems[oldItemPosition].id == newItems[newItemPosition].id
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
//            oldItems[oldItemPosition] == newItems[newItemPosition]
//    }
//}






