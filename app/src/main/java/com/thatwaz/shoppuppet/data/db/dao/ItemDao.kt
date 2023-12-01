package com.thatwaz.shoppuppet.data.db.dao

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
}




