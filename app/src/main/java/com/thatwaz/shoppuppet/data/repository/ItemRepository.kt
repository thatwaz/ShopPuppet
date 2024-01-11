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

//    fun getFrequentItems(): LiveData<List<Item>> {
//        val thirtyDaysAgo = calculateThirtyDaysAgo()
//        return itemDao.getFrequentItems(thirtyDaysAgo)
//    }



    // Get active unpurchased items
    fun getActiveUnpurchasedItems(): LiveData<List<Item>> = itemDao.getActiveUnpurchasedItems()

    // Get active purchased items
    fun getActivePurchasedItems(): LiveData<List<Item>> {
        val thirtyDaysAgo = calculateThirtyDaysAgo()
        return itemDao.getActivePurchasedItems(thirtyDaysAgo)
    }

    // Calculate the date 30 days ago
    private fun calculateThirtyDaysAgo(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -30)  // Subtract 30 days from the current date
        return calendar.timeInMillis  // Return the time 30 days ago in milliseconds
    }
    // Utility method to calculate the date 30 days ago




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

    /*temp code for freq items r.v. */
    suspend fun deleteSoftDeletedItems() {
        itemDao.deleteSoftDeletedItems()
    }


//    suspend fun softDeleteItems(items: List<Item>, deleteOlder: Boolean = true) {
//        items.forEach { newItem ->
//            // Find items with the same name
//            val duplicateItems = itemDao.getItemsByName(newItem.name)
//
//            // Determine the item to be hard deleted (older or newer)
//            val itemToHardDelete = if (deleteOlder) {
//                // Find the oldest item
//                duplicateItems.minByOrNull { it.lastPurchasedDate ?: Date(0) }
//            } else {
//                // Find the newest item
//                duplicateItems.maxByOrNull { it.lastPurchasedDate ?: Date(0) }
//            }
//
//            // Hard delete the selected item
//            itemToHardDelete?.let { hardDeleteItem ->
//                itemDao.deleteItem(hardDeleteItem)
//            }
//
//            // Soft delete the rest
//            duplicateItems.filter { it.id != itemToHardDelete?.id }.forEach { item ->
//                item.isSoftDeleted = true
//                itemDao.updateItem(item)
//            }
//        }
//    }

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




//    suspend fun softDeleteItems(items: List<Item>) {
//        items.forEach { item ->
//            item.isSoftDeleted = true  // Mark the item as soft-deleted
//            // Optionally update the lastPurchasedDate or other relevant fields
//            itemDao.updateItem(item)  // Update the item in the database to reflect the soft deletion
//        }
//    }


    // Update an item's details
//    suspend fun updateItem(item: Item) = itemDao.updateItem(item)


    suspend fun updateItem(item: Item) {
        Log.d("RepositoryLog", "Updating item in database: ${item.name}")
        Log.d("RepositoryLog", "Updating purchase status in database: ${item.isPurchased}")
        val result = itemDao.updateItem(item)
        Log.d("RepositoryLog", "Item update completed. Rows affected: $result")
    }

    fun getPurchasedAndSoftDeletedItems(): LiveData<List<Item>> {
        return itemDao.getPurchasedAndSoftDeletedItems()
    }

    fun getPurchasedAndNotSoftDeletedItems(): LiveData<List<Item>> {
        return itemDao.getPurchasedAndNotSoftDeletedItems()
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


    /*for future */
    suspend fun deleteOldSoftDeletedItems() {
        Log.i("Item Repo","Initiating clean-up")
        val thresholdDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -3)  // Set to 30 days ago
        }.time
        val oldItems = itemDao.getOldSoftDeletedItems(thresholdDate)
        Log.i("Item Repo","Old items are $oldItems")
        if (oldItems.isNotEmpty()) {
            itemDao.deleteItemsOlderThanThirtyDays(oldItems)
        }
    }

//    suspend fun cleanupOldSoftDeletedItems() {
//        val thirtyDaysAgoMillis = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000)
//        itemDao.permanentDeleteSoftDeletedItemsOlderThan(thirtyDaysAgoMillis)
//    }

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


