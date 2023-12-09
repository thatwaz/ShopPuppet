package com.thatwaz.shoppuppet.domain.model

data class ShopWithItemCountAndPriority(
    val id: Long,
    val name: String,
    val iconRef: Int,
    val colorResId: Int,
    val initials: String? = null,
    val itemCount: Int,
    val hasPriorityItem: Boolean
)

