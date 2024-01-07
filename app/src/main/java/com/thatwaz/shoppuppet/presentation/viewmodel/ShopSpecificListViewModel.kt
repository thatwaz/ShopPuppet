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
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class ShopSpecificListViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {


    private var currentShopId: Long? = null

    // LiveData for unpurchased items
    private val _unpurchasedItems = MutableLiveData<List<Item>>()
    val unpurchasedItems: LiveData<List<Item>> = _unpurchasedItems

    // LiveData for purchased items
    private val _purchasedItems = MutableLiveData<List<Item>>()
    val purchasedItems: LiveData<List<Item>> = _purchasedItems

    private val _purchasedAndSoftDeletedItems = MutableLiveData<List<Item>>()
    var purchasedAndSoftDeletedItems: LiveData<List<Item>> = _purchasedAndSoftDeletedItems

    private val _purchasedAndNotSoftDeletedItems = MutableLiveData<List<Item>>()
    var purchasedAndNotSoftDeletedItems: LiveData<List<Item>> = _purchasedAndNotSoftDeletedItems

    private val _softDeletedItems = MutableLiveData<List<Item>>()
    val softDeletedItems: LiveData<List<Item>> = _softDeletedItems

    // Function to fetch all items from the repository
    init {
        fetchPurchasedAndSoftDeletedItems()
        fetchPurchasedAndNotSoftDeletedItems()
    }



//    fun fetchShopSpecificItems(shopId: Long) {
//        currentShopId = shopId
//        viewModelScope.launch {
//            val allItems = repository.getItemsByShop(shopId)
//
//            // Active Unpurchased Items: Not marked as purchased, presumably available to buy.
//            val activeUnpurchasedItems = allItems.filter { !it.isPurchased }
//
//            // Active Purchased Items: Marked as purchased recently (within the last 30 days).
//            val activePurchasedItems = allItems.filter {
//                it.isPurchased && (it.lastPurchasedDate?.time ?: 0L) > getThirtyDaysAgo()
//            }
//
//            _unpurchasedItems.value = activeUnpurchasedItems
//            _purchasedItems.value = activePurchasedItems
//
//            Log.d("ViewModelLog", "Active Unpurchased Items: ${activeUnpurchasedItems.joinToString { it.name }}")
//            Log.d("ViewModelLog", "Active Purchased Items: ${activePurchasedItems.joinToString { it.name }}")
//        }
//    }


    private fun getThirtyDaysAgo(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -30)  // Subtract 30 days from the current date
        return calendar.timeInMillis  // Return the time 30 days ago in milliseconds
    }

    fun handleUnpurchasedItemChecked(item: Item) {
        item.isPurchased = true
        item.lastPurchasedDate = Date()  // Setting the lastPurchasedDate to current date
        Log.d("ViewModelLog", "Item checked as purchased: ${item.name}, isPurchased: ${item.isPurchased}")
        viewModelScope.launch {
            repository.updateItem(item)
            updateLists()
        }
    }

    fun handlePurchasedItemChecked(item: Item) {
        item.isPurchased = false
        // Decide how you want to handle lastPurchasedDate when an item is unchecked as purchased
        Log.d("ViewModelLog", "Item unchecked as purchased: ${item.name}, isPurchased: ${item.isPurchased}")
        viewModelScope.launch {
            repository.updateItem(item)
            updateLists()
        }
    }

    fun fetchPurchasedAndSoftDeletedItems() {
        purchasedAndSoftDeletedItems = repository.getPurchasedAndSoftDeletedItems()
    }

    fun fetchPurchasedAndNotSoftDeletedItems() {
        purchasedAndNotSoftDeletedItems = repository.getPurchasedAndNotSoftDeletedItems()
    }


    private fun updateLists() {
        currentShopId?.let { shopId ->
            viewModelScope.launch {
                val allItems = repository.getItemsByShop(shopId)
                _unpurchasedItems.value = allItems.filter { !it.isPurchased }
                _purchasedItems.value = allItems.filter { it.isPurchased }
                Log.i("ViewModelLog","${_purchasedItems.value} Item is now purchased")
            }
        } ?: Log.d("ViewModelLog", "Shop ID not set")
    }

    // todo Diagnose why last item in list does not disappear in ui immediately

//    fun softDeleteCheckedItems(items: List<Item>) {
//        viewModelScope.launch {
//            items.forEach { item ->
//                item.isPurchased = true  // Keep marked as purchased
//                item.lastPurchasedDate = Date()  // Update the last purchased date
//                // Additional flag or logic for explicit deletion if needed
//                repository.updateItem(item)
//            }
//            updateLists()  // This will refresh both lists and remove soft-deleted items
//        }
//    }

    fun fetchShopSpecificItems(shopId: Long) {
        currentShopId = shopId
        viewModelScope.launch {
            val allItems = repository.getItemsByShop(shopId)

            // Filter for active unpurchased items
            val activeUnpurchasedItems = allItems.filter { !it.isPurchased && !it.isSoftDeleted }
            _unpurchasedItems.value = activeUnpurchasedItems

            // Filter for active purchased items (not soft deleted)
            val activePurchasedItems = allItems.filter {
                it.isPurchased && !it.isSoftDeleted && (it.lastPurchasedDate?.time ?: 0L) > getThirtyDaysAgo()
            }
            _purchasedItems.value = activePurchasedItems

            // You don't necessarily need a separate LiveData for softDeletedItems unless you want to specifically track or manage them.
        }
    }
//TODO FIGURE OUT THE DISCONNECT BETWEEN THE ABOVE AND BELOW FUNCTION

    fun softDeleteCheckedItems(items: List<Item>) {
        viewModelScope.launch {
            items.forEach { item ->

                item.isPurchased = true  // Soft delete: mark as purchased
                item.lastPurchasedDate = Date()  // Set the current date as the last purchased date
                Log.i("ViewModelLog","date of last purchase is ${item.lastPurchasedDate}")
                repository.updateItem(item)  // Update the item in the database
                //todo below works the way you had it
//                repository.deleteItemsWithShopAssociation(items)
                repository.softDeleteItems(items)
                currentShopId?.let { fetchShopSpecificItems(it) }
            }
            currentShopId?.let { fetchShopSpecificItems(it) }
            // This refetches items and should update LiveData
        }
    }

}


