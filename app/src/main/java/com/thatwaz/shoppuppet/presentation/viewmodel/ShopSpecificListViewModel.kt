package com.thatwaz.shoppuppet.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ItemRepository
import com.thatwaz.shoppuppet.domain.model.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopSpecificListViewModel @Inject constructor(
    private val repository: ItemRepository
) : ViewModel() {

    // LiveData for shop-specific items
    private val _shopSpecificItems = MutableLiveData<List<Item>>()
    val shopSpecificItems: LiveData<List<Item>> = _shopSpecificItems

    // Function to fetch shop-specific items based on shopId
    fun fetchShopSpecificItems(shopId: Long) {
        viewModelScope.launch {
            val result = repository.getItemsByShop(shopId)
            Log.i("DOH1","Result is $result")
            _shopSpecificItems.value = result
        }
    }
}


//@HiltViewModel
//class ShopSpecificListViewModel @Inject constructor(
//    private val repository: ItemRepository
//) : ViewModel() {
//
//    private val _items = MutableLiveData<List<Item>>()
//    val items: LiveData<List<Item>> = _items
//
//    fun fetchItemsForShop(shopId: Long) {
//        viewModelScope.launch {
//            val result = repository.getItemsByShop(shopId)
//            _items.value = result
//        }
//    }
//}
