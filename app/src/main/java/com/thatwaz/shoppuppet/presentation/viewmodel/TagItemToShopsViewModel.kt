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
import com.thatwaz.shoppuppet.domain.model.ShopWithSelection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagItemToShopsViewModel @Inject constructor(
    private val repository: ShopRepository,
    private val itemRepository: ItemRepository,
    private val shopRepository: ShopRepository,
    private val crossRefRepository: ItemShopCrossRefRepository
) : ViewModel() {

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops


    // LiveData holding the list of all shops
    private val _allShopsLiveData = MutableLiveData<List<Shop>>()
    val allShopsLiveData: LiveData<List<Shop>> = _allShopsLiveData

    private val _selectedShopsLiveData = MutableLiveData<List<ShopWithSelection>>()
    val selectedShopsLiveData: LiveData<List<ShopWithSelection>> = _selectedShopsLiveData

    //todo get shops to log
    private val _selectedShops = MutableLiveData<List<Shop>>()
    val selectedShops: LiveData<List<Shop>> = _selectedShops

    init {
        loadShops()
    }

    fun loadShops() {
        viewModelScope.launch {
            val shops = shopRepository.getAllShops()  // Replace with actual data fetching
            _allShopsLiveData.value = shops
            Log.i("CRACK","shops loading are $shops")
            // Initialize the selectedShopsLiveData with all shops unselected
            _selectedShopsLiveData.value = shops.map { ShopWithSelection(it, false) }
        }
    }

    // Toggle selection state for a shop
    fun toggleShopSelection(shop: Shop) {
        _selectedShopsLiveData.value = _selectedShopsLiveData.value?.map { shopWithSelection ->
            if (shopWithSelection.shop.id == shop.id) {
                // Toggle the selection state
                shopWithSelection.copy(isSelected = !shopWithSelection.isSelected)
            } else {
                shopWithSelection
            }
        }
    }

//    private fun loadShops() {
//        viewModelScope.launch {
//            // Fetch the list of shops from your repository
//            val shopsList = repository.getAllShops()
//            Log.i("DOH!", "Shops list is $shopsList")
//            _shops.value = shopsList
//        }
//    }

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

    fun fetchShopsByIds(shopIds: List<Long>): LiveData<List<Shop>> {
        val result = MutableLiveData<List<Shop>>()
        viewModelScope.launch {
            // Fetch the shops from the repository
            val shops = shopRepository.getShopsByIds(shopIds)
            result.postValue(shops)
        }
        return result
    }


//    fun fetchShopsByIds(shopIds: LongArray): LiveData<List<Shop>> {
//        val result = MutableLiveData<List<Shop>>()
//        viewModelScope.launch {
//            // Fetch the shops from the repository
//            val shops = shopRepository.getShopsByIds(shopIds)
//            result.postValue(shops)
//        }
//        return result
//    }

    fun fetchAndSetSelectedShops(itemId: Long) {
        viewModelScope.launch {
            try {
                // Log the itemId received
                Log.i("Crappy", "Fetching associated shops for itemId: $itemId")

                // Get the list of shop IDs associated with the item
                val associatedShopIds = crossRefRepository.getShopIdsForItem(itemId)

                // Log the associated shop IDs
                Log.i("Crappy", "Associated shop IDs: $associatedShopIds")

                // Fetch shops by their IDs
                val associatedShops = shopRepository.getShopsByIds(associatedShopIds)

                // Log the associated shops
                Log.i("Crappy", "Associated shops: $associatedShops")

                // Update the selected shops
                _selectedShops.postValue(associatedShops)
            } catch (e: Exception) {
                // Handle any exceptions, such as logging or showing an error message
                Log.e("Crappy", "Error fetching associated shops: ${e.message}")
            }
        }
    }




//    fun setSelectedShops(shopIds: LongArray) {
//        val selectedShops = _selectedShops.value.orEmpty().filter { it.id in shopIds }
//        _selectedShops.value = selectedShops
//    }

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
        val shopIds = crossRefRepository.getShopIdsForItem(itemId)

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


