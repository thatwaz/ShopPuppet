package com.thatwaz.shoppuppet.domain.model

data class ShopWithPriorityStatus(
    // Include all fields from the Shop entity
    val id: Long,
    val name: String,
    val iconResName: String,
    val colorResName: String,
    val initials: String? = null,
    // This field indicates whether the shop has priority items
    val hasPriorityItem: Boolean
)

