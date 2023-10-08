package com.thatwaz.shoppuppet.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thatwaz.shoppuppet.domain.model.Shop

@Dao
interface ShopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shop: Shop): Long

    @Query("SELECT * FROM shops")
    suspend fun getAllShops(): List<Shop>

    @Query("SELECT * FROM shops WHERE id = :shopId")
    suspend fun getShopById(shopId: Long): Shop?

    @Update
    suspend fun updateShop(shop: Shop): Int

    @Delete
    suspend fun deleteShop(shop: Shop): Int

    // Assuming you have an ItemDao with a getItemsCountForShop() method
    @Query("SELECT COUNT(*) FROM items WHERE shopId = :shopId")
    suspend fun getItemsCountForShop(shopId: Long): Int
}

