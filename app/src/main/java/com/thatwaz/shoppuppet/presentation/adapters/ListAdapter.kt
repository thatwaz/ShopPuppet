package com.thatwaz.shoppuppet.presentation.adapters

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.thatwaz.shoppuppet.R
import com.thatwaz.shoppuppet.domain.model.ItemUiModel


class ListAdapter(private val itemClickListener: ItemClickListener
) : androidx.recyclerview.widget.ListAdapter<ItemUiModel, ListAdapter.ShoppingViewHolder>(DiffCallback) {

    interface ItemClickListener {
        fun onDeleteItem(item: ItemUiModel)
    }

    class ShoppingViewHolder(itemView: View, private val itemClickListener: ItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val tvItemName: TextView = itemView.findViewById(R.id.tv_item_name)
        val chipGroupShops: ChipGroup = itemView.findViewById(R.id.chipGroupShops)
        val imgDropdown: ImageView = itemView.findViewById(R.id.img_drop_down)
        val btnDeleteItem: ImageButton = itemView.findViewById(R.id.btn_delete_item)
        val imgPriorityStar: ImageView = itemView.findViewById(R.id.img_star)


        fun bind(itemUiModel: ItemUiModel) {
            tvItemName.text = itemUiModel.itemName

            if (itemUiModel.isPriorityItem) {
                imgPriorityStar.visibility = View.VISIBLE
            } else {
                imgPriorityStar.visibility = View.INVISIBLE
            }

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
            btnDeleteItem.setOnClickListener {
                itemClickListener.onDeleteItem(itemUiModel)
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
        return ShoppingViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}



