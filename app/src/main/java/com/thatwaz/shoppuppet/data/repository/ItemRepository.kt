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

    suspend fun getItemsByShop(shopId: Long): List<Item> = itemDao.getItemsByShop(shopId)

    // Insert a new item
    suspend fun insertItem(item: Item): Long = itemDao.insert(item)

    suspend fun getItemById(itemId: Long): Item? = itemDao.getItemById(itemId)

    // Used for hard deleting items directly from the list fragment
    suspend fun deleteItemWithShops(item: Item) {
        // Step 1: Delete associations in item_shop_cross_ref
        val shopIds = itemShopCrossRefDao.getShopIdsForItem(item.id)
        for (shopId in shopIds) {
            itemShopCrossRefDao.deleteCrossRef(item.id, shopId)
        }

        // Step 2: Delete the item itself
        itemDao.deleteItem(item)
    }

    /*temp code for freq items r.v. */
    suspend fun deleteSoftDeletedItems() {
        itemDao.deleteSoftDeletedItems()
    }
    suspend fun softDeleteItems(items: List<Item>) {
        items.forEach { newItem ->
            // Soft delete the new item
            newItem.isSoftDeleted = true
            itemDao.updateItem(newItem)

            // Find older items with the same name
            val olderItems = itemDao.getItemsByName(newItem.name).filter {
                it.id != newItem.id && it.lastPurchasedDate?.before(newItem.lastPurchasedDate) == true
            }

            // Hard delete older items
            olderItems.forEach { oldItem ->
                itemDao.deleteItem(oldItem)
            }
        }
    }

    //    suspend fun updateItem(item: Item) {
    //        try {
    //            val result = itemDao.updateItem(item)
    //            Log.d("RepositoryLog", "Item update completed. Rows affected: $result")
    //        } catch (e: SQLiteConstraintException) {
    //            Log.e("RepositoryLog", "Constraint failure while updating item", e)
    //            // Handle the constraint violation, maybe notify the user or retry
    //        } catch (e: Exception) {
    //            Log.e("RepositoryLog", "Error updating item", e)
    //            // Handle other types of exceptions
    //            throw e // Optionally re-throw if you want higher-level handling as well
    //        }
    //    }


    suspend fun updateItem(item: Item) {
        Log.d("RepositoryLog", "Updating item in database: ${item.name}")
        Log.d("RepositoryLog", "Updating purchase status in database: ${item.isPurchased}")
        Log.d("RepositoryLog", "Updating priority status: ${item.isPriorityItem}")
        val result = itemDao.updateItem(item)
        Log.d("RepositoryLog", "Item update completed. Rows affected: $result")
    }

    fun getPurchasedAndSoftDeletedItems(): LiveData<List<Item>> {
        return itemDao.getPurchasedAndSoftDeletedItems()
    }

    fun getPurchasedAndNotSoftDeletedItems(): LiveData<List<Item>> {
        return itemDao.getPurchasedAndNotSoftDeletedItems()
    }

    suspend fun updateItemPriority(itemId: Long, isPriority: Boolean) {
        val item = itemDao.getItemById(itemId)
        if (item != null) {
            item.isPriorityItem = isPriority
            itemDao.updateItem(item)
            Log.d("RepositoryLog", "Updated item's priority status: $isPriority")
        }
    }

    // used to auto clean up soft-deleted items older than 14 days in database
    suspend fun deleteOldSoftDeletedItems() {
        Log.i("Item Repo","Initiating clean-up")
        val thresholdDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -1)  // Set to 1 day ago for test purposesx
        }.time
        val oldItems = itemDao.getOldSoftDeletedItems(thresholdDate)
        Log.i("Item Repo","Old items are $oldItems")
        if (oldItems.isNotEmpty()) {
            itemDao.deleteItemsOlderThanThirtyDays(oldItems)
        }
    }

}


