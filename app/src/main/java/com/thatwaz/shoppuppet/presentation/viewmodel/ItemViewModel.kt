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
import com.thatwaz.shoppuppet.domain.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val shopRepository: ShopRepository,
    private val crossRefRepository: ItemShopCrossRefRepository
) : ViewModel() {

    // LiveData to observe changes in items
    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    // LiveData to observe changes in shops
    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> get() = _shops

    fun logItemsWithAssociatedShops() {
        viewModelScope.launch {
            val allItems = itemRepository.getAllItems()
            allItems.forEach { item ->
                val associatedShops = shopRepository.getShopsForItem(item.id)
                Log.d("ItemViewModel", "Item name: ${item.name}, Associated Shops: ${associatedShops.joinToString { it.name }}")
            }
        }
    }



    // Function to fetch all items associated with a specific shop
    fun fetchItemsForShop(shopId: Long) {
        viewModelScope.launch {
            _items.value = shopRepository.getItemsForShop(shopId)
        }
    }

    fun fetchAllItems() {
        viewModelScope.launch {
            val allItems = itemRepository.getAllItems()
            _items.value = allItems

            // Log items
            allItems.forEach { item ->
                Log.d("ItemViewModel", "Item name: ${item.name}, Item description: ${item.description}")
            }
        }
    }
    init {
        fetchAllItems()
    }

    // Function to fetch all shops
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

    init {
        fetchAllShops()
    }


    // Function to insert an item and associate it with given shops
    fun insertItemWithShops(item: Item, shopIds: List<Long>) {
        viewModelScope.launch {
            val itemId = itemRepository.insertItem(item)
            shopIds.forEach { shopId ->
                crossRefRepository.associateItemWithShop(itemId, shopId)
            }
        }
    }

    // Add more functions as needed...
}




//@HiltViewModel
//class ItemViewModel @Inject constructor(
//    private val itemRepository: ItemRepository
//) : ViewModel() {
//
//    // Live data to observe changes in items
//    private val _items = MutableLiveData<List<Item>>()
//    val items: LiveData<List<Item>> get() = _items
//
//    // Function to fetch all items
//    fun fetchAllItems() {
//        viewModelScope.launch {
//            _items.value = itemRepository.getAllItems()
//        }
//    }
//
//    // Other functions to handle add, update, delete operations, etc.
//    // ...
//}







//@HiltViewModel
//class ItemViewModel @Inject constructor(
//    private val itemRepository: ItemRepository
//) : ViewModel() {
//
//    val items = MutableLiveData<List<Item>>()
//
//    fun fetchItems() {
//        viewModelScope.launch {
//            items.value = itemRepository.getAllItems()
//        }
//    }
//
//    fun fetchItemsByShop(shopId: Long) {
//        viewModelScope.launch {
//            items.value = itemRepository.getItemsByShop(shopId)
//        }
//    }
//
//    fun addItem(item: Item) {
//        viewModelScope.launch {
//            itemRepository.insertItem(item)
//            // Optionally, refresh the items after adding.
//            fetchItems()
//        }
//    }
//
//    fun updateItem(item: Item) {
//        viewModelScope.launch {
//            itemRepository.updateItem(item)
//            // Optionally, refresh the items after updating.
//            fetchItems()
//        }
//    }
//
//    fun deleteItem(item: Item) {
//        viewModelScope.launch {
//            itemRepository.deleteItem(item)
//            // Optionally, refresh the items after deletion.
//            fetchItems()
//        }
//    }
//
//    fun setItemPurchased(itemId: Long, purchased: Boolean) {
//        viewModelScope.launch {
//            itemRepository.setItemPurchased(itemId, purchased)
//            // Optionally, refresh the items after changing purchase status.
//            fetchItems()
//        }
//    }
//
//    // ... You can add other functions as needed based on your app's functionality.
//}
