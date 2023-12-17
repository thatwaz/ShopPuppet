package com.thatwaz.shoppuppet.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ShopRepository
import com.thatwaz.shoppuppet.domain.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject



@HiltViewModel
class AddShopViewModel @Inject constructor(
    application: Application,
    private val repository: ShopRepository
) : AndroidViewModel(application) {

    // LiveData for shop details
    private val _shopName = MutableLiveData<String?>()
    val shopName: LiveData<String?> get() = _shopName

//    private val _selectedIconRef = MutableLiveData<Int>()
//    val selectedIconRef: LiveData<Int> get() = _selectedIconRef

//    private val _selectedIconRef = MutableLiveData<Int?>()
//    val selectedIconRef: LiveData<Int?> get() = _selectedIconRef

    private val _selectedIconResName = MutableLiveData<String?>()
    val selectedIconResName: LiveData<String?> get() = _selectedIconResName



    private val _selectedColorResName = MutableLiveData<String?>()
    val selectedColorResName: LiveData<String?> get() = _selectedColorResName

//    private val _selectedColor = MutableLiveData(R.color.black)
//    val selectedColor: LiveData<Int> get() = _selectedColor

    private val _shopInitials = MutableLiveData<String?>()
    val shopInitials: LiveData<String?> get() = _shopInitials

    val allShops: LiveData<List<Shop>> = liveData {
        val shops = repository.getAllShops()
        Log.d("ShopViewModel", "Retrieved shops: $shops")
        emit(shops)
    }


    fun updateShopName(name: String) {
        _shopName.value = name
    }

//    fun updateSelectedIconRef(iconRef: Int) {
//        _selectedIconRef.value = iconRef
//    }

    fun updateSelectedIconRef(iconRef: String) {
        _selectedIconResName.value = iconRef
    }

    fun updateSelectedColor(color: String) {
        _selectedColorResName.value = color
    }

    fun updateShopInitials(initials: String?) {
        _shopInitials.value = initials
    }


    fun saveShop(): Boolean {
        // Validation, ensuring all necessary details are available
        if (shopName.value.isNullOrBlank()) return false

        // Ensure the icon resource name and color resource name are set
//        val iconResName = selectedIconResName.value ?: "" // Replace with a default icon name or leave blank
        //todo below needs investigating

        val iconResName = selectedIconResName.value ?: "default_icon"
//        val iconResName = getIconResName(R.id.ib_store) ?: ""
        val colorResName = selectedColorResName.value ?: "" // Replace with a default color name or leave blank

        val shop = Shop(
            name = shopName.value!!,
            iconResName = iconResName,
            colorResName = colorResName,
            initials = shopInitials.value
        )
        Log.d("AddShopViewModel", "IconResName: ${selectedIconResName.value}, ColorResName: ${selectedColorResName.value}")

        // Save to your database using the repository
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertShop(shop)
            }
        }

        return true
    }


//    fun saveShop(): Boolean {
//        // Validation, ensuring all necessary details are available
//        if (shopName.value.isNullOrBlank()) return false
//
//        val shop = Shop(
//            name = shopName.value!!,
//            iconRef = selectedIconRef.value ?: 0, // Use 0 or a similar indicator for no icon
//            colorResId = selectedColor.value!!,
//            initials = shopInitials.value
//        )
//
//        // Save to your database using the repository
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                repository.insertShop(shop)
//            }
//        }
//
//        return true
//    }

//    fun saveShop(): Boolean {
//        // Validation, ensuring all necessary details are available
//        if (shopName.value.isNullOrBlank()) return false
//
//        val shop = Shop(
//            name = shopName.value!!,
//            iconRef = selectedIconRef.value ?: R.drawable.ic_grocery_store,
//            colorResId = selectedColor.value!!,
//            initials = shopInitials.value
//        )
//
//        // Save to your database using the repository
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                repository.insertShop(shop)
//            }
//        }
//
//        return true
//    }

}
