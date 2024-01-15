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






// edit vm
@HiltViewModel
class SelectedShopsViewModel @Inject constructor(
        private val shopRepository: ShopRepository
) : ViewModel() {
    private val _selectedShops = MutableLiveData<List<Shop>>(emptyList())
    val selectedShops: LiveData<List<Shop>> get() = _selectedShops



    fun addSelectedShop(shop: Shop) {
        val newSelectedShops = selectedShops.value.orEmpty().toMutableList().apply {
            if (!contains(shop)) {
                add(shop)
            }
        }
        _selectedShops.value = newSelectedShops
        Log.d("SelectedShops", "Added shop, new list: $newSelectedShops")
    }


    fun removeSelectedShop(shop: Shop) {
        if (selectedShops.value.orEmpty().contains(shop)) {
            val newSelectedShops = selectedShops.value.orEmpty().filter { it != shop }
            _selectedShops.value = newSelectedShops
            Log.d("SelectedShops", "Removed shop, new list: $newSelectedShops")
        } else {
            Log.d("SelectedShops", "Attempted to remove a shop not in the list: $shop")
            // You could handle this case differently if needed.
        }
    }


        fun initializeSelectedShops(shops: List<Shop>) {
            Log.i("SSVM","sel shops live data is ${_selectedShops.value}")
        _selectedShops.value = shops

    }

        fun setSelectedShopIds(shopIds: List<Long>) {
        viewModelScope.launch {
            try {
                // Fetch shops by their IDs
                val shops = shopRepository.getShopsByIds(shopIds)

                // Update the selected shops
                _selectedShops.postValue(shops)
                Log.i("SSVM","fetching shop ids: $shops")
            } catch (e: Exception) {
                // Handle any exceptions
                Log.e("SelectedShopsViewModel", "Error setting selected shops: ${e.message}")
            }
        }
    }
    fun isSelected(shop: Shop): Boolean {
        Log.i("Crow","Checked shop is ${_selectedShops.value}")
        return _selectedShops.value.orEmpty().contains(shop)
    }
}




