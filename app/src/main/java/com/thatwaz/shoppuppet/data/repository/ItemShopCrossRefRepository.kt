package com.thatwaz.shoppuppet.data.repository

import com.thatwaz.shoppuppet.data.db.dao.ItemShopCrossRefDao
import com.thatwaz.shoppuppet.domain.model.ItemShopCrossRef
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemShopCrossRefRepository @Inject constructor(private val crossRefDao: ItemShopCrossRefDao) {

    suspend fun associateItemWithShop(itemId: Long, shopId: Long) {
        val crossRef = ItemShopCrossRef(itemId, shopId)
        crossRefDao.insertCrossRef(crossRef)
    }

    suspend fun removeAssociationBetweenItemAndShop(itemId: Long, shopId: Long) {
        crossRefDao.deleteCrossRef(itemId, shopId)
    }

    // Other related methods...
}
