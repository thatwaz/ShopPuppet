package com.thatwaz.shoppuppet.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thatwaz.shoppuppet.domain.model.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    @Query("SELECT * FROM items")
    suspend fun getAllItems(): List<Item>

    @Query("SELECT * FROM items WHERE shopId = :shopId")
    suspend fun getItemsByShop(shopId: Long): List<Item>

    @Update
    suspend fun updateItem(item: Item): Int

    @Delete
    suspend fun deleteItem(item: Item): Int

    @Query("DELETE FROM items WHERE shopId = :shopId")
    suspend fun deleteItemsByShop(shopId: Long): Int

    @Query("SELECT COUNT(*) FROM items WHERE shopId = :shopId")
    suspend fun getItemsCountForShop(shopId: Long): Int

    @Query("UPDATE Items SET isPurchased = :purchased WHERE id = :itemId")
    suspend fun setItemPurchasedStatus(itemId: Long, purchased: Boolean)

    @Query("SELECT * FROM Items WHERE isPurchased = 1")
    suspend fun getPurchasedItems(): List<Item>

    // Get items purchased within the last 30 days
//    @Query("SELECT * FROM items WHERE lastPurchasedDate >= :dateCutoff")
//    suspend fun getRecentPurchases(dateCutoff: Date): List<Item>
//
//    // Update the last purchased date for an item
//    @Query("UPDATE Items SET lastPurchasedDate = :date WHERE id = :itemId")
//    suspend fun updateLastPurchasedDate(itemId: Long, date: Date)

}

