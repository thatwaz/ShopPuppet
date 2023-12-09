package com.thatwaz.shoppuppet.domain.model



data class ShopWithItemCount(
    val shop: Shop,
    val itemCount: Int,
    val hasPriorityItem: Boolean  // Add this property
)

//data class ShopWithItemCount(
//    val shop: Shop,
//    val itemCount: Int
//)

