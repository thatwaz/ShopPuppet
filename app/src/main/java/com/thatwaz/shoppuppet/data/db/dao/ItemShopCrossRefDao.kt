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

    // Query for items based on shopId and vice versa as needed
}
