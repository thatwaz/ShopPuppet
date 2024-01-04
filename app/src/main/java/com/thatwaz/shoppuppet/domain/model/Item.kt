package com.thatwaz.shoppuppet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    // set description in case it's needed in the future
    var description: String,
    var isPurchased: Boolean = false,
    var isPriorityItem: Boolean = false,
    var lastPurchasedDate: Date? = null, // This will store the last purchase date. Null means the item hasn't been purchased yet.
    val purchaseCount: Int = 0  // This will store how often an item has been purchased.
)





