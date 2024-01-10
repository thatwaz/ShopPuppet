package com.thatwaz.shoppuppet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.thatwaz.shoppuppet.domain.model.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    @Query("SELECT * FROM items")
    suspend fun getAllItems(): List<Item>

    @Query("""
    SELECT items.* FROM items 
    INNER JOIN item_shop_cross_ref ON items.id = item_shop_cross_ref.itemId 
    WHERE item_shop_cross_ref.shopId = :shopId
""")
    suspend fun getItemsByShop(shopId: Long): List<Item>
    @Update
    suspend fun updateItem(item: Item): Int

    @Delete
    suspend fun deleteItem(item: Item): Int

    @Query("DELETE FROM item_shop_cross_ref WHERE shopId = :shopId")
    suspend fun deleteItemsByShop(shopId: Long): Int


    @Query("""
    SELECT COUNT(items.id) FROM items 
    INNER JOIN item_shop_cross_ref ON items.id = item_shop_cross_ref.itemId 
    WHERE item_shop_cross_ref.shopId = :shopId
""")
    suspend fun getItemsCountForShop(shopId: Long): Int

    @Query("SELECT * FROM items WHERE id = :itemId")
    suspend fun getItemById(itemId: Long): Item?

    @Query("UPDATE Items SET isPurchased = :purchased WHERE id = :itemId")
    suspend fun setItemPurchasedStatus(itemId: Long, purchased: Boolean)

//    @Query("SELECT * FROM Items WHERE isPurchased = 1")
//    suspend fun getPurchasedItems(): List<Item>

    @Transaction
    suspend fun deleteItemsWithShopAssociation(itemIds: List<Long>) {
        // Step 1: Delete associations in item_shop_cross_ref
        itemIds.forEach { itemId ->
            deleteCrossRef(itemId)
        }

        // Step 2: Delete the items themselves
        deleteItemsByIds(itemIds)
    }

    // Helper method to delete cross references
    @Query("DELETE FROM item_shop_cross_ref WHERE itemId = :itemId")
    suspend fun deleteCrossRef(itemId: Long)

    // Method to delete items by their IDs
    @Query("DELETE FROM items WHERE id IN (:itemIds)")
    suspend fun deleteItemsByIds(itemIds: List<Long>)

//    @Query("SELECT * FROM items WHERE lastPurchasedDate >= :thirtyDaysAgo ORDER BY purchaseCount DESC")
//    fun getFrequentItems(thirtyDaysAgo: Long): LiveData<List<Item>>

//    @Query("SELECT * FROM items WHERE isSoftDeleted = 0")
//    fun getAllActiveItems(): LiveData<List<Item>>  // Modify as per your actual method names and logic
//
//
//    @Query("SELECT * FROM items WHERE lastPurchasedDate >= :thirtyDaysAgo AND isPurchased = 0 ORDER BY lastPurchasedDate DESC, purchaseCount DESC")
//    fun getFrequentItems(thirtyDaysAgo: Long): LiveData<List<Item>>


    // for purchased items recyclerview

    // TODO - this is a temp fix to not show dup. items in purchased and soft-del. recycler view.
    // TOdo - need to find way to delete dup items
    @Query("""
    SELECT MIN(id) as id, name, MIN(description) as description, 
           MAX(isPurchased) as isPurchased, MAX(isPriorityItem) as isPriorityItem, 
           MAX(isSoftDeleted) as isSoftDeleted, MAX(lastPurchasedDate) as lastPurchasedDate, 
           MAX(purchaseCount) as purchaseCount
    FROM items 
    WHERE isPurchased = 1 AND isSoftDeleted = 1 
    GROUP BY name 
    ORDER BY name COLLATE NOCASE ASC
""")
    fun getPurchasedAndSoftDeletedItems(): LiveData<List<Item>>

//    @Query("SELECT * FROM items WHERE isPurchased = 1 AND isSoftDeleted = 1")
//    fun getPurchasedAndSoftDeletedItems(): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE isPurchased = 1 AND isSoftDeleted = 0 ORDER BY name COLLATE NOCASE ASC")
    fun getPurchasedAndNotSoftDeletedItems(): LiveData<List<Item>>


//    @Query("SELECT * FROM items WHERE isPurchased = 1 AND isSoftDeleted = 0")
//    fun getPurchasedAndNotSoftDeletedItems(): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE isSoftDeleted = 0 AND isPurchased = 0")
    fun getActiveUnpurchasedItems(): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE isSoftDeleted = 0 AND isPurchased = 1 AND lastPurchasedDate >= :thirtyDaysAgo")
    fun getActivePurchasedItems(thirtyDaysAgo: Long): LiveData<List<Item>>

    /*temp code for freq items r.v. */
    @Query("DELETE FROM items WHERE isSoftDeleted = 1")
    suspend fun deleteSoftDeletedItems()


    // In your DAO
    /* for future */
//    @Query("DELETE FROM items WHERE isSoftDeleted = 1 AND lastPurchasedDate < :threshold")
//    suspend fun permanentDeleteSoftDeletedItemsOlderThan(threshold: Long)



}




