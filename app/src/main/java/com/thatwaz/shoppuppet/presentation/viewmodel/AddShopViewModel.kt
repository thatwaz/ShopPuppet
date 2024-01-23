package com.thatwaz.shoppuppet.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.thatwaz.shoppuppet.data.repository.ShopRepository
import com.thatwaz.shoppuppet.domain.model.Shop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * ViewModel for AddShopFragment. Manages shop details including name, icon, color, and initials.
 * Provides LiveData to track the status of shop save operations and any errors encountered.
 */
@HiltViewModel
class AddShopViewModel @Inject constructor(
    application: Application,
    private val repository: ShopRepository
) : AndroidViewModel(application) {

    // LiveData for shop details
    private val _shopName = MutableLiveData<String?>()
    val shopName: LiveData<String?> get() = _shopName

    private val _selectedIconResName = MutableLiveData<String?>()
    private val selectedIconResName: LiveData<String?> get() = _selectedIconResName

    private val _selectedColorResName = MutableLiveData<String?>()
    private val selectedColorResName: LiveData<String?> get() = _selectedColorResName

    private val _shopInitials = MutableLiveData<String?>()
    private val shopInitials: LiveData<String?> get() = _shopInitials

    // LiveData to indicate the status of save operation
    private val _saveStatus = MutableLiveData<Result<Unit>>()
    val saveStatus: LiveData<Result<Unit>> = _saveStatus

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun updateShopName(name: String) {
        _shopName.value = name
    }

    fun updateSelectedIconRef(iconRef: String) {
        _selectedIconResName.value = iconRef
    }

    fun updateSelectedColor(color: String) {
        _selectedColorResName.value = color
    }

    fun updateShopInitials(initials: String?) {
        _shopInitials.value = initials
    }

    fun saveShop() {

        val iconResName = selectedIconResName.value ?: "default_icon"
        val colorResName = selectedColorResName.value ?: ""

        val shop = Shop(
            name = shopName.value!!,
            iconResName = iconResName,
            colorResName = colorResName,
            initials = shopInitials.value
        )

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.insertShop(shop)
                }
                _saveStatus.postValue(Result.success(Unit))
            } catch (e: Exception) {
                _error.postValue("Failed to save shop: ${e.message}")
                _saveStatus.postValue(Result.failure(e))
            }
        }
    }

}
