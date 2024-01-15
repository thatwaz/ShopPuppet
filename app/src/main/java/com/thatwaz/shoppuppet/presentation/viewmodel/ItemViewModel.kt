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

    //todo below isPrirorityItem is not updating in itemUiModels
    private val _itemUiModels = MutableLiveData<List<ItemUiModel>>()
    val itemUiModels: LiveData<List<ItemUiModel>> = _itemUiModels

    init {
        logItemsWithAssociatedShops()
        fetchAllItems()
        fetchAllShops()
//        refreshUiModels()
    }

    fun updateItemName(itemName: String) {
        itemNameLiveData.value = itemName

        Log.i("DOH!", "updateItemName called with itemName: $itemName")
        Log.i("DOH!", "itemNameLiveData value: ${itemNameLiveData.value}")

    }

    //used for delete logic in list fragment -------
    fun findItemByUiModel(itemUiModel: ItemUiModel): Item? {
        return items.value?.find { it.id == itemUiModel.itemId }
    }


    /* This is used for deleting items directly from the main list */
    fun deleteItemWithShops(item: Item) {
        viewModelScope.launch {
            // Delete the item and its associated shops
            itemRepository.deleteItemWithShops(item)
            refreshUiModels()
        }
    }

    // end------------------------------------------
    fun fetchAllShops() {
        viewModelScope.launch {
            _shops.value = shopRepository.getAllShops()

            val allShops = shopRepository.getAllShops()
            _shops.value = allShops
            allShops.forEach { shop ->
                Log.d("HorseShit", "Shop name: ${shop.name}, Shop id: ${shop.id}")
            }
        }
    }


    // TODO THIS IS UPDATING PRIORITY STATUS CORRECTLY BUT U.I. IS NOT
    fun fetchAllItems() {
        viewModelScope.launch {
            val allItems = itemRepository.getAllItems()
            // Process all items first
            allItems.forEach { item ->
                // Log each item
                Log.d(
                    "ItemViewModel",
                    "Item name: ${item.name}, item ps: ${item.isPriorityItem}Item last purchased: ${item.lastPurchasedDate}"
                )
            }
//                        refreshUiModels()
            // After processing all items, update LiveData
            _items.postValue(allItems)

        }
    }

    fun cleanUpOldSoftDeletedItems() {
        viewModelScope.launch {
            itemRepository.deleteOldSoftDeletedItems()
        }
    }

    fun insertItemWithShopsAsync(item: Item, shopIds: List<Long>, isPriority: Boolean): Deferred<Long> =
        viewModelScope.async {
            // Insert the item and get its ID
            val itemId = itemRepository.insertItem(item)

            // If insertion was successful (itemId is not -1), handle additional operations
            if (itemId != -1L) {
                // Associate item with shops
                shopIds.forEach { shopId ->
                    crossRefRepository.associateItemWithShop(itemId, shopId)
                }
                // Update priority status

                updatePriorityStatus(itemId, isPriority)


            } else {
                updateItem(item.id, item.name, shopIds, isPriority)
                item.id
            }

            // Fetch all items again to update the _items LiveData
            fetchAllItems()
//            logItemsWithAssociatedShops()

            // Return the generated item ID
            itemId
        }

    fun logItemsWithAssociatedShops() {
        viewModelScope.launch {
            // Fetch all items first
            val allItems = itemRepository.getAllItems()

            // Prepare a list to hold the UI models
            val uiModels = mutableListOf<ItemUiModel>()

            // Iterate over each item and fetch associated shops, excluding soft-deleted items
            for (item in allItems) {
                // Skip soft-deleted items
                if (!item.isSoftDeleted) {
                    val associatedShopIds = crossRefRepository.getShopIdsForItem(item.id)
                    val associatedShops = shopRepository.getShopsByIds(associatedShopIds)

                    // Logging associated shops for each item
                    Log.i("Crispy", "Associated shops for item ${item.name} are $associatedShops")

                    // Create and add the UI model to the list
                    uiModels.add(
                        ItemUiModel(
                            itemId = item.id,
                            itemName = item.name,
                            shopNames = associatedShops,
                            isPriorityItem = item.isPriorityItem
                        )
                    )
                }
            }
            //Adding this fixed u.i. issue to change without leaving and returning to fragment
            refreshUiModels()
            // Now, update the LiveData with the list of UI models
            _itemUiModels.postValue(uiModels)
            Log.i("ThreadCheck", "Current thread: ${Thread.currentThread().name}")
//            _itemUiModels.postValue(uiModels)

            // Logging after updating LiveData

            Log.i("Baked Goods", "UI models updated: $uiModels")

            fetchAllItems()
        }
    }


    // this is updating item but not item ui models
//    fun updateItem(itemId: Long, itemName: String, shopIds: List<Long>, isPriority: Boolean) {
//        viewModelScope.launch {
//            itemRepository.getItemById(itemId)?.let { item ->
//                item.name = itemName
//                item.isPriorityItem = isPriority
//                itemRepository.updateItem(item)
//                updateShopAssociations(item.id, shopIds)
//                refreshUiModels()
//            }
//        }
//    }

    fun updateItem(itemId: Long, itemName: String, shopIds: List<Long>, isPriority: Boolean) {
        viewModelScope.launch {
            itemRepository.getItemById(itemId)?.let { item ->
                item.name = itemName
                itemRepository.updateItem(item)
                updateShopAssociations(item.id, shopIds)
                updatePriorityStatus(itemId, isPriority)
//                refreshUiModels()
            }
        }

    }

    private fun updatePriorityStatus(itemId: Long, isPriority: Boolean) {
        viewModelScope.launch {
            val item = itemRepository.getItemById(itemId)
            item?.let {
                it.isPriorityItem = isPriority
                itemRepository.updateItem(it)
                Log.i("Kangaroo","status is ${it.name} and ${it.isPriorityItem}")
                refreshUiModels()
            }
        }

    }

    fun refreshUiModels() {
        viewModelScope.launch {
            val allItems = itemRepository.getAllItems().filterNot { it.isSoftDeleted }
            val uiModels = allItems.map { item ->
                ItemUiModel(
                    itemId = item.id,
                    itemName = item.name,
                    shopNames = shopRepository.getShopsByIds(
                        crossRefRepository.getShopIdsForItem(
                            item.id
                        )
                    ),
                    isPriorityItem = item.isPriorityItem
                )
            }
            _itemUiModels.postValue(uiModels)
            Log.i("Kangaroo","ui models are $uiModels")
        }
    }


    // TODO THIS FUNCTION UPDATES SHOPS IMMEDIATELY FOR U.I. BUT PRIORITY STATUS IS NOT UPDATED
    private fun updateShopAssociations(itemId: Long, newShopIds: List<Long>) {
        viewModelScope.launch {
            crossRefRepository.removeAllAssociationsForItem(itemId)
            newShopIds.forEach { shopId ->
                crossRefRepository.associateItemWithShop(itemId, shopId)
            }
//            refreshUiModels()
        }
    }


}










