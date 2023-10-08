package com.thatwaz.shoppuppet.data.repository

import com.thatwaz.shoppuppet.data.db.dao.ShopDao
import com.thatwaz.shoppuppet.domain.model.Shop
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(private val shopDao: ShopDao) {

    // Insert a new shop
    suspend fun insertShop(shop: Shop): Long = shopDao.insert(shop)

    // Retrieve all shops
    suspend fun getAllShops(): List<Shop> = shopDao.getAllShops()

    // Retrieve a shop by its ID
    suspend fun getShopById(shopId: Long): Shop? = shopDao.getShopById(shopId)

    // Update a shop's details
    suspend fun updateShop(shop: Shop) = shopDao.updateShop(shop)

    // Delete a shop
    suspend fun deleteShop(shop: Shop) = shopDao.deleteShop(shop)

    // Retrieve the number of items associated with a shop (assuming this method exists in your ShopDao)
    suspend fun getItemsCountForShop(shopId: Long): Int = shopDao.getItemsCountForShop(shopId)
}
