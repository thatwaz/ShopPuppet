package com.thatwaz.shoppuppet.data.source.local

import com.thatwaz.shoppuppet.data.source.local.db.ShoppingItemDao
import com.thatwaz.shoppuppet.domain.model.ShoppingItem

class ShoppingListLocalDataSourceImpl(
    private val shoppingItemDao: ShoppingItemDao
): ShoppingListLocalDataSource {
    override suspend fun saveItem(item: ShoppingItem): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItem(item: ShoppingItem): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllItems(): Result<List<ShoppingItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getItemById(id: Long): Result<ShoppingItem> {
        TODO("Not yet implemented")
    }


}
