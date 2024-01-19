package com.thatwaz.shoppuppet.domain.model

data class ShopWithPriorityStatus(
    val id: Long,
    val name: String,
    val iconResName: String,
    val colorResName: String,
    val initials: String? = null,
    val hasPriorityItem: Boolean
)

