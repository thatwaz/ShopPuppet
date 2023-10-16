package com.thatwaz.shoppuppet.util.mock

import com.thatwaz.shoppuppet.domain.model.Shop

object MockDataStore {
    private val mockShops: MutableList<Shop> by lazy { generateMockShops() }

    fun getShops(): List<Shop> = mockShops

    fun addShop(shop: Shop) {
        mockShops.add(shop)
    }

    private fun generateMockShops(): MutableList<Shop> {
        return mutableListOf(
//            Shop(name = "Shop 1", iconRef = "icon_grocery_store", colorRef = R.color.shop_blue),
//            Shop(name = "Shop 2", iconRef = "icon2", colorRef = R.color.shop_green),
//            Shop(name = "Shop 3", iconRef = "icon3", colorRef = R.color.shop_red)
            // ... Add as many mock shops as you want ...
        )
    }
}
