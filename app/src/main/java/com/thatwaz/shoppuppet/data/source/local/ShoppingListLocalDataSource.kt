package com.thatwaz.shoppuppet.data.source.local

import com.thatwaz.shoppuppet.domain.model.ShoppingItem

interface ShoppingListLocalDataSource {
    suspend fun saveItem(item: ShoppingItem): Result<Unit>
    suspend fun deleteItem(item: ShoppingItem): Result<Unit>
    suspend fun getAllItems(): Result<List<ShoppingItem>>
    suspend fun getItemById(id: Long): Result<ShoppingItem>
    // ... any other operations you'd need
}
