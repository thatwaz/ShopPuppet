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

    // Function to fetch all items from the repository
    fun fetchShopSpecificItems(shopId: Long) {
        currentShopId = shopId
        viewModelScope.launch {
            val result = repository.getItemsByShop(shopId)

            // Filter and set the values for unpurchased and purchased items
            val unpurchasedItems = result.filter { !it.isPurchased }
            val purchasedItems = result.filter { it.isPurchased }
            _unpurchasedItems.value = unpurchasedItems
            _purchasedItems.value = purchasedItems

            // Log the lists of unpurchased and purchased items
            Log.d("ViewModelLog", "Unpurchased Items: ${unpurchasedItems.joinToString { it.name }}")
            Log.d("ViewModelLog", "Purchased Items: ${purchasedItems.joinToString { it.name }}")
        }
    }


    fun handleUnpurchasedItemChecked(item: Item) {
        item.isPurchased = true
        Log.d("ViewModelLog", "Item checked as purchased: ${item.name}, isPurchased: ${item.isPurchased}")
        viewModelScope.launch {
            repository.updateItem(item)
            updateLists()
        }

    }

    fun handlePurchasedItemChecked(item: Item) {
        item.isPurchased = false
        Log.d("ViewModelLog", "Item unchecked as purchased: ${item.name}, isPurchased: ${item.isPurchased}")
        viewModelScope.launch {
            repository.updateItem(item)
            updateLists()
        }
    }



    private fun updateLists() {
        currentShopId?.let { shopId ->
            viewModelScope.launch {
                val allItems = repository.getItemsByShop(shopId)
                _unpurchasedItems.value = allItems.filter { !it.isPurchased }
                _purchasedItems.value = allItems.filter { it.isPurchased }
            }
        } ?: Log.d("ViewModelLog", "Shop ID not set")
    }

    // todo Diagnose why last item in list does not disappear in ui immediately
    fun deleteCheckedItems(items: List<Item>) {
        viewModelScope.launch {
            repository.deleteItemsWithShopAssociation(items)
            currentShopId?.let { fetchShopSpecificItems(it) }
            // Refresh data or notify UI about changes as needed
        }
    }

}


