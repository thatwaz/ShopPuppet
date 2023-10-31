package com.thatwaz.shoppuppet.presentation.adapters

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.domain.model.ItemUiModel


class ListAdapter(
) : androidx.recyclerview.widget.ListAdapter<ItemUiModel, ListAdapter.ShoppingViewHolder>(DiffCallback) {

    class ShoppingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvItemName: TextView = itemView.findViewById(R.id.tv_item_name)
        val chipGroupShops: ChipGroup = itemView.findViewById(R.id.chipGroupShops)
        val imgDropdown: ImageView = itemView.findViewById(R.id.img_drop_down)


        fun bind(itemUiModel: ItemUiModel) {
            tvItemName.text = itemUiModel.itemName

            // Clear any existing chips
            chipGroupShops.removeAllViews()

            // Populate chips based on the shops
            itemUiModel.shopNames.forEach { shopName ->
                val chip = Chip(itemView.context)
                chip.setChipBackgroundColorResource(R.color.off_white)
                val strokeColor =
                    ContextCompat.getColor(itemView.context, R.color.shop_blue)
                chip.chipStrokeColor = ColorStateList.valueOf(strokeColor)
                chip.chipStrokeWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    1f,
                    itemView.resources.displayMetrics
                )
                chip.text = shopName.name
                chip.isClickable = false
                chip.isCheckable = false
                chipGroupShops.addView(chip)
            }

            imgDropdown.setOnClickListener {
                if (chipGroupShops.visibility == View.VISIBLE) {
                    chipGroupShops.visibility = View.GONE
                    imgDropdown.setImageDrawable(
                        ContextCompat.getDrawable(
                            it.context,
                            R.drawable.ic_arrow_down
                        )
                    )
                } else {
                    chipGroupShops.visibility = View.VISIBLE
                    imgDropdown.setImageDrawable(
                        ContextCompat.getDrawable(
                            it.context,
                            R.drawable.ic_arrow_up
                        )
                    )
                }
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ItemUiModel>() {
        override fun areItemsTheSame(oldItem: ItemUiModel, newItem: ItemUiModel): Boolean {
            // Implement this based on your needs
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: ItemUiModel, newItem: ItemUiModel): Boolean {
            // Implement this based on your needs
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
        return ShoppingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}


//class ListAdapter : androidx.recyclerview.widget.ListAdapter<ItemUiModel, ListAdapter.ShoppingViewHolder>(DiffCallback) {
//
//    class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvItemName: TextView = itemView.findViewById(R.id.tv_item_name)
//        val chipGroupShops: ChipGroup = itemView.findViewById(R.id.chipGroupShops)
//        val imgDropdown: ImageView = itemView.findViewById(R.id.img_drop_down)
//        val btnDeleteItem: Button = itemView.findViewById(R.id.btn_delete_item)
//
//        fun bind(itemUiModel: ItemUiModel) {
//            tvItemName.text = itemUiModel.itemName
//            btnDeleteItem.setOnClickListener {
//                viewModel.deleteItemWithShops(item)
//            }
//
//            // Clear any existing chips
//            chipGroupShops.removeAllViews()
//
//            // Populate chips based on the shops
//            itemUiModel.shopNames.forEach { shopName ->
//                val chip = Chip(itemView.context)
//                chip.setChipBackgroundColorResource(R.color.off_white)
//                val strokeColor = ContextCompat.getColor(itemView.context, R.color.shop_blue)
//                chip.chipStrokeColor = ColorStateList.valueOf(strokeColor)
//                chip.chipStrokeWidth = TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP,
//                    1f,
//                    itemView.resources.displayMetrics
//                )
//                chip.text = shopName.name
//                chip.isClickable = false
//                chip.isCheckable = false
//                chipGroupShops.addView(chip)
//            }
//
//            imgDropdown.setOnClickListener {
//                if (chipGroupShops.visibility == View.VISIBLE) {
//                    chipGroupShops.visibility = View.GONE
//                    imgDropdown.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            it.context,
//                            R.drawable.ic_arrow_down
//                        )
//                    )
//                } else {
//                    chipGroupShops.visibility = View.VISIBLE
//                    imgDropdown.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            it.context,
//                            R.drawable.ic_arrow_up
//                        )
//                    )
//                }
//            }
//        }
//    }
//
//    companion object DiffCallback : DiffUtil.ItemCallback<ItemUiModel>() {
//        override fun areItemsTheSame(oldItem: ItemUiModel, newItem: ItemUiModel): Boolean {
//            // Implement this based on your needs
//            return oldItem.itemId == newItem.itemId
//        }
//
//        override fun areContentsTheSame(oldItem: ItemUiModel, newItem: ItemUiModel): Boolean {
//            // Implement this based on your needs
//            return oldItem == newItem
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
//        return ShoppingViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind(item)
//    }
//}




//class ListAdapter(initialItems: List<ItemUiModel>) :
//    RecyclerView.Adapter<ListAdapter.ShoppingViewHolder>() {
//
//    private val items: MutableList<ItemUiModel> = initialItems.toMutableList()
//
//    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvItemName: TextView = itemView.findViewById(R.id.tv_item_name)
//        val chipGroupShops: ChipGroup = itemView.findViewById(R.id.chipGroupShops)
//        val imgDropdown: ImageView = itemView.findViewById(R.id.img_drop_down)
//
//        fun bind(itemUiModel: ItemUiModel) {
//            tvItemName.text = itemUiModel.itemName
//
//            // Clear any existing chips
//            chipGroupShops.removeAllViews()
//
//            // Populate chips based on the shops
//            itemUiModel.shopNames.forEach { shopName ->
//                val chip = Chip(itemView.context)
//                chip.setChipBackgroundColorResource(R.color.off_white)
//                val strokeColor = ContextCompat.getColor(itemView.context, R.color.shop_blue)
//                chip.chipStrokeColor = ColorStateList.valueOf(strokeColor)
//                chip.chipStrokeWidth = TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP,
//                    1f,
//                    itemView.resources.displayMetrics
//                )
//                chip.text = shopName
//                chip.isClickable = false
//                chip.isCheckable = false
//                chipGroupShops.addView(chip)
//            }
//
//            imgDropdown.setOnClickListener {
//                if (chipGroupShops.visibility == View.VISIBLE) {
//                    chipGroupShops.visibility = View.GONE
//                    imgDropdown.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            it.context,
//                            R.drawable.ic_arrow_down
//                        )
//                    )
//                } else {
//                    chipGroupShops.visibility = View.VISIBLE
//                    imgDropdown.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            it.context,
//                            R.drawable.ic_arrow_up
//                        )
//                    )
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
//        return ShoppingViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
//        holder.bind(items[position])
//    }
//
//    fun updateData(newData: List<ItemUiModel>) {
//        items.clear()
//        items.addAll(newData)
//        notifyDataSetChanged()
//    }
//
//    override fun getItemCount(): Int = items.size
//}


//class ListAdapter(initialItems: List<Item>, initialShopsMap: Map<Item, List<Shop>>) :
//    RecyclerView.Adapter<ListAdapter.ShoppingViewHolder>() {
//
//    private val items: MutableList<Item> = initialItems.toMutableList()
//    private val shopsMap: MutableMap<Item, List<Shop>> = initialShopsMap.toMutableMap()
//
//    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvItemName: TextView = itemView.findViewById(R.id.tv_item_name)
//        val chipGroupShops: ChipGroup = itemView.findViewById(R.id.chipGroupShops)
//        val imgDropdown: ImageView = itemView.findViewById(R.id.img_drop_down)
//
//        fun bind(item: Item) {
//            tvItemName.text = item.name
//
//            // Clear any existing chips
//            chipGroupShops.removeAllViews()
//
//            // Populate chips based on the shops
//            shopsMap[item]?.forEach { shop ->
//                val chip = Chip(itemView.context)
//                chip.setChipBackgroundColorResource(R.color.off_white)
//                val strokeColor = ContextCompat.getColor(itemView.context, R.color.shop_blue)
//                chip.chipStrokeColor = ColorStateList.valueOf(strokeColor)
//                chip.chipStrokeWidth = TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP,
//                    1f,
//                    itemView.resources.displayMetrics
//                )
//                chip.text = shop.name
//                chip.isClickable = false
//                chip.isCheckable = false
//                chipGroupShops.addView(chip)
//            }
//
//            imgDropdown.setOnClickListener {
//                if (chipGroupShops.visibility == View.VISIBLE) {
//                    chipGroupShops.visibility = View.GONE
//                    imgDropdown.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            it.context,
//                            R.drawable.ic_arrow_down
//                        )
//                    )
//                } else {
//                    chipGroupShops.visibility = View.VISIBLE
//                    imgDropdown.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            it.context,
//                            R.drawable.ic_arrow_up
//                        )
//                    )
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
//        return ShoppingViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
//        holder.bind(items[position])
//    }
//
//    fun updateData(newData: List<Item>, newShopsMap: Map<Item, List<Shop>>) {
//        items.clear()
//        shopsMap.clear()
//        items.addAll(newData)
//        shopsMap.putAll(newShopsMap)
//        notifyDataSetChanged()
//    }
//
//    override fun getItemCount(): Int = items.size
//}






//class ListAdapter(initialItems: List<ShoppingItem>) :
//    RecyclerView.Adapter<ListAdapter.ShoppingViewHolder>() {
//
//    private val items: MutableList<ShoppingItem> = initialItems.toMutableList()
//
//
//    // Inner class for the ViewHolder
//    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvItemName: TextView = itemView.findViewById(R.id.tv_item_name)
//        val chipGroupShops: ChipGroup = itemView.findViewById(R.id.chipGroupShops)
//        val imgDropdown: ImageView = itemView.findViewById(R.id.img_drop_down)
//
//        fun bind(item: ShoppingItem) {
//            tvItemName.text = item.name
//
//            // Clear any existing chips (important for recycling views)
//            chipGroupShops.removeAllViews()
//
//            // Populate chips based on the shops
//            for (shop in item.shops) {
//                val chip = Chip(itemView.context)
//                chip.setChipBackgroundColorResource(R.color.off_white)
//                val strokeColor = ContextCompat.getColor(itemView.context, R.color.shop_blue)
//                chip.chipStrokeColor = ColorStateList.valueOf(strokeColor)
//                chip.chipStrokeWidth = TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_DIP,
//                    1f,
//                    itemView.resources.displayMetrics
//                )
//                chip.text = shop
//                chip.isClickable = false
//                chip.isCheckable = false
//                chipGroupShops.addView(chip)
//            }
//
//            imgDropdown.setOnClickListener {
//                if (chipGroupShops.visibility == View.VISIBLE) {
//                    chipGroupShops.visibility = View.GONE
//                    imgDropdown.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            it.context,
//                            R.drawable.ic_arrow_down
//                        )
//                    )  // Change to your arrow pointing down image
//                } else {
//                    chipGroupShops.visibility = View.VISIBLE
//                    imgDropdown.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            it.context,
//                            R.drawable.ic_arrow_up
//                        )
//                    )  // Change to your arrow pointing up image
//                }
//            }
//
//
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.shopping_item, parent, false)
//        return ShoppingViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
//        // Use the bind method to set up the views for the current item
//        holder.bind(items[position])
//    }
//
//    fun updateData(newData: List<Item>) {
//        items.clear()
//        items.addAll(newData)
//        Log.d("Adapter", "Updated data. Item count: ${items.size}")
//        notifyDataSetChanged()
//    }
//
//
//    override fun getItemCount(): Int {
//        Log.d("Adapter", "Getting item count: ${items.size}")
//        return items.size
//    }
//
//
//}

