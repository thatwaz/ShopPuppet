package com.thatwaz.shoppuppet.domain.model

data class ShopWithPriorityStatus(
    // Include all fields from the Shop entity
    val id: Long,
    val name: String,
    val iconRef: Int,
    val colorResId: Int,
    val initials: String? = null,
    // This field indicates whether the shop has priority items
    val hasPriorityItem: Boolean
)

