package com.thatwaz.shoppuppet.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "item_shop_cross_ref",
    primaryKeys = ["itemId", "shopId"],
    foreignKeys = [
        ForeignKey(entity = Item::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Shop::class,
            parentColumns = ["id"],
            childColumns = ["shopId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class ItemShopCrossRef(
    val itemId: Long,
    val shopId: Long
)



