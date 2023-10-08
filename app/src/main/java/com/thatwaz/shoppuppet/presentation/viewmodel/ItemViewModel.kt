package com.thatwaz.shoppuppet.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ItemRepository
import com.thatwaz.shoppuppet.domain.model.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository
) : ViewModel() {

    val items = MutableLiveData<List<Item>>()

    fun fetchItems() {
        viewModelScope.launch {
            items.value = itemRepository.getAllItems()
        }
    }

    fun fetchItemsByShop(shopId: Long) {
        viewModelScope.launch {
            items.value = itemRepository.getItemsByShop(shopId)
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch {
            itemRepository.insertItem(item)
            // Optionally, refresh the items after adding.
            fetchItems()
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            itemRepository.updateItem(item)
            // Optionally, refresh the items after updating.
            fetchItems()
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemRepository.deleteItem(item)
            // Optionally, refresh the items after deletion.
            fetchItems()
        }
    }

    fun setItemPurchased(itemId: Long, purchased: Boolean) {
        viewModelScope.launch {
            itemRepository.setItemPurchased(itemId, purchased)
            // Optionally, refresh the items after changing purchase status.
            fetchItems()
        }
    }

    // ... You can add other functions as needed based on your app's functionality.
}
