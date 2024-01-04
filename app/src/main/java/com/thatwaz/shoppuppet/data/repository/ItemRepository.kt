package com.thatwaz.shoppuppet.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.thatwaz.shoppuppet.data.db.dao.ItemDao
import com.thatwaz.shoppuppet.data.db.dao.ItemShopCrossRefDao
import com.thatwaz.shoppuppet.domain.model.Item
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val itemShopCrossRefDao: ItemShopCrossRefDao
) {

    // Get all items
    suspend fun getAllItems(): List<Item> = itemDao.getAllItems()

    fun getFrequentItems(): LiveData<List<Item>> {
        val thirtyDaysAgo = calculateThirtyDaysAgo()
        return itemDao.getFrequentItems(thirtyDaysAgo)
    }

    // Utility method to calculate the date 30 days ago
    private fun calculateThirtyDaysAgo(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -2)
        return calendar.timeInMillis
    }

    // Get items for a specific shop
    suspend fun getItemsByShop(shopId: Long): List<Item> = itemDao.getItemsByShop(shopId)

    // Insert a new item
    suspend fun insertItem(item: Item): Long = itemDao.insert(item)

    suspend fun getItemById(itemId: Long): Item? = itemDao.getItemById(itemId)

    /* This is used for deleting items directly from the main list */
    suspend fun deleteItemWithShops(item: Item) {
        // Step 1: Delete associations in item_shop_cross_ref
        val shopIds = itemShopCrossRefDao.getShopIdsForItem(item.id)
        for (shopId in shopIds) {
            itemShopCrossRefDao.deleteCrossRef(item.id, shopId)
        }

        // Step 2: Delete the item itself
        itemDao.deleteItem(item)
    }

    /* This is used for deleting items after they are marked as purchased */
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

    suspend fun editItemDetails(
        itemId: Long,
        newName: String?,
        newDescription: String?,
        newPriorityStatus: Boolean?
    ) {
        val currentItem = itemDao.getItemById(itemId)
        currentItem?.let { item ->
            newName?.let { item.name = it }
            newDescription?.let { item.description = it }
            newPriorityStatus?.let { item.isPriorityItem = it }

            updateItem(item)
            Log.d("RepositoryLog", "Edited item details in database for item ID: $itemId")
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


