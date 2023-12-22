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
        if (!_selectedShops.value.orEmpty().contains(shop)) {
            _selectedShops.value = _selectedShops.value.orEmpty().toMutableList().apply {
                add(shop)
                Log.i("crow","selected shops are: ${_selectedShops.value}")
            }
        }
    }


    fun removeSelectedShop(shop: Shop) {
        val newList = _selectedShops.value.orEmpty().filter { it != shop }
        Log.i("crow","Removing shop: $shop. New list: $newList")
        _selectedShops.value = newList
    }

        fun initializeSelectedShops(shops: List<Shop>) {
        _selectedShops.value = shops
    }

        fun setSelectedShopIds(shopIds: List<Long>) {
        viewModelScope.launch {
            try {
                // Fetch shops by their IDs
                val shops = shopRepository.getShopsByIds(shopIds)

                // Update the selected shops
                _selectedShops.postValue(shops)
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
//@HiltViewModel
//class SelectedShopsViewModel @Inject constructor(
//    private val shopRepository: ShopRepository
//) : ViewModel() {
//
//
////    private val _allShops = MutableLiveData<List<Shop>>()
////    val allShops: LiveData<List<Shop>> get() = _allShops
//
//    private val _selectedShops = MutableLiveData<List<Shop>>()
//    val selectedShops: LiveData<List<Shop>> get() = _selectedShops
//
//        //todo this does not recogninze previously checked shops until a shop is rechecked
//    fun addSelectedShop(shop: Shop) {
//        val currentSelectedShops = _selectedShops.value.orEmpty().toMutableList()
//        if (!currentSelectedShops.contains(shop)) {
//            currentSelectedShops.add(shop)
//            _selectedShops.value = currentSelectedShops
//            Log.i("Crappy","checked shops should be: $currentSelectedShops")
//        }
//    }
//    fun initializeSelectedShops(shops: List<Shop>) {
//        _selectedShops.value = shops
//    }
//
//
//    fun removeSelectedShop(shop: Shop) {
//        val currentSelectedShops = _selectedShops.value.orEmpty().toMutableList()
//        currentSelectedShops.remove(shop)
//        _selectedShops.value = currentSelectedShops
//        Log.i("Crappy","this shop has been removed: $currentSelectedShops")
//    }
//
//    fun setSelectedShopIds(shopIds: List<Long>) {
//        viewModelScope.launch {
//            try {
//                // Fetch shops by their IDs
//                val shops = shopRepository.getShopsByIds(shopIds)
//
//                // Update the selected shops
//                _selectedShops.postValue(shops)
//            } catch (e: Exception) {
//                // Handle any exceptions
//                Log.e("SelectedShopsViewModel", "Error setting selected shops: ${e.message}")
//            }
//        }
//    }
//    fun isSelected(shop: Shop): Boolean {
//        Log.i("Crow","Checked shop is ${_selectedShops.value}")
//        return _selectedShops.value.orEmpty().contains(shop)
//    }
//}



