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

@HiltViewModel
class TagItemToShopsViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {

    private val _shops = MutableLiveData<List<Shop>>()
    val shops: LiveData<List<Shop>> = _shops

    private val _selectedShops = MutableLiveData<List<Shop>>()
    val selectedShops: LiveData<List<Shop>> = _selectedShops

    init {
        loadShops()
    }

    private fun loadShops() {
        viewModelScope.launch {
            // Fetch the list of shops from your repository
            val shopsList = repository.getAllShops()
            Log.i("DOH!","Shops list is $shopsList")
            _shops.value = shopsList
        }
    }
    // Function to fetch the list of shops using a coroutine
    suspend fun fetchShops(): List<Shop> {
        try {
            val shopsList = repository.getAllShops()
            Log.d("TagItemToShopsViewModel", "Fetched ${shopsList.size} shops")
            Log.d("TagItemToShopsViewModel", "Shops are $shopsList")
            _shops.postValue(shopsList)

            return shopsList
        } catch (e: Exception) {
            // Handle the exception, e.g., log it or display an error message
            throw e // Rethrow the exception to indicate the error
        }
    }


    // Method to handle shop selection
    fun selectShop(shop: Shop) {
        val currentSelectedShops = _selectedShops.value.orEmpty().toMutableList()

        if (currentSelectedShops.contains(shop)) {
            currentSelectedShops.remove(shop)
        } else {
            currentSelectedShops.add(shop)
        }

        _selectedShops.value = currentSelectedShops
    }

}
