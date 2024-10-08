package com.example.burgerapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapplication.auth.AppAuth
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

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val currentQuantity = repository.getProductQuantityFromLocal(product)

            if (currentQuantity > 0) {
                repository.saveCartLocally(product, currentQuantity + 1)
            } else {
                repository.saveCartLocally(product, 1)
            }
            Log.d("CartViewModel", "Added to cart: ${product.name}, quantity: ${currentQuantity?.plus(1) ?: 1}")
        }
    }

    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            val currentQuantity = repository.getProductQuantityFromLocal(product)
            if (currentQuantity > 1) {
                repository.saveCartLocally(product, currentQuantity - 1)
            } else {
                repository.removeFromCartLocal(product)
            }
        }
    }

    fun sendCart() {
        viewModelScope.launch {
            val localCartItems = repository.getCartFromLocal()
            val token = appAuth.getAuthToken()
            if (token != null) {
                repository.sendCartToServer(localCartItems, token)
            }
            //repository.clearLocalCart()
        }
    }
}