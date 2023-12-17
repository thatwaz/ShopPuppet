package com.thatwaz.shoppuppet.domain.model

data class ShopWithItemCountAndPriority(
    val id: Long,
    val name: String,
    val iconResName: String,
    val colorResName: String,
    val initials: String? = null,
    val itemCount: Int,
    val hasPriorityItem: Boolean
)

