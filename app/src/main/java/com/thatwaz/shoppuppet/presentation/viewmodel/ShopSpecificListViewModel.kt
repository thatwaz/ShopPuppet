package com.thatwaz.shoppuppet.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ItemRepository
import com.thatwaz.shoppuppet.domain.model.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel for handling item data specific to a shop. Manages the retrieval and update of items
 * in different states such as unpurchased, purchased, soft-deleted, and non-soft-deleted.
 *
 * - `currentShopId` keeps track of the currently selected shop.
 * - `unpurchasedItems` LiveData represents items that are not yet purchased for the current shop.
 * - `purchasedItems` LiveData represents items that have been purchased.
 * - `purchasedAndSoftDeletedItems` LiveData holds items that are purchased and marked for soft deletion.
 * - `purchasedAndNotSoftDeletedItems` LiveData contains items that are purchased but not marked for soft deletion.
 *
 * Provides functionality to:
 * - Update item's purchased status.
 * - Soft delete items (mark as purchased and set for deletion).
 * - Permanently delete soft-deleted items.
 * - Fetch and update lists of items based on their purchase and deletion status.
 *
 * Utilizes a repository for data operations and LiveData to communicate updates to the UI layer.
 */



@HiltViewModel
class ShopSpecificListViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {


    private var currentShopId: Long? = null

    private val _unpurchasedItems = MutableLiveData<List<Item>>()
    val unpurchasedItems: LiveData<List<Item>> = _unpurchasedItems

    private val _purchasedItems = MutableLiveData<List<Item>>()

    private val _purchasedAndSoftDeletedItems = itemRepository.getPurchasedAndSoftDeletedItems()
    val purchasedAndSoftDeletedItems: LiveData<List<Item>> = _purchasedAndSoftDeletedItems

//    private val _purchasedAndNotSoftDeletedItems = itemRepository.getPurchasedAndNotSoftDeletedItemsByShop()
    private val _purchasedAndNotSoftDeletedItems = MutableLiveData<List<Item>>()
    val purchasedAndNotSoftDeletedItems: LiveData<List<Item>> = _purchasedAndNotSoftDeletedItems

//    // Observed in Shop Specific List Fragment. This handles the rv checkbox state of the item as purchased/unpurchased
//    val purchasedAndNotSoftDeletedItems: LiveData<List<Item>> = _purchasedAndNotSoftDeletedItems

    // LiveData to track error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun setShopId(shopId: Long) {
        currentShopId = shopId
        fetchPurchasedAndNotSoftDeletedItemsForShop()
    }

    private fun fetchPurchasedAndNotSoftDeletedItemsForShop() {
        currentShopId?.let { shopId ->
            itemRepository.getPurchasedAndNotSoftDeletedItemsByShop(shopId).observeForever { purchasedItems ->
                _purchasedAndNotSoftDeletedItems.value = purchasedItems ?: emptyList()
                Log.i("DOH!","Purchased items in vm are $purchasedItems")
            }
        }
    }




    fun handleUnpurchasedItemChecked(item: Item) {
        item.isPurchased = true
        item.lastPurchasedDate = Date()  // Setting the lastPurchasedDate to current date

        viewModelScope.launch {
            try {
                itemRepository.updateItem(item)
                updateLists()
            } catch (e: Exception) {
                // Post error message to LiveData
                _error.postValue("Failed to update item: ${e.localizedMessage}")
            }
        }
    }

    fun handlePurchasedItemChecked(item: Item) {
        item.isPurchased = false
        viewModelScope.launch {
            try {
                itemRepository.updateItem(item)
                updateLists()
            } catch (e: Exception) {
                _error.postValue("Failed to update item: ${e.localizedMessage}")
            }
        }
    }
// todo fix to where purchased items only show up for the specific shop
    private fun updateLists() {
        currentShopId?.let { shopId ->
            viewModelScope.launch {
                try {
                    val allItems = itemRepository.getItemsByShop(shopId)
                    Log.d("ShopSpecific"," all items are $allItems")
                    _unpurchasedItems.value = allItems.filter { !it.isPurchased && !it.isSoftDeleted }
                    Log.i("DOH!","Unpurchased - ${_unpurchasedItems.value}")
                    _purchasedItems.value = allItems.filter { it.isPurchased }
                } catch (e: Exception) {
                    _error.postValue("Failed to update lists: ${e.localizedMessage}")
                }
            }
        }
    }

// todo compare filtering with above function
    fun fetchShopSpecificItems(shopId: Long) {
        currentShopId = shopId
        viewModelScope.launch {
            try {
                val allItems = itemRepository.getItemsByShop(shopId)
                // Filter for active unpurchased items
                val activeUnpurchasedItems = allItems.filter { !it.isPurchased && !it.isSoftDeleted }
                _unpurchasedItems.value = activeUnpurchasedItems

                // Filter for active purchased items (not soft deleted)
                val activePurchasedItems = allItems.filter {
                    it.isPurchased && !it.isSoftDeleted
                }
                _purchasedItems.value = activePurchasedItems
            } catch (e: Exception) {
                _error.postValue("Error fetching shop-specific items: ${e.localizedMessage}")
            }
        }
    }


    fun softDeleteCheckedItems(items: List<Item>) {
        viewModelScope.launch {
            try {
                // Update each item individually
                items.forEach { item ->
                    itemRepository.updateItem(item)  // Update the item in the database
                }

                // Soft delete all items at once after individual updates
                itemRepository.softDeleteItems(items)

                // Refreshing UI models only once after all items have been updated
                currentShopId?.let { fetchShopSpecificItems(it) }
            } catch (e: Exception) {
                _error.postValue("Error deleting items: ${e.localizedMessage}")
            }
        }
    }

    fun hardDeleteSoftDeletedItems() {
        viewModelScope.launch {
            try{
                itemRepository.deleteSoftDeletedItems()
            } catch (e: Exception) {
                _error.postValue("Error deleting items: ${e.localizedMessage}")
            }
        }
    }

}


