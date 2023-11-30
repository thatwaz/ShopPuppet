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


//    private fun updateLists() {
//        val allItems = (_unpurchasedItems.value.orEmpty() + _purchasedItems.value.orEmpty())
//        _unpurchasedItems.value = allItems.filter { !it.isPurchased }
//        _purchasedItems.value = allItems.filter { it.isPurchased }
//        Log.i("ViewModelLog","updating lists, purchased is ${_purchasedItems.value}")
//    }
}


//@HiltViewModel
//class ShopSpecificListViewModel @Inject constructor(
//    private val repository: ItemRepository
//) : ViewModel() {
//
//
//    private val _shopSpecificItems = MutableLiveData<List<Item>>()
//    val shopSpecificItems: LiveData<List<Item>> = _shopSpecificItems
//
//
//    private val _shopSpecificCheckedItems = MutableLiveData<List<Item>>()
//    val shopSpecificCheckedItems: LiveData<List<Item>> = _shopSpecificCheckedItems
//
//    private val _purchasedCheckedItems = MutableLiveData<List<Item>>()
//    val purchasedCheckedItems: LiveData<List<Item>> = _purchasedCheckedItems
//
//
//    // LiveData for unpurchased items
//    private val _unpurchasedItems = MutableLiveData<List<Item>>()
//    val unpurchasedItems: LiveData<List<Item>> = _unpurchasedItems
//
//    // LiveData for purchased items
//    private val _purchasedItems = MutableLiveData<List<Item>>()
//    val purchasedItems: LiveData<List<Item>> = _purchasedItems
//
//
//
//    fun updateShopSpecificCheckedItems(newCheckedItems: List<Item>) {
//        _shopSpecificCheckedItems.value = newCheckedItems
//        Log.d("ShopSpecificListViewModel", "Updated shop-specific checked items: $newCheckedItems")
//    }
//
//
//    fun updatePurchasedCheckedItems(newCheckedItems: List<Item>) {
//        _purchasedCheckedItems.value = newCheckedItems
//        Log.d("ShopSpecificListViewModel", "Updated purchased checked items: $newCheckedItems")
//    }
//
//
//
//
//    // Function to fetch all items from the repository
//    fun fetchShopSpecificItems(shopId: Long) {
//        viewModelScope.launch {
//            val result = repository.getItemsByShop(shopId)
//            Log.i("DOH1", "Result is $result")
//            _shopSpecificItems.value = result
//
//            // Split items into unpurchased and purchased lists
//            val unpurchased = result.filter { !it.isPurchased }
//            val purchased = result.filter { it.isPurchased }
//
//            _unpurchasedItems.value = unpurchased
//            _purchasedItems.value = purchased
//            Log.i("DOH1", "Unpurchased items: $unpurchased")
//            Log.i("DOH1", "purchased items: $purchased")
//        }
//    }
//}


//@HiltViewModel
//class ShopSpecificListViewModel @Inject constructor(
//    private val repository: ItemRepository
//) : ViewModel() {
//
//    private val _items = MutableLiveData<List<Item>>()
//    val items: LiveData<List<Item>> = _items
//
//    fun fetchItemsForShop(shopId: Long) {
//        viewModelScope.launch {
//            val result = repository.getItemsByShop(shopId)
//            _items.value = result
//        }
//    }
//}
