package com.thatwaz.shoppuppet.presentation.viewmodel

import android.util.Log
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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val shopRepository: ShopRepository,
    private val crossRefRepository: ItemShopCrossRefRepository
) : ViewModel() {

    private val _itemName = MutableLiveData("")
    var itemName: LiveData<String> = _itemName

    private val _itemNameLiveData = MutableLiveData<String>()
    var itemNameLiveData: MutableLiveData<String> = _itemNameLiveData

    // LiveData to observe changes in items
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    // LiveData to observe changes in shops
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> get() = _shops

    private val _itemUiModels = MutableLiveData<List<ItemUiModel>>()
    val itemUiModels: LiveData<List<ItemUiModel>> = _itemUiModels

    fun updateItemName(itemName: String) {
        itemNameLiveData.value = itemName

        Log.i("DOH!", "updateItemName called with itemName: $itemName")
        Log.i("DOH!", "itemNameLiveData value: ${itemNameLiveData.value}")

    }
    init {
        logItemsWithAssociatedShops()
        fetchAllItems()
        fetchAllShops()

    }


    fun findItemByUiModel(itemUiModel: ItemUiModel): Item? {
        return items.value?.find { it.id == itemUiModel.itemId }
    }


    fun deleteItemWithShops(item: Item) {
        viewModelScope.launch {
            // Delete the item and its associated shops
            itemRepository.deleteItemWithShops(item)
        }
    }

    fun fetchAllShops() {
        viewModelScope.launch {
            _shops.value = shopRepository.getAllShops()

            val allShops = shopRepository.getAllShops()
            _shops.value = allShops
            allShops.forEach { shop ->
                Log.d("ItemViewModel", "Shop name: ${shop.name}, Shop id: ${shop.id}")
            }
        }
    }

    fun fetchAllItems() {
        viewModelScope.launch {
            val allItems = itemRepository.getAllItems()
            // Process all items first
            allItems.forEach { item ->
                // Log each item
                Log.d("ItemViewModel", "Item name: ${item.name}, Item description: ${item.description}")
            }
            // After processing all items, update LiveData
            _items.postValue(allItems)
        }
    }

    fun insertItemWithShopsAsync(item: Item, shopIds: List<Long>): Deferred<Unit> = viewModelScope.async {
        // Insert the item and get its ID
        val itemId = itemRepository.insertItem(item)

        // Create a list to hold all deferred association operations
        val associationDeferred = shopIds.map { shopId ->
            async { crossRefRepository.associateItemWithShop(itemId, shopId) }
        }

        // Await all association operations to complete
        associationDeferred.forEach { it.await() }

        // Fetch all items again to update the _items LiveData
        fetchAllItems()
        logItemsWithAssociatedShops()
    }


    fun logItemsWithAssociatedShops() {
        viewModelScope.launch {
            // Fetch all items first
            val allItems = itemRepository.getAllItems()

            // Prepare a list to hold the UI models
            val uiModels = mutableListOf<ItemUiModel>()

            // Iterate over each item and fetch associated shops
            for (item in allItems) {
                val associatedShopIds = crossRefRepository.getShopIdsForItem(item.id)
                val associatedShops = shopRepository.getShopsByIds(associatedShopIds)

                // Logging associated shops for each item
                Log.i("Crispy", "Associated shops for item ${item.name} are $associatedShops")

                // Create and add the UI model to the list
                uiModels.add(ItemUiModel(itemId = item.id, itemName = item.name, shopNames = associatedShops))
            }

            // Now, update the LiveData with the list of UI models
            // This is done after all the asynchronous work is complete
            _itemUiModels.postValue(uiModels)

            // Logging after updating LiveData
            Log.i("Crispy", "UI models updated: $uiModels")
            fetchAllItems()
        }
    }

}










