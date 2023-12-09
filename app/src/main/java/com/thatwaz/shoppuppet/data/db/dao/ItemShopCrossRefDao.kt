package com.thatwaz.shoppuppet.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thatwaz.shoppuppet.domain.model.ItemShopCrossRef
import com.thatwaz.shoppuppet.domain.model.ShopWithPriorityStatus

@Dao
interface ItemShopCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: ItemShopCrossRef)

    @Query("DELETE FROM item_shop_cross_ref WHERE itemId = :itemId AND shopId = :shopId")
    suspend fun deleteCrossRef(itemId: Long, shopId: Long)

//    @Query("SELECT * FROM item_shop_cross_ref")
//    suspend fun getAllAssociations(): List<ItemShopCrossRef>

    @Query("SELECT s.*, EXISTS (SELECT 1 FROM item_shop_cross_ref isr JOIN items i ON isr.itemId = i.id WHERE isr.shopId = s.id AND i.isPriorityItem = 1) as hasPriorityItem FROM shops s")
    fun getShopsWithPriorityStatus(): LiveData<List<ShopWithPriorityStatus>>


    @Query("SELECT shopId FROM item_shop_cross_ref WHERE itemId = :itemId")
    suspend fun getShopIdsForItem(itemId: Long): List<Long>




}
