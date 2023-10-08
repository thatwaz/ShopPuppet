package com.thatwaz.shoppuppet.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ShopRepository
import com.thatwaz.shoppuppet.domain.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val shopRepository: ShopRepository
) : ViewModel() {

    val shops = MutableLiveData<List<Shop>>()

    fun fetchShops() {
        viewModelScope.launch {
            shops.value = shopRepository.getAllShops()
        }
    }

    // ... other functions for adding, updating, deleting shops
}
