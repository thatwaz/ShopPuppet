package com.thatwaz.shoppuppet.data.repository

import android.util.Log
import com.thatwaz.shoppuppet.data.db.dao.ShopDao
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.domain.model.ShopWithItemCount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(private val shopDao: ShopDao) {

    // Insert a new shop
    suspend fun insertShop(shop: Shop): Long = shopDao.insert(shop)

    // Retrieve all shops

    suspend fun getAllShops(): List<Shop> = shopDao.getAllShops()


//    // Retrieve a shop by its ID
//    suspend fun getShopById(shopId: Long): Shop? = shopDao.getShopById(shopId)
//
//    // Update a shop's details
//    suspend fun updateShop(shop: Shop) = shopDao.updateShop(shop)

    // Delete a shop
    suspend fun deleteShop(shop: Shop) = shopDao.deleteShop(shop)

    // Retrieve the number of items associated with a shop (assuming this method exists in your ShopDao)
    suspend fun getItemsCountForShop(shopId: Long): Int = shopDao.getItemsCountForShop(shopId)


    suspend fun getShopsWithItemCountAndPriorityStatus(): List<ShopWithItemCount> {
        return shopDao.getShopsWithItemCountAndPriorityStatus().map { shopWithItemCountAndPriority ->
            Log.d("ShopRepo", "Shop: ${shopWithItemCountAndPriority.name}, hasPriorityItem: ${shopWithItemCountAndPriority.hasPriorityItem}")

            ShopWithItemCount(
                shop = Shop(
                    id = shopWithItemCountAndPriority.id,
                    name = shopWithItemCountAndPriority.name,
                    iconResName = shopWithItemCountAndPriority.iconResName, // Updated to use iconResName
                    colorResName = shopWithItemCountAndPriority.colorResName, // Updated to use colorResName
                    initials = shopWithItemCountAndPriority.initials,
                    isPriority = shopWithItemCountAndPriority.hasPriorityItem
                ),
                itemCount = shopWithItemCountAndPriority.itemCount,
                hasPriorityItem = shopWithItemCountAndPriority.hasPriorityItem
            )
        }
    }
//    suspend fun getShopsWithItemCountAndPriorityStatus(): List<ShopWithItemCount> {
//        return shopDao.getShopsWithItemCountAndPriorityStatus().map { shopWithItemCountAndPriority ->
//            Log.d("ShopRepo", "Shop: ${shopWithItemCountAndPriority.name}, hasPriorityItem: ${shopWithItemCountAndPriority.hasPriorityItem}")
//
//            ShopWithItemCount(
//                shop = Shop(
//                    id = shopWithItemCountAndPriority.id,
//                    name = shopWithItemCountAndPriority.name,
//                    iconRef = shopWithItemCountAndPriority.iconRef,
//                    colorResId = shopWithItemCountAndPriority.colorResId,
//                    initials = shopWithItemCountAndPriority.initials,
//                    isPriority = shopWithItemCountAndPriority.hasPriorityItem
//                ),
//                itemCount = shopWithItemCountAndPriority.itemCount,
//                hasPriorityItem = shopWithItemCountAndPriority.hasPriorityItem
//            )
//        }
//    }

    suspend fun updatePriorityStatusForShops(shopIds: List<Long>, isPriority: Boolean) {
        shopDao.updatePriorityStatus(shopIds, isPriority)
    }




//    suspend fun getItemsForShop(shopId: Long): List<Item> {
//        return shopDao.getItemsForShop(shopId)
//    }

    suspend fun getShopsByIds(shopIds: List<Long>): List<Shop> {
        return shopDao.getShopsByIds(shopIds)
    }



//    suspend fun getShopsForItem(itemId: Long): List<Shop> {
//        val shops = shopDao.getShopsForItem(itemId)
//        Log.d("ShopRepository", "Shops for item $itemId: ${shops.joinToString { it.name }}")
//        return shops
//    }


//    suspend fun getShopsForItem(itemId: Long): List<Shop> {
//        return shopDao.getShopsForItem(itemId)
//    }


}
