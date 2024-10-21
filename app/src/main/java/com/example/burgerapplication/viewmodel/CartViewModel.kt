package com.example.burgerapplication.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapplication.auth.AppAuth
import com.example.burgerapplication.dto.CartResponse
import com.example.burgerapplication.dto.OrderResponse
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.error.ApiError
import com.example.burgerapplication.error.AppUnknownError
import com.example.burgerapplication.error.NetworkError
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

    private val _errorEvent = MutableLiveData<Boolean>()
    val errorEvent: LiveData<Boolean> get() = _errorEvent

    private val _orderResponse = MutableLiveData<OrderResponse>()
    val orderResponse: LiveData<OrderResponse> = _orderResponse

    fun sendOrder(paymentMethod: String) {
        viewModelScope.launch {
            try {
                val token = appAuth.getAuthToken()
                val response = repository.sendOrder(paymentMethod, token)
                _orderResponse.postValue(response)
            } catch (e: Exception) {
                _orderResponse.postValue(OrderResponse(success = false, points = 0.0))
            }
        }
    }


    fun addToCart(product: Product) {
        viewModelScope.launch {
            try {
                repository.saveCartLocally(product, 1)
                getProductQuantity(product.id)
            } catch (e: NetworkError) {
                _errorEvent.value = true
            } catch (e: ApiError) {
                _errorEvent.value = true
            } catch (e: AppUnknownError) {
                _errorEvent.value = true
            }
        }
    }

    fun updateCartQuantity(product: Product, quantity: Int) {
        viewModelScope.launch {
            try {
                repository.saveQuantityLocally(product, quantity)
                getProductQuantity(product.id)
                updateCartData()
            } catch (e: NetworkError) {
                _errorEvent.value = true
            } catch (e: ApiError) {
                _errorEvent.value = true
            } catch (e: AppUnknownError) {
                _errorEvent.value = true
            }
        }
    }

    fun updateCartData() {
        viewModelScope.launch {
            val localCartItems = repository.getCartFromLocal()
            val token = appAuth.getAuthToken()
            try {
                    val currentLanguage = getCurrentLanguage()
                    val cartResponse = if (currentLanguage == "ru") {
                        repository.sendCartToServerInRussian(localCartItems, token)
                    } else {
                        repository.sendCartToServer(localCartItems, token)
                    }
                    if (cartResponse != null) {
                        _finalPrice.value = cartResponse.finalPrice
                        _points.value = cartResponse.points.toInt()
                        _cartResponse.value = cartResponse
                    }
            } catch (e: NetworkError) {
                _errorEvent.value = true
            } catch (e: ApiError) {
                _errorEvent.value = true
            } catch (e: AppUnknownError) {
                _errorEvent.value = true
            } finally {
                _isLoading.value = false

            }
        }
    }

    private fun getCurrentLanguage(): String {
        val prefs = appAuth.context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return prefs.getString("selected_language", "en") ?: "en"
    }

    suspend fun clearCart(){
        try{
        repository.clearLocalCart()
        } catch (e: NetworkError) {
            _errorEvent.value = true
        } catch (e: ApiError) {
            _errorEvent.value = true
        } catch (e: AppUnknownError) {
            _errorEvent.value = true
        }
    }

    fun getProductQuantity(productId: Int) {
        viewModelScope.launch {
            try{
            val quantity = repository.getProductQuantityById(productId) ?: 0
            val currentQuantities = _productQuantities.value ?: emptyMap()
            _productQuantities.value = currentQuantities + (productId to quantity)
            } catch (e: NetworkError) {
                _errorEvent.value = true
            } catch (e: ApiError) {
                _errorEvent.value = true
            } catch (e: AppUnknownError) {
                _errorEvent.value = true
            }
        }
    }
}

