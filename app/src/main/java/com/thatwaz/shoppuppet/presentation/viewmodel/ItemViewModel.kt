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

//    private val _selectedShops = MutableLiveData<List<Shop>>()
//    val selectedShops: LiveData<List<Shop>> = _selectedShops

    // LiveData to observe changes in items
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    // LiveData to observe changes in shops
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> get() = _shops

    private val _itemUiModels = MutableLiveData<List<ItemUiModel>>()
    val itemUiModels: LiveData<List<ItemUiModel>> = _itemUiModels

//    val frequentItems: LiveData<List<Item>> = itemRepository.getFrequentItems()



    // Existing LiveData and functions...

    init {
        logItemsWithAssociatedShops()
        fetchAllItems()
        fetchAllShops()
//        calculateThirtyDaysAgo()
        fetchFrequentItems()
//        fetchFreqItems()

    }

    // Fetch frequently purchased items and log the retrieval
    fun fetchFrequentItems() {
        // Assigning the result of getFrequentItems() to frequentItems LiveData
        // Assuming getFrequentItems() returns LiveData<List<Item>>
        // Note: No need to calculateThirtyDaysAgo here as it should be done in the Repository

        // Log the operation for debugging
        Log.d("ItemViewModel", "Fetching frequently purchased items")
    }





    fun updateItemName(itemName: String) {
        itemNameLiveData.value = itemName

        Log.i("DOH!", "updateItemName called with itemName: $itemName")
        Log.i("DOH!", "itemNameLiveData value: ${itemNameLiveData.value}")

    }


// For frequently purchased items

