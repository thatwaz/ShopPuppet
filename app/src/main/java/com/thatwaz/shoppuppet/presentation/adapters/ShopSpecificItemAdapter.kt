package com.thatwaz.shoppuppet.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.domain.model.Item


class ShopSpecificItemAdapter(
    private val onItemCheckedListener: (Item) -> Unit
) : RecyclerView.Adapter<ShopSpecificItemAdapter.ItemViewHolder>() {
    private val unpurchasedItems: MutableList<Item> = mutableListOf()
    private val purchasedItems: MutableList<Item> = mutableListOf()

    init {
        Log.d("ShopSpecificItemAdapter", "Adapter created")
    }


    fun updateUnpurchasedCheckedItems(newCheckedItems: List<Item>) {
        val diffCallback = ShopSpecificItemDiffCallback(unpurchasedItems, newCheckedItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        unpurchasedItems.clear()
        unpurchasedItems.addAll(newCheckedItems)

        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_shop_specific,
            parent,
            false
        )
        Log.d("ShopSpecificItemAdapter", "onCreateViewHolder called")
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (position < unpurchasedItems.size) {
            val item = unpurchasedItems[position]
            holder.bind(item, true) // Pass the checked state
        } else {
            val item = purchasedItems[position - unpurchasedItems.size]
            holder.bind(item, true) // Pass the checked state
        }
        Log.d("ShopSpecificItemAdapter", "onBindViewHolder called for position $position")
    }

    override fun getItemCount(): Int {
        return unpurchasedItems.size + purchasedItems.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.tv_item_for_shop)
        private val checkBox: CheckBox = itemView.findViewById(R.id.cb_purchased)

        fun bind(item: Item, isChecked: Boolean) {
            itemNameTextView.text = item.name
            checkBox.isChecked = !isChecked // Set the initial checked state based on the parameter

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onItemCheckedListener(item)
                    Log.d("ShopSpecificItemAdapter", "Checkbox clicked for item ${item.id}")
                } else {
                    // Handle the case where the checkbox is unchecked
                    // For example, remove the item from the checked items list
                    // You can implement this logic here if needed.
                }
            }
        }

    }

    private class ShopSpecificItemDiffCallback(
        private val oldList: List<Item>,
        private val newList: List<Item>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            val areItemsTheSame = oldItem.id == newItem.id // Use a unique identifier for items
            Log.d("DiffCallback", "areItemsTheSame: ${oldItem.id} == ${newItem.id}, position: $oldItemPosition -> $newItemPosition")
            return areItemsTheSame
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            val areContentsTheSame = oldItem == newItem
            Log.d("DiffCallback", "areContentsTheSame: $oldItem == $newItem, position: $oldItemPosition -> $newItemPosition")
            return areContentsTheSame
        }
    }
}

//class ShopSpecificItemAdapter(
//    private val onItemCheckedListener: (Item) -> Unit
//) : RecyclerView.Adapter<ShopSpecificItemAdapter.ItemViewHolder>() {
//
//    private val items: MutableList<Item> = mutableListOf()
//    private var checkedItems: List<Item> = emptyList()
//
//    fun submitItems(newItems: List<Item>) {
//        val diffCallback = ShopSpecificItemDiffCallback(items, newItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        items.clear()
//        items.addAll(newItems)
//        diffResult.dispatchUpdatesTo(this)
//        Log.d("ShopSpecificItemAdapter", "submitItems called with ${newItems.size} items")
//    }
//    fun getItems(): List<Item> {
//        return items
//    }
//
//
//    fun updateCheckedItems(newCheckedItems: List<Item>) {
//        Log.d("Adapter", "updateCheckedItems called")
//        Log.d("Adapter", "Before update, checkedItems: $checkedItems")
//
//        val diffCallback = ShopSpecificItemDiffCallback(checkedItems, newCheckedItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        checkedItems = newCheckedItems
//        diffResult.dispatchUpdatesTo(this)
//
//        Log.d("Adapter", "After update, checkedItems: $checkedItems")
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(
//            R.layout.item_shop_specific,
//            parent,
//            false
//        )
//        return ItemViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val item = items[position]
//        holder.bind(item, checkedItems.contains(item)) // Pass the checked state
//        Log.d("ShopSpecificItemAdapter", "onBindViewHolder called for position $position")
//
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val itemNameTextView: TextView = itemView.findViewById(R.id.tv_item_for_shop)
//        private val checkBox: CheckBox = itemView.findViewById(R.id.cb_purchased)
//
//        fun bind(item: Item, isChecked: Boolean) {
//            itemNameTextView.text = item.name
//            checkBox.isChecked = isChecked
//
//            checkBox.setOnCheckedChangeListener { _, isChecked ->
//                if (isChecked) {
//                    onItemCheckedListener(item)
//                    Log.d("ShopSpecificItemAdapter", "Checkbox clicked for item ${item.id}")
//                } else {
//                    // Handle the case where the checkbox is unchecked
//                    // For example, remove the item from the checked items list
////                    val newCheckedItems = checkedItems.toMutableList()
////                    newCheckedItems.remove(item)
////                    updateCheckedItems(newCheckedItems) // Update the dataset and notify the adapter
//                }
//            }
//
//
////            checkBox.setOnCheckedChangeListener { _, isChecked ->
////                if (isChecked) {
////                    onItemCheckedListener(item)
////                    Log.d("ShopSpecificItemAdapter", "Checkbox clicked for item ${item.id}")
////                }
////            }
//        }
//    }
//
//    private class ShopSpecificItemDiffCallback(
//        private val oldList: List<Item>,
//        private val newList: List<Item>
//    ) : DiffUtil.Callback() {
//
//        override fun getOldListSize(): Int = oldList.size
//        override fun getNewListSize(): Int = newList.size
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldList[oldItemPosition]
//            val newItem = newList[newItemPosition]
//            val areItemsTheSame = oldItem.id == newItem.id // Use a unique identifier for items
//            Log.d("DiffCallback", "areItemsTheSame: ${oldItem.id} == ${newItem.id}, position: $oldItemPosition -> $newItemPosition")
//            return areItemsTheSame
//        }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldList[oldItemPosition]
//            val newItem = newList[newItemPosition]
//            val areContentsTheSame = oldItem == newItem
//            Log.d("DiffCallback", "areContentsTheSame: $oldItem == $newItem, position: $oldItemPosition -> $newItemPosition")
//            return areContentsTheSame
//        }
//    }
//
//}




