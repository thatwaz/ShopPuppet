package com.thatwaz.shoppuppet.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var isPurchased: Boolean = false,
    var isPriorityItem: Boolean = false,
    var isSoftDeleted: Boolean = false,
    var lastPurchasedDate: Date? = null
)





