package com.thatwaz.shoppuppet.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ItemRepository
import com.thatwaz.shoppuppet.data.repository.ItemShopCrossRefRepository
import com.thatwaz.shoppuppet.data.repository.ShopRepository
import com.thatwaz.shoppuppet.domain.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagItemToShopsViewModel @Inject constructor(
    private val repository: ShopRepository,
    private val itemRepository: ItemRepository,
    private val shopRepository: ShopRepository,
    private val itemShopCrossRefRepository: ItemShopCrossRefRepository
) : ViewModel() {

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops

    private val _selectedShops = MutableLiveData<List<Shop>>()
    val selectedShops: LiveData<List<Shop>> = _selectedShops

    init {
        loadShops()
    }

    private fun loadShops() {
        viewModelScope.launch {
            // Fetch the list of shops from your repository
            val shopsList = repository.getAllShops()
            Log.i("DOH!", "Shops list is $shopsList")
            _shops.value = shopsList
        }
    }

    fun updateItemPriority(itemId: Long, isPriority: Boolean) {
        Log.d("ViewModelLog", "Updating item priority. Item ID: $itemId, New Priority: $isPriority")

        viewModelScope.launch {
            try {
                // Update the item's priority status
                Log.d("ViewModelLog", "Calling repository to update item priority")
                itemRepository.updateItemPriority(itemId, isPriority)

                // Reflect these changes in the shop list
                Log.d("ViewModelLog", "Updating shops for item priority change")
                updateShopsForItemPriorityChange(itemId, isPriority)

                Log.d("ViewModelLog", "Item priority update and shop refresh complete")
            } catch (e: Exception) {
                Log.e("ViewModelLog", "Error updating item priority: ${e.message}")
                // Handle exceptions, like showing an error message
            }
        }
    }

//    fun updateItemPriority(itemId: Long, isPriority: Boolean) {
//        viewModelScope.launch {
//            try {
//                // Update the item's priority status
//                itemRepository.updateItemPriority(itemId, isPriority)
//                // Reflect these changes in the shop list
//                updateShopsForItemPriorityChange(itemId, isPriority)
//            } catch (e: Exception) {
//                // Handle exceptions, like showing an error message
//            }
//        }
//    }

    //todo add logic here
    private suspend fun updateShopsForItemPriorityChange(itemId: Long, isPriority: Boolean) {
        // Get the list of shop IDs associated with the item
        val shopIds = itemShopCrossRefRepository.getShopIdsForItem(itemId)

        // Update the priority status in shops
        shopRepository.updatePriorityStatusForShops(shopIds, isPriority)

        // Refresh the shops LiveData
        loadShops()
    }

//    private suspend fun updateShopsForItemPriorityChange(itemId: Long, isPriority: Boolean) {
//        // Get the list of shop IDs associated with the item
//        val shopIds = itemShopCrossRefRepository.getShopIdsForItem(itemId)
//        // Update the shops list
//        if (isPriority) {
//            // Add logic to handle the case when an item is set as a priority
//            // This might involve updating the shops' status in the UI
//        } else {
//            // Handle the case when an item's priority is removed
//        }
//        // Refresh the shops LiveData
//        loadShops()
//    }





}
    // Function to fetch the list of shops using a coroutine
//    suspend fun fetchShops(): List<Shop> {
//        try {
//            val shopsList = repository.getAllShops()
//            Log.d("TagItemToShopsViewModel", "Fetched ${shopsList.size} shops")
//            Log.d("TagItemToShopsViewModel", "Shops are $shopsList")
//            _shops.postValue(shopsList)
//
//            return shopsList
//        } catch (e: Exception) {
//            // Handle the exception, e.g., log it or display an error message
//            throw e // Rethrow the exception to indicate the error
//        }
//    }


    // Method to handle shop selection
//    fun selectShop(shop: Shop) {
//        val currentSelectedShops = _selectedShops.value.orEmpty().toMutableList()
//
//        if (currentSelectedShops.contains(shop)) {
//            currentSelectedShops.remove(shop)
//        } else {
//            currentSelectedShops.add(shop)
//        }
//
//        _selectedShops.value = currentSelectedShops
//    }


