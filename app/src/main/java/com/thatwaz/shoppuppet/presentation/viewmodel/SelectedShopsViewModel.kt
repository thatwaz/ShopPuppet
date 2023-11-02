package com.thatwaz.shoppuppet.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thatwaz.shoppuppet.domain.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SelectedShopsViewModel @Inject constructor() : ViewModel() {
    private val _selectedShops = MutableLiveData<List<Shop>>()
    val selectedShops: LiveData<List<Shop>> get() = _selectedShops

    fun addSelectedShop(shop: Shop) {
        val currentList = _selectedShops.value ?: emptyList()
        _selectedShops.value = currentList + shop
    }

    fun removeSelectedShop(shop: Shop) {
        val currentList = _selectedShops.value ?: emptyList()
        _selectedShops.value = currentList - shop
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
