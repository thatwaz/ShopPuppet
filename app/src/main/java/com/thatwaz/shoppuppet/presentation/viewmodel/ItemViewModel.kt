package com.thatwaz.shoppuppet.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ItemRepository
import com.thatwaz.shoppuppet.data.repository.ItemShopCrossRefRepository
import com.thatwaz.shoppuppet.data.repository.ShopRepository
import com.thatwaz.shoppuppet.domain.model.Item
import com.thatwaz.shoppuppet.domain.model.ItemUiModel
import com.thatwaz.shoppuppet.domain.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing items, associated shops, and UI models.
 * Handles CRUD operations for items, shop associations, and UI model updates.
 *
 * Functions:
 * - `updateItemName(itemName: String)`: Updates the name of the item.
 * - `findItemByUiModelForDeletion(itemUiModel: ItemUiModel)`: Finds an item for deletion.
 * - `hardDeleteItemWithShops(item: Item)`: Performs a hard delete of an item and its associated shops.
 * - `cleanUpOldSoftDeletedItems()`: Cleans up old soft-deleted items.
 * - `handleItemSave(itemId: Long, itemName: String, selectedShopIds: List<Long>,
 * isPriority: Boolean)`: Handles item save operations.
 * - `refreshUiModels()`: Refreshes UI models to reflect the latest data.
 *
 * LiveData:
 * - `items`: LiveData for item data.
 * - `shops`: LiveData for shop data.
 * - `itemUiModels`: LiveData for UI models.
 * - `error`: LiveData for error messages.
 */

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val shopRepository: ShopRepository,
    private val crossRefRepository: ItemShopCrossRefRepository
) : ViewModel() {

    private val _itemNameLiveData = MutableLiveData<String>()
    private var itemNameLiveData: MutableLiveData<String> = _itemNameLiveData

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> get() = _shops

    private val _itemUiModels = MutableLiveData<List<ItemUiModel>>()
    val itemUiModels: LiveData<List<ItemUiModel>> = _itemUiModels

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        logItemsWithAssociatedShops()
        fetchAllItems()
        fetchAllShops()
    }

    fun updateItemName(itemName: String) {
        itemNameLiveData.value = itemName
    }

    //Handles finding individual item deletion in List Fragment
    fun findItemByUiModelForDeletion(itemUiModel: ItemUiModel): Item? {
        return items.value?.find { it.id == itemUiModel.itemId }
    }

    //Handles actual individual item deletion in List Fragment
    fun hardDeleteItemWithShops(item: Item) {
        viewModelScope.launch {
            try {
                itemRepository.deleteItemWithShops(item)
                refreshUiModels()
            } catch (e: Exception) {
                _error.postValue("Error deleting item: ${e.localizedMessage}")
            }
        }
    }

    private fun fetchAllShops() {
        viewModelScope.launch {
            try {
                val allShops = shopRepository.getAllShops()
                _shops.value = allShops
                allShops.forEach { _ ->
                }
            } catch (e: Exception) {
                _error.postValue("Error fetching shops: ${e.localizedMessage}")
            }
        }
    }

    private fun fetchAllItems() {
        viewModelScope.launch {
            try {
                val allItems = itemRepository.getAllItems()

                allItems.forEach { _ ->

                }

                _items.postValue(allItems)
            } catch (e: Exception) {
                _error.postValue("Error fetching items: ${e.localizedMessage}")
            }
        }
    }

    fun cleanUpOldSoftDeletedItems() {
        viewModelScope.launch {
            try {
                itemRepository.deleteOldSoftDeletedItems(1)
            } catch (e: Exception) {
                _error.postValue("Error cleaning up old items: ${e.localizedMessage}")
            }
        }
    }

    fun handleItemSave(
        itemId: Long,
        itemName: String,
        selectedShopIds: List<Long>,
        isPriority: Boolean
    ) {
        viewModelScope.launch {
            try {
                if (itemId == -1L) {
                    addItem(itemName, selectedShopIds, isPriority)
                } else {
                    updateItem(itemId, itemName, selectedShopIds, isPriority)
                }
            } catch (e: Exception) {
                _error.postValue("Error saving item: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun addItem(
        itemName: String,
        selectedShopIds: List<Long>,
        isPriority: Boolean
    ) {
        // Create a new item
        val newItem = Item(name = itemName, isPriorityItem = isPriority)

        // Insert the item and get its ID
        val newItemId = itemRepository.insertItem(newItem)

        // Associate the new item with selected shops
        selectedShopIds.forEach { shopId ->
            crossRefRepository.associateItemWithShop(newItemId, shopId)
        }

        // Refresh UI models to reflect the new item
        refreshUiModels()
    }

    private suspend fun updateItem(
        itemId: Long,
        itemName: String,
        selectedShopIds: List<Long>,
        isPriority: Boolean
    ) {
        // Retrieve and update the existing item
        val existingItem = itemRepository.getItemById(itemId)
        existingItem?.let { item ->
            item.name = itemName
            item.isPriorityItem = isPriority
            itemRepository.updateItem(item)

            updateShopAssociations(itemId, selectedShopIds)
        }
        refreshUiModels()
    }

    private fun logItemsWithAssociatedShops() {
        viewModelScope.launch {
            try {
                // Fetch all non-deleted items from the repository
                val allItems = itemRepository.getAllItems()

                // Initialize a list to store UI models for each item
                val uiModels = mutableListOf<ItemUiModel>()

                // Process each item to fetch and associate shops, skipping soft-deleted items
                for (item in allItems) {
                    if (!item.isSoftDeleted) {
                        try {
                            // Fetch shop IDs associated with the item
                            val associatedShopIds = crossRefRepository.getShopIdsForItem(item.id)
                            // Fetch shop details based on the shop IDs
                            val associatedShops = shopRepository.getShopsByIds(associatedShopIds)

                            // Add a new UI model for the item with the fetched shop details
                            uiModels.add(
                                ItemUiModel(
                                    itemId = item.id,
                                    itemName = item.name,
                                    shopNames = associatedShops,
                                    isPriorityItem = item.isPriorityItem
                                )
                            )
                        } catch (e: Exception) {
                            _error.postValue("Error fetching shops for item ${item.name}: ${e.message}")
                        }
                    }
                }

                // Update the LiveData with the generated list of UI models
                _itemUiModels.postValue(uiModels)

                // Refresh the UI models to reflect the latest data
                refreshUiModels()

            } catch (e: Exception) {
                _error.postValue("Error logging items with associated shops: ${e.localizedMessage}")
            }
        }
    }

    fun refreshUiModels() {
        viewModelScope.launch {
            try {
                // Fetch all non-deleted items and transform them into UI models
                val allItems = itemRepository.getAllItems().filterNot { it.isSoftDeleted }
                val uiModels = mutableListOf<ItemUiModel>()

                for (item in allItems) {
                    // Attempt to fetch shop names for each item to include in the UI model
                    val shopNames = try {
                        shopRepository.getShopsByIds(crossRefRepository.getShopIdsForItem(item.id))
                    } catch (e: Exception) {
                        // Handle fetching error by creating an empty list for shop names
                        emptyList()
                    }

                    // Constructing the UI model with the item and associated shop details
                    val itemUiModel = ItemUiModel(
                        itemId = item.id,
                        itemName = item.name,
                        shopNames = shopNames,
                        isPriorityItem = item.isPriorityItem
                    )

                    uiModels.add(itemUiModel)
                }

                // Update the LiveData with the list of constructed UI models
                _itemUiModels.postValue(uiModels)
            } catch (e: Exception) {
                _error.postValue("Error refreshing UI: ${e.localizedMessage}")
                // Post an error message to the LiveData when an exception occurs
            }
        }
    }

    private fun updateShopAssociations(itemId: Long, newShopIds: List<Long>) {
        viewModelScope.launch {
            try {
                // First, remove all existing associations for the item
                crossRefRepository.removeAllAssociationsForItem(itemId)

                // Then, create new associations with the provided shop IDs
                newShopIds.forEach { shopId ->
                    crossRefRepository.associateItemWithShop(itemId, shopId)
                }
            } catch (e: Exception) {
                // If an error occurs, post the error message to the LiveData
                _error.postValue("Error updating shop associations: ${e.localizedMessage}")
            }
        }
    }

}










