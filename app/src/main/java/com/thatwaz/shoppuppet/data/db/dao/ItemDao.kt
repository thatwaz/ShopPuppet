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
import java.util.Date

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    @Query("SELECT * FROM items ORDER BY name COLLATE NOCASE ASC")
    suspend fun getAllItemsInAlphabeticalOrder(): List<Item>

    @Query("SELECT * FROM items ORDER BY id ASC")
    suspend fun getAllItemsByOrderOFEntry(): List<Item>


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

    @Query("""
                SELECT MIN(id) as id, name,
                       MAX(isPurchased) as isPurchased, MAX(isPriorityItem) as isPriorityItem,
                       MAX(isSoftDeleted) as isSoftDeleted, MAX(lastPurchasedDate) as lastPurchasedDate
                FROM items
                WHERE isPurchased = 1 AND isSoftDeleted = 1
                GROUP BY name
                ORDER BY name COLLATE NOCASE ASC
            """)
    fun getPurchasedAndSoftDeletedItems(): LiveData<List<Item>>


    /**
     Used for not finding items with duplicate names so they do not appear more than once in
     recently purchased items list
     */
    @Query("SELECT * FROM items WHERE name = :name")
    suspend fun getItemsByName(name: String): List<Item>

    @Query("SELECT * FROM items WHERE isPurchased = 1 AND isSoftDeleted = 0 ORDER BY name COLLATE NOCASE ASC")
    fun getPurchasedAndNotSoftDeletedItems(): LiveData<List<Item>>


    @Query("SELECT * FROM items WHERE isSoftDeleted = 0 AND isPurchased = 1 AND lastPurchasedDate >= :thirtyDaysAgo")
    fun getActivePurchasedItems(thirtyDaysAgo: Long): LiveData<List<Item>>

    /** Used for deleting all frequently purchased items in Add Item Fragment*/
    @Query("DELETE FROM items WHERE isSoftDeleted = 1")
    suspend fun deleteSoftDeletedItems()

    @Delete
    suspend fun deleteItemsOlderThanThirtyDays(items: List<Item>)

    @Query("SELECT * FROM items WHERE isSoftDeleted = 1 AND lastPurchasedDate <= :thresholdDate")
    suspend fun getOldSoftDeletedItems(thresholdDate: Date): List<Item>

}




