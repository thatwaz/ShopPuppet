package com.thatwaz.shoppuppet.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ShopRepository
import com.thatwaz.shoppuppet.domain.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel for managing the selection of shops.
 * Keeps track of user-selected shops and provides functionality for adding or removing shops from
 * the selection.
 * Exposes LiveData for the list of selected shops and any errors encountered during operations.
 * Also facilitates fetching and setting shops based on their IDs, enhancing the ViewModel's
 * integration with the repository layer.
 */
@HiltViewModel
class SelectedShopsViewModel @Inject constructor(
        private val shopRepository: ShopRepository
) : ViewModel() {
    private val _selectedShops = MutableLiveData<List<Shop>>(emptyList())
    val selectedShops: LiveData<List<Shop>> get() = _selectedShops

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // todo fix to where shops are still tagged after config. change
    fun addSelectedShop(shop: Shop) {
        val newSelectedShops = selectedShops.value.orEmpty().toMutableList().apply {
            if (!contains(shop)) {
                add(shop)
            }
        }
        _selectedShops.value = newSelectedShops
        Log.i("ShopObserver","Observing $newSelectedShops")
    }



    fun removeSelectedShop(shop: Shop) {
        val currentSelectedShops = selectedShops.value.orEmpty()
        if (currentSelectedShops.contains(shop)) {
            val newSelectedShops = currentSelectedShops.filter { it != shop }
            _selectedShops.value = newSelectedShops
        } else {
            _error.postValue("Shop not found in the selection.")
        }
    }


    fun setSelectedShopIds(shopIds: List<Long>) {
        viewModelScope.launch {
            try {
                // Fetch shops by their IDs
                val shops = shopRepository.getShopsByIds(shopIds)

                // Update the selected shops
                _selectedShops.postValue(shops)

            } catch (e: Exception) {
                _error.postValue("Error setting selected shops: ${e.localizedMessage}")
            }
        }
    }

}




