package com.thatwaz.shoppuppet.presentation.viewmodel

import android.util.Log
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




@HiltViewModel
class ShopsViewModel @Inject constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {

    private val _shops = MutableLiveData<List<Shop>>()
    private val _shopsWithItemCount = MutableLiveData<List<ShopWithItemCount>>()

    val shopsWithItemCount: LiveData<List<ShopWithItemCount>> get() = _shopsWithItemCount


    val shops: LiveData<List<Shop>> get() = _shops

    init {
        fetchShopsWithItemCount()
    }

    fun fetchShopsWithItemCount() {
        viewModelScope.launch {
            // Fetch shops with item count and priority status
            val shopsWithItemCount = shopRepository.getShopsWithItemCountAndPriorityStatus()
            _shopsWithItemCount.value = shopsWithItemCount
        }
    }


//    fun fetchShopsWithItemCount() {
//        viewModelScope.launch {
//            val shops = shopRepository.getAllShops()
//            val shopsWithItemCount = shops.map { shop ->
//                ShopWithItemCount(
//                    shop = shop,
//                    itemCount = shopRepository.getItemsCountForShop(shop.id) // Assuming this method exists
//                )
//            }
//            _shopsWithItemCount.value = shopsWithItemCount
//
//        }
//    }

    private fun fetchShops() {
        viewModelScope.launch {
            _shops.value = shopRepository.getAllShops()
            Log.i("DOH!","Shops are ${_shops.value}")
        }
    }

    fun deleteShop(shop: Shop) {
        viewModelScope.launch {
            shopRepository.deleteShop(shop)
            fetchShops() // Refresh the shops list
        }
    }


}


