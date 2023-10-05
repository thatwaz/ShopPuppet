package com.thatwaz.shoppuppet.domain.repository


import com.thatwaz.shoppuppet.domain.model.ShoppingItem

interface ShoppingListRepository {
    suspend fun addItem(item: ShoppingItem): Result<Unit>
    suspend fun removeItem(item: ShoppingItem): Result<Unit>
    suspend fun getAllItems(): Result<List<ShoppingItem>>

}
