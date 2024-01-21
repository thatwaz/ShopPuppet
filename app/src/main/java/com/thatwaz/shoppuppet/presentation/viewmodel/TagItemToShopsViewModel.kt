package com.thatwaz.shoppuppet.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ItemRepository
import com.thatwaz.shoppuppet.data.repository.ItemShopCrossRefRepository
import com.thatwaz.shoppuppet.data.repository.ShopRepository
import com.thatwaz.shoppuppet.domain.model.ItemUiModel
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
//    val allShopsLiveData: LiveData<List<Shop>> = _allShopsLiveData

    private val _selectedShopsLiveData = MutableLiveData<List<ShopWithSelection>?>()
    val selectedShopsLiveData: MutableLiveData<List<ShopWithSelection>?> = _selectedShopsLiveData

    private val _itemUiModels = MutableLiveData<List<ItemUiModel>>()
    val itemUiModels: LiveData<List<ItemUiModel>> = _itemUiModels

    private val _isPriority = MutableLiveData<Boolean>(false)
    val isPriority: LiveData<Boolean> = _isPriority

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Method to update isPriority
    fun setIsPriority(priority: Boolean) {
        _isPriority.value = priority
    }

    init {
        loadShops()
    }

    fun loadShops() {
        viewModelScope.launch {
            val shops = shopRepository.getAllShops()  // Replace with actual data fetching
            _allShopsLiveData.value = shops
            Log.i("CRACK", "shops loading are $shops")
            // Initialize the selectedShopsLiveData with all shops unselected
            _selectedShopsLiveData.value = shops.map { ShopWithSelection(it, false) }
        }
    }

    // Toggle selection state for a shop

    fun toggleShopSelection(shop: Shop) {
        Log.i("GravyBoat", "toggleShopSelection called for shop: ${shop.name}")
        Log.i("GravyBoat", "toggle shops be like ${selectedShopsLiveData.value}")
        val updatedList = _selectedShopsLiveData.value?.map { shopWithSelection ->
            if (shopWithSelection.shop.id == shop.id) {
                shopWithSelection.copy(isSelected = !shopWithSelection.isSelected)
            } else {
                shopWithSelection
            }
        }
        // IMPORTANT: Post the updated list back to LiveData
        _selectedShopsLiveData.value = updatedList
        Log.i("GravyBoat", "updated list is $updatedList")
    }

    fun fetchAndSetSelectedShops(itemId: Long) {
        viewModelScope.launch {
            try {
                Log.i("Crappy", "Fetching associated shops for itemId: $itemId")
                // Fetch IDs of shops associated with the item
                val associatedShopIds = crossRefRepository.getShopIdsForItem(itemId)
                Log.i("Crappy", "Associated shop IDs: $associatedShopIds")

                // Fetch all shops
                val allShops = shopRepository.getAllShops()

                // Determine which shops are associated and their selection status
                val shopsWithSelection = allShops.map { shop ->
                    ShopWithSelection(shop, associatedShopIds.contains(shop.id))
                }

                // Log the shops determined to be associated with the item
                val associatedShops = shopsWithSelection.filter { it.isSelected }
                Log.i("Crappy", "Associated shops: ${associatedShops.map { it.shop }}")

                // Update the LiveData with the new list
                _selectedShopsLiveData.value = shopsWithSelection
            } catch (e: Exception) {
                Log.e("Crappy", "Error fetching associated shops: ${e.message}")
            }
        }
    }

    //todo this is only called when setting initial priority
    fun setInitialItemPriorityStatus(itemId: Long, isPriority: Boolean) {
        Log.d("Bazinga", "Updating item priority. Item ID: $itemId, New Priority: $isPriority")

        viewModelScope.launch {

            try {
                // Update the item's priority status
                Log.d("Bazinga", "Calling repository to update item priority")
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


    //todo add logic here
    private suspend fun updateShopsForItemPriorityChange(itemId: Long, isPriority: Boolean) {
        // Get the list of shop IDs associated with the item
        val shopIds = crossRefRepository.getShopIdsForItem(itemId)

        // Update the priority status in shops
        shopRepository.updatePriorityStatusForShops(shopIds, isPriority)

        // Refresh the shops LiveData
        loadShops()
    }
}



