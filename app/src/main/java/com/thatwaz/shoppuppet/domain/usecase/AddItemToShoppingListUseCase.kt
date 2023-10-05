package com.thatwaz.shoppuppet.domain.usecase

import com.thatwaz.shoppuppet.domain.model.ShoppingItem
import com.thatwaz.shoppuppet.domain.repository.ShoppingListRepository

class AddItemToShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository
) {
    suspend fun execute(item: ShoppingItem): Result<Unit> {
        // Validation logic (e.g., check for null, validate item details, etc.)

        // You can add validation here. If validation fails, return a failure Result.

        // Add the item using the repository
        return try {
            shoppingListRepository.addItem(item)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
