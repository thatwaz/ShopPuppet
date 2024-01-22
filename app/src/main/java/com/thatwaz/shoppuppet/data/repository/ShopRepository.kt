package com.thatwaz.shoppuppet.data.repository

import android.util.Log
import com.thatwaz.shoppuppet.data.db.dao.ShopDao
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.domain.model.ShopWithItemCount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(private val shopDao: ShopDao) {

    suspend fun insertShop(shop: Shop): Long = shopDao.insert(shop)
    suspend fun getAllShops(): List<Shop> = shopDao.getAllShops()
    suspend fun deleteShop(shop: Shop) = shopDao.deleteShop(shop)
    suspend fun getShopsWithItemCountAndPriorityStatus(): List<ShopWithItemCount> {
        return shopDao.getShopsWithItemCountAndPriorityStatus()
            .map { shopWithItemCountAndPriority ->
                Log.d(
                    "ShopRepo",
                    "Shop: ${shopWithItemCountAndPriority.name}, " +
                            "ColorResName: ${shopWithItemCountAndPriority.colorResName}, " +
                            "IconResName: ${shopWithItemCountAndPriority.iconResName}," +
                            " hasPriorityItem: ${shopWithItemCountAndPriority.hasPriorityItem}"
                )
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
    }

    suspend fun updatePriorityStatusForShops(shopIds: List<Long>, isPriority: Boolean) {
        shopDao.updatePriorityStatus(shopIds, isPriority)
    }

    suspend fun getShopsByIds(shopIds: List<Long>): List<Shop> {
        return shopDao.getShopsByIds(shopIds)
    }

}
