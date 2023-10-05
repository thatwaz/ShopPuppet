package com.thatwaz.shoppuppet.domain.model



data class ShoppingItem(
    val id: Long,             // A unique identifier, useful for database operations.
    val name: String,         // The name of the item.
    val quantity: Int,        // Quantity of the item to purchase.
    val checked: Boolean = false,  // Whether the item has been bought/checked off or not.
    val notes: String? = null     // Optional notes for the item.
)

