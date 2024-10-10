package com.example.burgerapplication.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapplication.auth.AppAuth
import com.example.burgerapplication.dto.CartResponse
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    val repository: CartRepository,
    val appAuth: AppAuth
) : ViewModel() {

    private val _finalPrice = MutableLiveData<Double>()
    val finalPrice: LiveData<Double> = _finalPrice

    private val _points = MutableLiveData<Int>()
    val points: LiveData<Int> = _points

    private val _cartResponse = MutableLiveData<CartResponse?>()
    val cartResponse: LiveData<CartResponse?> get() = _cartResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _productQuantities = MutableLiveData<Map<Int, Int>>()
    val productQuantities: LiveData<Map<Int, Int>> get() = _productQuantities

    fun addToCart(product: Product) {
        viewModelScope.launch {
            repository.saveCartLocally(product, 1)
            Log.d("CartViewModel", "Added to cart: ${product.name}")
            getProductQuantity(product.id)

        }
        updateCartData()
    }

    fun updateCartQuantity (product: Product, quantity : Int) {
        viewModelScope.launch {
            repository.saveQuantityLocally(product,quantity )
            getProductQuantity(product.id)

        }
        updateCartData()
    }


//    fun removeFromCart(product: Product) {
//        viewModelScope.launch {
//            val currentQuantity = repository.getProductQuantityFromLocal(product)
//            if (currentQuantity > 1) {
//                repository.saveCartLocally(product, -1)
//            } else {
//                repository.removeFromCartLocal(product)
//            }
//            getProductQuantity(product.id)
//            updateCartData()
//        }
//    }


    fun updateCartData() {
        viewModelScope.launch {
            val localCartItems = repository.getCartFromLocal()
            val token = appAuth.getAuthToken()
            try {
                if (token != null) {
                    val currentLanguage = getCurrentLanguage()
                    val cartResponse = if (currentLanguage == "ru") {
                        repository.sendCartToServerInRussian(localCartItems, token)
                    }else{ repository.sendCartToServer(localCartItems, token)
                    }
                    if (cartResponse != null) {
                        _finalPrice.value = cartResponse.finalPrice
                        _points.value = cartResponse.points
                        _cartResponse.value = cartResponse
                    }
                }
            } finally { _isLoading.value= false

            }
        }
    }

    private fun getCurrentLanguage(): String {
        val prefs = appAuth.context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return prefs.getString("selected_language", "en") ?: "en"
    }

    suspend fun clearCart(){
        repository.clearLocalCart()
    }

    fun getProductQuantity(productId: Int) {
        viewModelScope.launch {
            val quantity = repository.getProductQuantityById(productId) ?: 0
            val currentQuantities = _productQuantities.value ?: emptyMap()
            _productQuantities.value = currentQuantities + (productId to quantity)
        }
    }
}

