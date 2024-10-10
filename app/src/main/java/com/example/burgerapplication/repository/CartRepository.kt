package com.example.burgerapplication.repository

import android.util.Log
import com.example.burgerapplication.api.ProductApiService
import com.example.burgerapplication.dao.CartDao
import com.example.burgerapplication.dto.Cart
import com.example.burgerapplication.dto.CartResponse
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.entity.CartEntity
import javax.inject.Inject

class CartRepository@Inject constructor(
    private val productApiService: ProductApiService,
    private val cartDao: CartDao
) {
    suspend fun sendCartToServer(cartItems: List<CartEntity>, token: String): CartResponse? {
        val cartData = cartItems.map { Cart(it.productId, it.quantity) }

        val response = productApiService.sendCart("Bearer $token", cartData)
        return if (response.isSuccessful) {
            val cartResponse = response.body()
            if (cartResponse != null) {
                Log.d("CartRepository", "Final Price: ${cartResponse.finalPrice}")
                Log.d("CartRepository", "Points: ${cartResponse.points}")
                Log.d("CartRepository", "Products: ${cartResponse.products}")
            }
            cartResponse
        } else {
            Log.e("CartRepository", "Error sending cart: ${response.errorBody()?.string()}")
            null
        }
    }

    suspend fun sendCartToServerInRussian(cartItems: List<CartEntity>, token: String): CartResponse? {
        val cartData = cartItems.map { Cart(it.productId, it.quantity) }

        val response = productApiService.sendCartInRussian("Bearer $token", cartData)
        return if (response.isSuccessful) {
            val cartResponse = response.body()
            if (cartResponse != null) {
                Log.d("CartRepository", "Final Price: ${cartResponse.finalPrice}")
                Log.d("CartRepository", "Points: ${cartResponse.points}")
                Log.d("CartRepository", "Products: ${cartResponse.products}")
            }
            cartResponse
        } else {
            Log.e("CartRepository", "Error sending cart: ${response.errorBody()?.string()}")
            null
        }
    }

    suspend fun saveCartLocally(product: Product, quantity: Int) {
        val existingQuantity = cartDao.getQuantityByProductId(product.id) ?: 0
        val newQuantity = existingQuantity + quantity
        val cartEntity = CartEntity(productId = product.id, quantity = newQuantity)
        cartDao.insert(cartEntity)
    }

    suspend fun saveQuantityLocally(product: Product, quantity: Int) {
        val cartEntity = CartEntity(productId = product.id, quantity = quantity)
        cartDao.insert(cartEntity)
    }


    suspend fun getCartFromLocal(): List<CartEntity> {
        return cartDao.getAllCartItems()
    }

    suspend fun getProductQuantityFromLocal(product: Product): Int {
        return cartDao.getQuantityByProductId(product.id) ?: 0
    }
    suspend fun getProductQuantityById(productId: Int): Int? {
        return cartDao.getQuantityByProductId(productId)
    }

    suspend fun removeFromCartLocal(product: Product) {
        val currentQuantity = cartDao.getQuantityByProductId(product.id) ?: 0
        if (currentQuantity > 1) {
            val newQuantity = currentQuantity - 1
            cartDao.insert(CartEntity(productId = product.id, quantity = newQuantity))
        } else {
            cartDao.deleteByProductId(product.id)
        }
    }

    suspend fun getAllCartItems(): List<CartEntity> {
        return cartDao.getAllCartItems()
    }

     suspend fun clearLocalCart() {
        cartDao.clearCart()
    }
}
