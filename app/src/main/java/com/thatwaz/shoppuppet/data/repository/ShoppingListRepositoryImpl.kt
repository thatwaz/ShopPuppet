package com.thatwaz.shoppuppet.data.repository


import com.thatwaz.shoppuppet.domain.model.ShoppingItem
import com.thatwaz.shoppuppet.domain.repository.ShoppingListRepository
import com.thatwaz.shoppuppet.data.source.local.ShoppingListLocalDataSource
// If you had a remote data source, you'd also import it here.

class ShoppingListRepositoryImpl(
    private val localDataSource: ShoppingListLocalDataSource
    // You could also have a remote data source as another parameter if needed
): ShoppingListRepository {

    override suspend fun addItem(item: ShoppingItem): Result<Unit> {
        return localDataSource.saveItem(item)
    }

    override suspend fun removeItem(item: ShoppingItem): Result<Unit> {
        return localDataSource.deleteItem(item)
    }

    override suspend fun getAllItems(): Result<List<ShoppingItem>> {
        return localDataSource.getAllItems()
    }

    // ... implement other operations

}
