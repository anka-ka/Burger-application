package com.example.burgerapplication.repository

import android.util.Log
import com.example.burgerapplication.api.ProductApiService
import com.example.burgerapplication.dao.CartDao
import com.example.burgerapplication.dto.Cart
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.entity.CartEntity
import javax.inject.Inject

class CartRepository@Inject constructor(
    private val productApiService: ProductApiService,
    private val cartDao: CartDao
) {
    suspend fun sendCartToServer(cartItems: List<CartEntity>, token: String) {
        val cartData = cartItems.map { Cart(it.productId, it.quantity) }

        val response = productApiService.sendCart(token, cartData)
        if (response.isSuccessful) {
            val sentCart = response.body()
        } else {
            Log.e("CartRepository", "Error sending cart: ${response.errorBody()?.string()}")
        }
    }
    suspend fun saveCartLocally(product: Product, quantity: Int) {
        val cartEntity = CartEntity(productId = product.id, quantity = quantity)
        cartDao.insert(cartEntity)
    }

    suspend fun getCartFromLocal(): List<CartEntity> {
        return cartDao.getAllCartItems()
    }

    suspend fun getProductQuantityFromLocal(product: Product): Int {
        return cartDao.getQuantityByProductId(product.id) ?: 0
    }

    suspend fun removeFromCartLocal(product: Product) {
        cartDao.deleteByProductId(product.id)
    }

    suspend fun getAllCartItems(): List<CartEntity> {
        return cartDao.getAllCartItems()
    }

//    suspend fun clearLocalCart() {
//        cartDao.clearCart()
//    }
}
