package com.thatwaz.shoppuppet.data.repository

import com.thatwaz.shoppuppet.data.db.dao.ItemDao
import com.thatwaz.shoppuppet.domain.model.Item
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(private val itemDao: ItemDao) {

    // Get all items
    suspend fun getAllItems(): List<Item> = itemDao.getAllItems()

    // Get items for a specific shop
    suspend fun getItemsByShop(shopId: Long): List<Item> = itemDao.getItemsByShop(shopId)

    // Insert a new item
    suspend fun insertItem(item: Item): Long = itemDao.insert(item)

    suspend fun getItemById(itemId: Long): Item? = itemDao.getItemById(itemId)





    // Update an item's details
    suspend fun updateItem(item: Item) = itemDao.updateItem(item)

    // Delete an item
    suspend fun deleteItem(item: Item) = itemDao.deleteItem(item)

    // Delete items by a specific shop
    suspend fun deleteItemsByShop(shopId: Long) = itemDao.deleteItemsByShop(shopId)

    // Get the count of items for a specific shop
    suspend fun getItemsCountForShop(shopId: Long): Int = itemDao.getItemsCountForShop(shopId)

    // Set item as purchased or not
    suspend fun setItemPurchased(itemId: Long, purchased: Boolean) = itemDao.setItemPurchasedStatus(itemId, purchased)

    // Get all purchased items
    suspend fun getPurchasedItems(): List<Item> = itemDao.getPurchasedItems()
}

