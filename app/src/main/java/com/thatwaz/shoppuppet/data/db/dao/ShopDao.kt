package com.thatwaz.shoppuppet.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.domain.model.ShopWithItemCountAndPriority

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


    @Query("""
    SELECT s.id, s.name, s.iconResName, s.colorResName, s.initials, s.isPriority, 
           COUNT(CASE WHEN i.isSoftDeleted = 0 THEN i.id ELSE NULL END) AS itemCount, 
           MAX(CASE WHEN i.isSoftDeleted = 0 THEN i.isPriorityItem ELSE 0 END) AS hasPriorityItem
    FROM shops s
    LEFT JOIN item_shop_cross_ref isr ON s.id = isr.shopId
    LEFT JOIN items i ON isr.itemId = i.id
    GROUP BY s.id
""")
    suspend fun getShopsWithItemCountAndPriorityStatus(): List<ShopWithItemCountAndPriority>


    @Query("UPDATE shops SET isPriority = :isPriority WHERE id IN (:shopIds)")
    suspend fun updatePriorityStatus(shopIds: List<Long>, isPriority: Boolean)


    @Query("SELECT * FROM shops WHERE id IN (:shopIds)")
    suspend fun getShopsByIds(shopIds: List<Long>): List<Shop>

}

