package com.thatwaz.shoppuppet.data.repository

import android.util.Log
import com.thatwaz.shoppuppet.data.db.dao.ItemShopCrossRefDao
import com.thatwaz.shoppuppet.domain.model.ItemShopCrossRef
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ItemShopCrossRefRepository @Inject constructor(private val crossRefDao: ItemShopCrossRefDao) {

    suspend fun associateItemWithShop(itemId: Long, shopId: Long) {
        try {
            val crossRef = ItemShopCrossRef(itemId, shopId)
            crossRefDao.insertCrossRef(crossRef)
        } catch (e: Exception) {
            Log.e("ItemShopCrossRefRepo", "Error associating item with shop: ${e.localizedMessage}")
            // Optionally, rethrow the exception or handle it as needed
        }
    }

    /** Used to remove previous shops-to-item associations when user is editing an item */
    suspend fun removeAllAssociationsForItem(itemId: Long) {
        try {
            crossRefDao.deleteAllCrossRefsForItem(itemId)
        } catch (e: Exception) {
            Log.e(
                "ItemShopCrossRefRepo",
                "Error removing associations for item $itemId: ${e.localizedMessage}"
            )
        }
    }

    /** Used for getting shops that the user tags to the item */
    suspend fun getShopIdsForItem(itemId: Long): List<Long> {
        return try {
            crossRefDao.getShopIdsForItem(itemId)
        } catch (e: Exception) {
            Log.e(
                "ItemShopCrossRefRepo",
                "Error fetching shop IDs for item $itemId: ${e.localizedMessage}"
            )
            emptyList() // Return an empty list or handle the error as needed
        }
    }


}