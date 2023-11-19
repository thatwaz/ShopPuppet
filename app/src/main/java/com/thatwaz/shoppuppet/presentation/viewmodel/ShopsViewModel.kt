package com.thatwaz.shoppuppet.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ShopRepository
import com.thatwaz.shoppuppet.domain.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> get() = _shops

    init {
        fetchShops()
    }

    private fun fetchShops() {
        viewModelScope.launch {
            _shops.value = shopRepository.getAllShops()
        }
    }

    fun deleteShop(shop: Shop) {
        viewModelScope.launch {
            shopRepository.deleteShop(shop)
            fetchShops() // Refresh the shops list
        }
    }
}

//@HiltViewModel
//class ShopsViewModel @Inject constructor(
//    private val shopRepository: ShopRepository
//) : ViewModel() {
//
//    private val _shops = MutableLiveData<List<Shop>>()
//    val shops: LiveData<List<Shop>> get() = _shops
//
//    val allShops: LiveData<List<Shop>> = liveData {
//        emit(shopRepository.getAllShops())
//    }
//
//    // This function may not be necessary if you use the liveData builder as above
//    fun fetchShops() {
//        viewModelScope.launch {
//            _shops.value = shopRepository.getAllShops()
//        }
//    }
//
//    fun deleteShop(shop: Shop) {
//        viewModelScope.launch {
//            try {
//                shopRepository.deleteShop(shop)
//                val updatedShops = shopRepository.getAllShops()
////                _allShops.value = updatedShops
//
//                // Optionally, refresh the list of shops or post a success message
//            } catch (e: Exception) {
//                // Handle any exceptions, e.g., show an error message
//            }
//        }
//    }
//}







//@HiltViewModel
//class ShopViewModel @Inject constructor(
//    private val shopRepository: ShopRepository
//) : ViewModel() {
//
//    val shops = MutableLiveData<List<Shop>>()
//
//    fun fetchShops() {
//        viewModelScope.launch {
//            shops.value = shopRepository.getAllShops()
//        }
//    }
//
//
//}
