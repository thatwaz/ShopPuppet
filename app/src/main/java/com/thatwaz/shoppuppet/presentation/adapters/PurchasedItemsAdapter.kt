package com.thatwaz.shoppuppet.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thatwaz.shoppuppet.databinding.ItemShopSpecificBinding
import com.thatwaz.shoppuppet.domain.model.Item
import androidx.recyclerview.widget.ListAdapter


class PurchasedItemsAdapter(
    private val onItemCheckedListener: (Item) -> Unit
) : ListAdapter<Item, PurchasedItemsAdapter.ItemViewHolder>(PurchasedItemsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemShopSpecificBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ItemViewHolder(private val binding: ItemShopSpecificBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.tvItemForShop.text = item.name
            binding.cbPurchased.isChecked = item.isPurchased

            // Clear previous listeners and set new ones
            binding.cbPurchased.setOnCheckedChangeListener(null)
            binding.cbPurchased.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    onItemCheckedListener(item)
                }
            }
        }
    }

    companion object {
        private val PurchasedItemsDiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}


//class PurchasedItemsAdapter(
//    private val onItemCheckedListener: (Item) -> Unit
//) : RecyclerView.Adapter<PurchasedItemsAdapter.ItemViewHolder>() {
//
//    private val items: MutableList<Item> = mutableListOf()
//
//    fun submitItems(newItems: List<Item>) {
//        Log.d("PurchasedAdapterLog", "Submitting new items: ${newItems.map { it.name + ": " + it.isPurchased }}")
//
//        val diffCallback = PurchasedItemsDiffCallback(items, newItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        items.clear()
//        items.addAll(newItems)
//        diffResult.dispatchUpdatesTo(this)
//        Log.d("PurchasedAdapterLog", "Items after submission: ${items.map { it.name + ": " + it.isPurchased }}")
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(
//            R.layout.item_shop_specific,
//            parent,
//            false
//        )
//        Log.d("PurchasedAdapterLog","on create view holder called")
//        return ItemViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val item = items[position]
//        Log.d("PurchasedAdapterLog","Item is $item")
//        holder.bind(item)
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val itemNameTextView: TextView = itemView.findViewById(R.id.tv_item_for_shop)
//        private val checkBox: CheckBox = itemView.findViewById(R.id.cb_purchased)
//
//        fun bind(item: Item) {
//            itemNameTextView.text = item.name
//            checkBox.isChecked = item.isPurchased
//
//            Log.d("ViewHolderLog", "Purchased item: ${item.name}, isPurchased: ${item.isPurchased}")
//
//            checkBox.setOnCheckedChangeListener(null) // Clear previous listeners
//            checkBox.setOnCheckedChangeListener { _, isChecked ->
//                if (!isChecked) {
//                    onItemCheckedListener(item)
//                }
//            }
//        }
//    }
//
//    private class PurchasedItemsDiffCallback(
//        private val oldItems: List<Item>,
//        private val newItems: List<Item>
//    ) : DiffUtil.Callback() {
//
//        override fun getOldListSize(): Int = oldItems.size
//        override fun getNewListSize(): Int = newItems.size
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldItems[oldItemPosition]
//            val newItem = newItems[newItemPosition]
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldItems[oldItemPosition]
//            val newItem = newItems[newItemPosition]
//            return oldItem == newItem
//        }
//    }
//}



//class PurchasedItemsAdapter(
//    private val onItemCheckedListener: (Item) -> Unit
//) : RecyclerView.Adapter<PurchasedItemsAdapter.ItemViewHolder>() {
//
//    private val items: MutableList<Item> = mutableListOf()
//
//    fun submitItems(newItems: List<Item>) {
//        val diffCallback = PurchasedItemsDiffCallback(items, newItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        items.clear()
//        items.addAll(newItems)
//        diffResult.dispatchUpdatesTo(this)
//    }
//
//    fun updatePurchasedCheckedItems(newCheckedItems: List<Item>) {
//        val diffCallback = PurchasedItemsDiffCallback(items, newCheckedItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        items.clear()
//        items.addAll(newCheckedItems)
//
//        diffResult.dispatchUpdatesTo(this)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(
//            R.layout.item_shop_specific, // Use the same item layout as ShopSpecificItemAdapter
//            parent,
//            false
//        )
//        return ItemViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val item = items[position]
//        holder.bind(item)
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
//        fun bind(item: Item) {
//            itemNameTextView.text = item.name
//            checkBox.isChecked = true // Since these are purchased items, they should be checked
//
//            // Handle checkbox clicks
//            checkBox.setOnCheckedChangeListener(null) // Clear previous listeners
//            checkBox.setOnClickListener {
//                onItemCheckedListener(item)
//            }
//        }
//    }
//
//    // DiffUtil callback for calculating item differences
//    private class PurchasedItemsDiffCallback(
//        private val oldItems: List<Item>,
//        private val newItems: List<Item>
//    ) : DiffUtil.Callback() {
//
//        override fun getOldListSize(): Int {
//            return oldItems.size
//        }
//
//        override fun getNewListSize(): Int {
//            return newItems.size
//        }
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldItems[oldItemPosition]
//            val newItem = newItems[newItemPosition]
//            return oldItem.id == newItem.id // Use a unique identifier for items
//        }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldItems[oldItemPosition]
//            val newItem = newItems[newItemPosition]
//            return oldItem == newItem // Check if the item contents are the same
//        }
//    }
//}


//class PurchasedItemsAdapter(
//    private val onItemCheckedListener: (Item) -> Unit
//) : RecyclerView.Adapter<PurchasedItemsAdapter.ItemViewHolder>() {
//
//    private val items: MutableList<Item> = mutableListOf()
//
//
//    fun submitItems(newItems: List<Item>) {
//        val diffCallback = PurchasedItemsDiffCallback(items, newItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        items.clear()
//        items.addAll(newItems)
//        diffResult.dispatchUpdatesTo(this)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(
//            R.layout.item_shop_specific, // Use the same item layout as ShopSpecificItemAdapter
//            parent,
//            false
//        )
//        return ItemViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val item = items[position]
//        holder.bind(item)
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
//        fun bind(item: Item) {
//            itemNameTextView.text = item.name
//            checkBox.isChecked = true // Since these are purchased items, they should be checked
//
//            // Handle checkbox clicks
//            checkBox.setOnCheckedChangeListener(null) // Clear previous listeners
//            checkBox.setOnClickListener {
//                onItemCheckedListener(item)
//            }
//        }
//    }
//
//    // DiffUtil callback for calculating item differences
//    private class PurchasedItemsDiffCallback(
//        private val oldItems: List<Item>,
//        private val newItems: List<Item>
//    ) : DiffUtil.Callback() {
//
//        override fun getOldListSize(): Int {
//            return oldItems.size
//        }
//
//        override fun getNewListSize(): Int {
//            return newItems.size
//        }
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldItems[oldItemPosition]
//            val newItem = newItems[newItemPosition]
//            return oldItem.id == newItem.id // Use a unique identifier for items
//        }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldItems[oldItemPosition]
//            val newItem = newItems[newItemPosition]
//            return oldItem == newItem // Check if the item contents are the same
//        }
//    }
//}

//class PurchasedItemsAdapter(
//    private val onItemCheckedListener: (Item) -> Unit
//) : RecyclerView.Adapter<PurchasedItemsAdapter.ItemViewHolder>() {
//
//    private val items: MutableList<Item> = mutableListOf()
//    private var checkedItems: List<Item> = emptyList()
//
//    fun submitItems(newItems: List<Item>) {
//        val diffCallback = PurchasedItemsDiffCallback(items, newItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        items.clear()
//        items.addAll(newItems)
//        diffResult.dispatchUpdatesTo(this)
//    }
//    fun updateCheckedItems(newCheckedItems: List<Item>) {
//        val diffCallback = PurchasedItemsDiffCallback(checkedItems, newCheckedItems)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//
//        checkedItems = newCheckedItems
//        diffResult.dispatchUpdatesTo(this)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(
//            R.layout.item_shop_specific, // Use the same item layout as ShopSpecificItemAdapter
//            parent,
//            false
//        )
//        return ItemViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val item = items[position]
//        val isChecked = checkedItems.contains(item)
//        holder.bind(item, isChecked)
//    }
//
//
//    override fun getItemCount(): Int {
//        Log.i("DOH","Purchased list size is ${items.size}")
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
////            itemView.setOnClickListener {
////                onItemClickListener.invoke(item)
////            }
//
//            // Handle checkbox clicks
//            checkBox.setOnCheckedChangeListener { _, isChecked ->
//                if (isChecked) {
//                    onItemCheckedListener(item)
//                }
//            }
//        }
//    }
//
//
//    // DiffUtil callback for calculating item differences
//    private class PurchasedItemsDiffCallback(
//        private val oldItems: List<Item>,
//        private val newItems: List<Item>
//    ) : DiffUtil.Callback() {
//
//        override fun getOldListSize(): Int {
//            return oldItems.size
//        }
//
//        override fun getNewListSize(): Int {
//            return newItems.size
//        }
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldItems[oldItemPosition]
//            val newItem = newItems[newItemPosition]
//            return oldItem.id == newItem.id // Use a unique identifier for items
//        }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldItems[oldItemPosition]
//            val newItem = newItems[newItemPosition]
//            return oldItem == newItem // Check if the item contents are the same
//        }
//    }
//}

