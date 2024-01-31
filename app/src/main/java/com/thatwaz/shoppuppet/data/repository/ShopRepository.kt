package com.thatwaz.shoppuppet.data.repository

import android.util.Log
import com.thatwaz.shoppuppet.data.db.dao.ShopDao
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.domain.model.ShopWithItemCount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(private val shopDao: ShopDao) {

    suspend fun insertShop(shop: Shop): Long {
        return try {
            shopDao.insert(shop)
        } catch (e: Exception) {
            Log.e("ShopRepository", "Error inserting shop: ${e.message}")
            -1 // Indicate failure with a special value, or consider re-throwing
        }
    }
    suspend fun getAllShops(): List<Shop> {
        return try {
            shopDao.getAllShops()
        } catch (e: Exception) {
            Log.e("ShopRepository", "Error fetching all shops: ${e.localizedMessage}")
            emptyList() // Return an empty list in case of an error
        }
    }

    suspend fun deleteShop(shop: Shop): Boolean {
        return try {
            shopDao.deleteShop(shop)
            true // Indicate success
        } catch (e: Exception) {
            Log.e("ShopRepository", "Error deleting shop: ${e.localizedMessage}")
            false // Indicate failure
        }
    }

    suspend fun getShopsWithItemCountAndPriorityStatus(): List<ShopWithItemCount> {
        return try {
            shopDao.getShopsWithItemCountAndPriorityStatus().map { shopWithItemCountAndPriority ->
                ShopWithItemCount(
                    shop = Shop(
                        id = shopWithItemCountAndPriority.id,
                        name = shopWithItemCountAndPriority.name,
                        iconResName = shopWithItemCountAndPriority.iconResName,
                        colorResName = shopWithItemCountAndPriority.colorResName,
                        initials = shopWithItemCountAndPriority.initials,
                        isPriority = shopWithItemCountAndPriority.hasPriorityItem
                    ),
                    itemCount = shopWithItemCountAndPriority.itemCount,
                    hasPriorityItem = shopWithItemCountAndPriority.hasPriorityItem
                )
            }
        } catch (e: Exception) {
            Log.e(
                "ShopRepository", "Error fetching shops with item count and priority" +
                        " status: ${e.localizedMessage}")
            emptyList()
        }
    }

    /**
     * Retrieves a list of [Shop] objects by their IDs.
     *
     * This function is typically used to get shops that the user has tagged to their items,
     * allowing for a quick retrieval of shop details based on a list of shop IDs.
     *
     * @param shopIds A list of shop IDs to fetch.
     * @return A list of [Shop] objects corresponding to the provided IDs. Returns an empty list
     *         if no shops are found or if an error occurs during the database query.
     * @throws Exception Logs an error and returns an empty list if the database query fails.
     */
    suspend fun getShopsByIds(shopIds: List<Long>): List<Shop> {
        return try {
            shopDao.getShopsByIds(shopIds)
        } catch (e: Exception) {
            Log.e("ShopRepository", "Error fetching shops by IDs: ${e.localizedMessage}")
            emptyList() // Return an empty list as a fallback
        }
    }

}
