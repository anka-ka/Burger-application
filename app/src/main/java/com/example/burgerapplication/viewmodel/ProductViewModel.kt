package com.example.burgerapplication.viewmodel


import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.error.ApiError
import com.example.burgerapplication.error.AppUnknownError
import com.example.burgerapplication.error.NetworkError
import com.example.burgerapplication.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val context: Application) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val burgers: LiveData<List<Product>> get() = _products

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product> get() = _product

    private val _menuButtonClickEvent = MutableLiveData<Unit>()
    val menuButtonClickEvent: LiveData<Unit> get() = _menuButtonClickEvent

    private val _errorEvent = MutableLiveData<Boolean>()
    val errorEvent: LiveData<Boolean> get() = _errorEvent

    fun loadProducts() {
        viewModelScope.launch {
            val productList = repository.getProducts()
            _products.value = productList
        }
    }

    fun loadProductById(id: Int) {
        viewModelScope.launch {
            try {
            val language = getSavedLanguage()
            val product = if (language == "ru") {
                repository.getProductByIdInRussian(id)
            } else {
                repository.getProductById(id)
            }
            _product.value = product
            } catch (e: NetworkError) {
                _errorEvent.value = true
            } catch (e: ApiError) {
                _errorEvent.value = true
            } catch (e: AppUnknownError) {
                _errorEvent.value = true
            }
        }
    }

    fun loadProductsByType(type: String) {
        viewModelScope.launch {
            try {
            val language = getSavedLanguage()
            val productList = if (language == "ru") {
                repository.getProductsByTypeInRussian(type)
            } else {
                repository.getProductsByType(type)
            }
            _products.value = productList
            } catch (e: NetworkError) {
                _errorEvent.value = true
            } catch (e: ApiError) {
                _errorEvent.value = true
            } catch (e: AppUnknownError) {
                _errorEvent.value = true
            }
        }
    }

    fun saveLanguagePreference(language: String) {
        val sharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("selected_language", language).apply()
    }

    fun getSavedLanguage(): String {
        val sharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return sharedPreferences.getString("selected_language", "en") ?: "en"
    }

    fun saveThemePreference(theme: String) {
        val sharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("selected_theme", theme).apply()
    }


    fun updateTheme(theme: String) {
        repository.updateTheme(theme)
    }


    fun loadProductsBasedOnLanguage() {
        viewModelScope.launch {
            try{
            val language = getSavedLanguage()
            val productList = repository.getProductsBasedOnLanguage(language)
            _products.value = productList
            } catch (e: NetworkError) {
                _errorEvent.value = true
            } catch (e: ApiError) {
                _errorEvent.value = true
            } catch (e: AppUnknownError) {
                _errorEvent.value = true
            }
        }
    }

    fun onMenuButtonClick() {
        _menuButtonClickEvent.value = Unit
    }
}