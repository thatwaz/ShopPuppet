package com.thatwaz.shoppuppet.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.thatwaz.shoppuppet.domain.model.Item
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
//    @Query("SELECT COUNT(*) FROM items WHERE shopId = :shopId")
//    suspend fun getItemsCountForShop(shopId: Long): Int

    @Query("""
    SELECT COUNT(items.id) FROM items 
    INNER JOIN item_shop_cross_ref ON items.id = item_shop_cross_ref.itemId 
    WHERE item_shop_cross_ref.shopId = :shopId
""")
    suspend fun getItemsCountForShop(shopId: Long): Int

    @Transaction
    @Query("SELECT * FROM items INNER JOIN item_shop_cross_ref ON items.id = item_shop_cross_ref.itemId WHERE item_shop_cross_ref.shopId = :shopId")
    suspend fun getItemsForShop(shopId: Long): List<Item>

    @Query("""
    SELECT * FROM Shops 
    INNER JOIN item_shop_cross_ref
    ON shopId = item_shop_cross_ref.shopId 
    WHERE item_shop_cross_ref.itemId = :itemId
""")
    suspend fun getShopsForItem(itemId: Long): List<Shop>



}

