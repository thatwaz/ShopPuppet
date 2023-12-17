package com.thatwaz.shoppuppet.data.repository

import android.util.Log
import com.thatwaz.shoppuppet.data.db.dao.ItemDao
import com.thatwaz.shoppuppet.data.db.dao.ItemShopCrossRefDao
import com.thatwaz.shoppuppet.domain.model.Item
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val itemShopCrossRefDao: ItemShopCrossRefDao
) {

    // Get all items
    suspend fun getAllItems(): List<Item> = itemDao.getAllItems()

    // Get items for a specific shop
    suspend fun getItemsByShop(shopId: Long): List<Item> = itemDao.getItemsByShop(shopId)

    // Insert a new item
    suspend fun insertItem(item: Item): Long = itemDao.insert(item)

//    suspend fun getItemById(itemId: Long): Item? = itemDao.getItemById(itemId)

    suspend fun deleteItemWithShops(item: Item) {
        // Step 1: Delete associations in item_shop_cross_ref
        val shopIds = itemShopCrossRefDao.getShopIdsForItem(item.id)
        for (shopId in shopIds) {
            itemShopCrossRefDao.deleteCrossRef(item.id, shopId)
        }

        // Step 2: Delete the item itself
        itemDao.deleteItem(item)
    }

    suspend fun deleteItemsWithShopAssociation(items: List<Item>) {
        val itemIds = items.map { it.id }
        itemDao.deleteItemsWithShopAssociation(itemIds)
    }


    // Update an item's details
//    suspend fun updateItem(item: Item) = itemDao.updateItem(item)


    suspend fun updateItem(item: Item) {
        Log.d("RepositoryLog", "Updating item in database: ${item.name}")
        Log.d("RepositoryLog", "Updating purchase status in database: ${item.isPurchased}")
        val result = itemDao.updateItem(item)
        Log.d("RepositoryLog", "Item update completed. Rows affected: $result")
    }

    // Delete an item
    suspend fun deleteItem(item: Item) = itemDao.deleteItem(item)


    suspend fun updateItemPriority(itemId: Long, isPriority: Boolean) {
        val item = itemDao.getItemById(itemId)
        if (item != null) {
            item.isPriorityItem = isPriority
            itemDao.updateItem(item)
            Log.d("RepositoryLog", "Updated item's priority status: $isPriority")
        }
    }
}

// Delete items by a specific shop
//    suspend fun deleteItemsByShop(shopId: Long) = itemDao.deleteItemsByShop(shopId)
//
//    // Get the count of items for a specific shop
//    suspend fun getItemsCountForShop(shopId: Long): Int = itemDao.getItemsCountForShop(shopId)
//
//    // Set item as purchased or not
//    suspend fun setItemPurchased(itemId: Long, purchased: Boolean) =
//        itemDao.setItemPurchasedStatus(itemId, purchased)
//
//    // Get all purchased items
//    suspend fun getPurchasedItems(): List<Item> = itemDao.getPurchasedItems()


