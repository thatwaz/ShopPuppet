package com.thatwaz.shoppuppet.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thatwaz.shoppuppet.domain.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectedShopsViewModel @Inject constructor() : ViewModel() {
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

//    fun removeSelectedShop(shop: Shop) {
//        _selectedShops.value = _selectedShops.value.orEmpty().filter { it != shop }
//    }

    fun isSelected(shop: Shop): Boolean {
        Log.i("Crow","Checked shop is ${_selectedShops.value}")
        return _selectedShops.value.orEmpty().contains(shop)
    }
}



//@HiltViewModel
//class SelectedShopsViewModel @Inject constructor() : ViewModel() {
//    private val _selectedShops = MutableLiveData<List<Shop>>()
//    val selectedShops: LiveData<List<Shop>> get() = _selectedShops
//
//    fun addSelectedShop(shop: Shop) {
//        val currentList = _selectedShops.value ?: emptyList()
//        _selectedShops.value = currentList + shop
//    }
//
//    fun removeSelectedShop(shop: Shop) {
//        val currentList = _selectedShops.value ?: emptyList()
//        _selectedShops.value = currentList - shop
//    }
//}
