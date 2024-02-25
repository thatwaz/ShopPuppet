package com.thatwaz.shoppuppet.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thatwaz.shoppuppet.domain.model.ItemShopCrossRef

@Dao
interface ItemShopCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: ItemShopCrossRef)

    @Query("DELETE FROM item_shop_cross_ref WHERE itemId = :itemId AND shopId = :shopId")
    suspend fun deleteCrossRef(itemId: Long, shopId: Long)

    /** Used to remove previous shops-to-item associations when user is editing an item */
    @Query("DELETE FROM item_shop_cross_ref WHERE itemId = :itemId")
    suspend fun deleteAllCrossRefsForItem(itemId: Long)


    @Query("SELECT shopId FROM item_shop_cross_ref WHERE itemId = :itemId")
    suspend fun getShopIdsForItem(itemId: Long): List<Long>

}
