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

    suspend fun getItemsByShop(shopId: Long): List<Item> {
        return try {
            itemDao.getItemsByShop(shopId)
        } catch (e: Exception) {
            Log.e("ItemRepository", "Error fetching items by shop: ${e.localizedMessage}")
            emptyList()  // Returning an empty list as a fallback
        }
    }


    // Insert a new item
    suspend fun insertItem(item: Item): Long {
        return try {
            itemDao.insert(item)
        } catch (e: Exception) {
            Log.e("ItemRepository", "Error inserting item: ${e.localizedMessage}")
            -1L  // Returning a negative value as an indication of failure
        }
    }


    suspend fun getItemById(itemId: Long): Item? = itemDao.getItemById(itemId)

    // Used for hard deleting items directly from the list fragment
    suspend fun deleteItemWithShops(item: Item) {
        try {
            // Step 1: Delete associations in item_shop_cross_ref
            val shopIds = itemShopCrossRefDao.getShopIdsForItem(item.id)
            for (shopId in shopIds) {
                itemShopCrossRefDao.deleteCrossRef(item.id, shopId)
            }

            // Step 2: Delete the item itself
            itemDao.deleteItem(item)
        } catch (e: Exception) {
            Log.e("ItemRepository", "Error deleting item with shops: ${e.localizedMessage}")
        }
    }


    // todo temp code for freq items r.v.
    suspend fun deleteSoftDeletedItems() {
        itemDao.deleteSoftDeletedItems()
    }
    suspend fun softDeleteItems(items: List<Item>) {
        items.forEach { newItem ->
            try {
                // Soft delete the new item
                newItem.isSoftDeleted = true
                itemDao.updateItem(newItem)

                // Find older items with the same name
                val olderItems = try {
                    itemDao.getItemsByName(newItem.name).filter {
                        it.id != newItem.id && it.lastPurchasedDate?.before(newItem.lastPurchasedDate) == true
                    }
                } catch (e: Exception) {
                    Log.e("ItemRepository", "Error finding older items: ${e.localizedMessage}")
                    emptyList()
                }

                // Hard delete older items
                olderItems.forEach { oldItem ->
                    try {
                        itemDao.deleteItem(oldItem)
                    } catch (e: Exception) {
                        Log.e("ItemRepository", "Error deleting older item: ${e.localizedMessage}")
                    }
                }
            } catch (e: Exception) {
                Log.e("ItemRepository", "Error in soft deleting item: ${e.localizedMessage}")
            }
        }
    }


    suspend fun updateItem(item: Item): Int {
        return try {
            itemDao.updateItem(item)
        } catch (e: Exception) {
            Log.e("ItemRepository", "Error updating item: ${item.name}, ${e.localizedMessage}")
            0  // Returning 0 rows affected in case of an error
        }
    }


    fun getPurchasedAndSoftDeletedItems(): LiveData<List<Item>> {
        return itemDao.getPurchasedAndSoftDeletedItems()
    }

    fun getPurchasedAndNotSoftDeletedItems(): LiveData<List<Item>> {
        return itemDao.getPurchasedAndNotSoftDeletedItems()
    }


    // used to auto clean up soft-deleted items older thresholdInDays set in view model
    suspend fun deleteOldSoftDeletedItems(thresholdInDays: Int) {
        val thresholdDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -thresholdInDays)
        }.time
        val oldItems = itemDao.getOldSoftDeletedItems(thresholdDate)
        if (oldItems.isNotEmpty()) {
            itemDao.deleteItemsOlderThanThirtyDays(oldItems)
        }
    }

}