//    fun fetchFreqItems() {
//        Log.d("ItemViewPredict", "Freq items are ${frequentItems.value}")
//    }


    //used for delete logic in list fragment -------
    fun findItemByUiModel(itemUiModel: ItemUiModel): Item? {
        return items.value?.find { it.id == itemUiModel.itemId }
    }


    /* This is used for deleting items directly from the main list */
    fun deleteItemWithShops(item: Item) {
        viewModelScope.launch {
            // Delete the item and its associated shops
            itemRepository.deleteItemWithShops(item)
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

    fun fetchAllItems() {
        viewModelScope.launch {
            val allItems = itemRepository.getAllItems()
            // Process all items first
            allItems.forEach { item ->
                // Log each item
                Log.d(
                    "ItemViewModel",
                    "Item name: ${item.name}, Item description: ${item.description}"
                )
            }
            // After processing all items, update LiveData
            _items.postValue(allItems)
        }
    }

    fun insertItemWithShopsAsync(item: Item, shopIds: List<Long>): Deferred<Long> =
        viewModelScope.async {
            // Insert the item and get its ID
            val itemId = itemRepository.insertItem(item)

            // If insertion was successful (itemId is not -1), associate item with shops
            if (itemId != -1L) {
                // Create and await all association operations
                shopIds.forEach { shopId ->
                    crossRefRepository.associateItemWithShop(itemId, shopId)
                }
            }

            // Fetch all items again to update the _items LiveData
            fetchAllItems()
            logItemsWithAssociatedShops()

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

            // Now, update the LiveData with the list of UI models
            _itemUiModels.postValue(uiModels)

            // Logging after updating LiveData
            Log.i("Crispy", "UI models updated: $uiModels")
            fetchAllItems()
        }
    }


//    fun logItemsWithAssociatedShops() {
//        viewModelScope.launch {
//            // Fetch all items first
//            val allItems = itemRepository.getAllItems()
//
//            // Prepare a list to hold the UI models
//            val uiModels = mutableListOf<ItemUiModel>()
//
//            // Iterate over each item and fetch associated shops
//            for (item in allItems) {
//                val associatedShopIds = crossRefRepository.getShopIdsForItem(item.id)
//                val associatedShops = shopRepository.getShopsByIds(associatedShopIds)
//
//                // Logging associated shops for each item
//                Log.i("Crispy", "Associated shops for item ${item.name} are $associatedShops")
//
//                // Create and add the UI model to the list
//                uiModels.add(
//                    ItemUiModel(
//                        itemId = item.id,
//                        itemName = item.name,
//                        shopNames = associatedShops,
//                        isPriorityItem = item.isPriorityItem
//                    )
//                )
//            }
//
//            // Now, update the LiveData with the list of UI models
//            // This is done after all the asynchronous work is complete
//            _itemUiModels.postValue(uiModels)
//
//            // Logging after updating LiveData
//            Log.i("Crispy", "UI models updated: $uiModels")
//            fetchAllItems()
//        }
//    }



    fun updateItem(itemId: Long, itemName: String, shopIds: List<Long>, isPriority: Boolean) {
        viewModelScope.launch {
            // Fetch the existing item
            val currentItem = itemRepository.getItemById(itemId)
            currentItem?.let { item ->
                item.name = itemName
                item.isPriorityItem = isPriority
                // Update any other fields if necessary

                // Update the item in the database
                itemRepository.updateItem(item)

                // Update the associations with shops
                updateShopAssociations(item.id, shopIds)
            }
        }
    }

    private fun updateShopAssociations(itemId: Long, newShopIds: List<Long>) {
        viewModelScope.launch {
            Log.d("UpdateAssociations", "Starting to update associations for item ID: $itemId")

            // Clear existing associations
            Log.d("UpdateAssociations", "Removing all associations for item ID: $itemId")
            crossRefRepository.removeAllAssociationsForItem(itemId)
            Log.d("UpdateAssociations", "All existing associations removed for item ID: $itemId")

            // Create new associations
            newShopIds.forEach { shopId ->
                Log.d("UpdateAssociations", "Associating shop ID $shopId with item ID: $itemId")
                crossRefRepository.associateItemWithShop(itemId, shopId)
            }
            Log.d("UpdateAssociations", "All new associations created for item ID: $itemId")

            // Optionally, refresh the item list
            Log.d("UpdateAssociations", "Refreshing all items post association update")
            fetchAllItems()
        }
    }


//    private fun updateShopAssociations(itemId: Long, newShopIds: List<Long>) {
//        viewModelScope.launch {
//            // Clear existing associations
//            crossRefRepository.removeAllAssociationsForItem(itemId)
//
//            // Create new associations
//            newShopIds.forEach { shopId ->
//                crossRefRepository.associateItemWithShop(itemId, shopId)
//            }
//
//            // Optionally, refresh the item list
//            fetchAllItems()
//        }
//    }


//    private fun updateShopAssociations(itemId: Long, newShopIds: List<Long>) {
//        viewModelScope.launch {
//            // Clear existing associations
//            crossRefRepository.removeAssociationBetweenItemAndShop(itemId)
//
//            // Create new associations
//            newShopIds.forEach { shopId ->
//                crossRefRepository.associateItemWithShop(itemId, shopId)
//            }
//
//            // Optionally, refresh the item list
//            fetchAllItems()
//        }
//    }

//    fun fetchAndSetSelectedShops(itemId: Long) {
//        viewModelScope.launch {
//            try {
//                // Log the itemId received
//                Log.i("Crappy", "Fetching associated shops for itemId: $itemId")
//
//                // Get the list of shop IDs associated with the item
//                val associatedShopIds = crossRefRepository.getShopIdsForItem(itemId)
//
//                // Log the associated shop IDs
//                Log.i("Crappy", "Associated shop IDs: $associatedShopIds")
//
//                // Fetch shops by their IDs
//                val associatedShops = shopRepository.getShopsByIds(associatedShopIds)
//
//                // Log the associated shops
//                Log.i("Crappy", "Associated shops: $associatedShops")
//
//                // Update the selected shops
//                _selectedShops.postValue(associatedShops)
//            } catch (e: Exception) {
//                // Handle any exceptions, such as logging or showing an error message
//                Log.e("Crappy", "Error fetching associated shops: ${e.message}")
//            }
//        }
//    }


//    fun fetchAndSetSelectedShops(itemId: Long) {
//        viewModelScope.launch {
//            try {
//                // Get the list of shop IDs associated with the item
//                val associatedShopIds = crossRefRepository.getShopIdsForItem(itemId)
//
//                // Fetch shops by their IDs
//                val associatedShops = shopRepository.getShopsByIds(associatedShopIds)
//
//                // Update the selected shops
//                _selectedShops.postValue(associatedShops)
//            } catch (e: Exception) {
//                // Handle any exceptions, such as logging or showing an error message
//                Log.e("ItemViewModel", "Error fetching associated shops: ${e.message}")
//            }
//        }
//    }



}










