package com.thatwaz.shoppuppet.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ShopRepository
import com.thatwaz.shoppuppet.domain.model.Shop
import com.thatwaz.shoppuppet.domain.model.ShopWithItemCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel for managing and displaying shop data.
 *
 * Offers functionality to fetch, update, and delete shop information, interfacing with the ShopRepository.
 * Exposes LiveData for shop lists and error messages, supporting responsive UI updates.
 *
 * - fetchShopsWithItemCount(): Fetches shops with item counts and priority status.
 * - deleteShop(shop: Shop): Deletes a specified shop and refreshes the list.
 * - error: LiveData for tracking and displaying errors during data operations.
 */

@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _shops = MutableLiveData<List<Shop>>()

    private val _shopsWithItemCount = MutableLiveData<List<ShopWithItemCount>>()

    val shopsWithItemCount: LiveData<List<ShopWithItemCount>> get() = _shopsWithItemCount

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        fetchShopsWithItemCount()
    }

    fun fetchShopsWithItemCount() {
        viewModelScope.launch {
            try {
                // Fetch shops with item count and priority status
                val shopsWithItemCount = shopRepository.getShopsWithItemCountAndPriorityStatus()
                _shopsWithItemCount.value = shopsWithItemCount
            } catch (e: Exception) {
                _error.postValue("Failed to fetch shop with item count: ${e.localizedMessage}")
            }

        }
    }

    private fun fetchShops() {
        viewModelScope.launch {
            try {
                _shops.value = shopRepository.getAllShops()
            } catch (e: Exception) {
                _error.postValue("Failed to fetch shop: ${e.localizedMessage}")
            }
        }
    }

    fun deleteShop(shop: Shop) {
        viewModelScope.launch {
            try {
                shopRepository.deleteShop(shop)
                fetchShops() // Refresh the shops list
            } catch (e: Exception) {
                _error.postValue("Failed to delete shop: ${e.localizedMessage}")
            }

        }
    }

}


