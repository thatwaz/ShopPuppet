package com.thatwaz.shoppuppet.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ItemShopCrossRefRepository
import com.thatwaz.shoppuppet.data.repository.ShopRepository
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.domain.model.ShopWithSelection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing item-shop associations and shop selection state.
 * Handles loading shop data, toggling shop selection, and updating item priority.
 *
 * Functions:
 * - `loadShops()`: Fetches and updates the list of all available shops.
 * - `toggleShopSelection(shop: Shop)`: Toggles the selection state of a given shop.
 * - `fetchAndSetSelectedShops(itemId: Long)`: Sets selected shops based on an item's associated shop IDs.
 *
 * LiveData:
 * - `selectedShopsLiveData`: Tracks shop selection state.
 * - `error`: LiveData for communicating errors during data processing.
 */

@HiltViewModel
class TagItemToShopsViewModel @Inject constructor(
    private val shopRepository: ShopRepository,
    private val crossRefRepository: ItemShopCrossRefRepository
) : ViewModel() {

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops


    /** LiveData holding the list of all shops */
    private val _allShopsLiveData = MutableLiveData<List<Shop>>()

    private val _selectedShopsLiveData = MutableLiveData<List<ShopWithSelection>?>()
    val selectedShopsLiveData: MutableLiveData<List<ShopWithSelection>?> = _selectedShopsLiveData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    init {
        loadShops()
    }

    private fun loadShops() {
        viewModelScope.launch {
            try {
                val shops = shopRepository.getAllShops()  // Replace with actual data fetching
                _allShopsLiveData.value = shops
                // Initialize the selectedShopsLiveData with all shops unselected
                _selectedShopsLiveData.value = shops.map { ShopWithSelection(it, false) }
            } catch (e: Exception) {
                _error.postValue("Failed to load shop: ${e.localizedMessage}")
            }

        }
    }

    /** Toggle selection state for a shop */
    fun toggleShopSelection(shop: Shop) {
        val updatedList = _selectedShopsLiveData.value?.map { shopWithSelection ->
            if (shopWithSelection.shop.id == shop.id) {
                shopWithSelection.copy(isSelected = !shopWithSelection.isSelected)
            } else {
                shopWithSelection
            }
        }
        _selectedShopsLiveData.value = updatedList
    }

    fun fetchAndSetSelectedShops(itemId: Long) {
        viewModelScope.launch {
            try {
                // Fetch IDs of shops associated with the item
                val associatedShopIds = crossRefRepository.getShopIdsForItem(itemId)

                // Fetch all shops
                val allShops = shopRepository.getAllShops()

                // Determine which shops are associated and their selection status
                val shopsWithSelection = allShops.map { shop ->
                    ShopWithSelection(shop, associatedShopIds.contains(shop.id))
                }

                // Update the LiveData with the new list
                _selectedShopsLiveData.value = shopsWithSelection
            } catch (e: Exception) {
                _error.postValue("Error fetching associated shop: ${e.localizedMessage}")
            }
        }
    }

}



