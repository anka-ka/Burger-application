package com.example.burgerapplication.repository

import android.util.Log
import com.example.burgerapplication.R
import com.example.burgerapplication.api.ProductApiService
import com.example.burgerapplication.dao.CartDao
import com.example.burgerapplication.dto.Cart
import com.example.burgerapplication.dto.CartResponse
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.entity.CartEntity
import com.example.burgerapplication.error.ApiError
import com.example.burgerapplication.error.AppUnknownError
import com.example.burgerapplication.error.NetworkError
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import com.example.burgerapplication.dto.OrderRequest
import com.example.burgerapplication.dto.OrderResponse
import java.io.IOException
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val productApiService: ProductApiService,
    private val cartDao: CartDao,
    @ApplicationContext
    private val context: Context
) {
    suspend fun sendCartToServer(cartItems: List<CartEntity>, token: String?): CartResponse {
        val cartData = cartItems.map { Cart(it.productId, it.quantity) }

        return try {
            val response = if (token.isNullOrBlank()) {
                productApiService.sendCartWithoutToken(cartData)
            } else {
                productApiService.sendCart("Bearer $token", cartData)
            }

            if (response.isSuccessful) {
                val cartResponse = response.body()
                if (cartResponse != null) {
                    Log.d("CartRepository", "Final Price: ${cartResponse.finalPrice}")
                    Log.d("CartRepository", "Points: ${cartResponse.points}")
                    Log.d("CartRepository", "Products: ${cartResponse.products}")
                    cartResponse
                } else {
                    throw ApiError(response.code(), context.getString(R.string.response_error))
                }
            } else {
                Log.e("CartRepository", "Error sending cart: ${response.errorBody()?.string()}")
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw AppUnknownError
        }
    }

    suspend fun sendCartToServerInRussian(
        cartItems: List<CartEntity>,
        token: String?
    ): CartResponse {
        val cartData = cartItems.map { Cart(it.productId, it.quantity) }

        return try {
            val response = if (token.isNullOrBlank()) {
                productApiService.sendCartWithoutTokenInRussian(cartData)
            } else {
                productApiService.sendCartInRussian("Bearer $token", cartData)
            }

            if (response.isSuccessful) {
                val cartResponse = response.body()
                if (cartResponse != null) {
                    Log.d("CartRepository", "Final Price: ${cartResponse.finalPrice}")
                    Log.d("CartRepository", "Points: ${cartResponse.points}")
                    Log.d("CartRepository", "Products: ${cartResponse.products}")
                    cartResponse
                } else {
                    throw ApiError(response.code(), context.getString(R.string.response_error))
                }
            } else {
                Log.e("CartRepository", "Error sending cart: ${response.errorBody()?.string()}")
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw AppUnknownError
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
        val currentQuantity = cartDao.getQuantityByProductId(product.id) ?: 0
        if (currentQuantity < 1) {
            cartDao.deleteByProductId(product.id)
        }
    }


    suspend fun getCartFromLocal(): List<CartEntity> {
        return cartDao.getAllCartItems()
    }

    suspend fun getProductQuantityById(productId: Int): Int? {
        return cartDao.getQuantityByProductId(productId)
    }

    suspend fun clearLocalCart() {
        cartDao.clearCart()
    }

    suspend fun sendOrder(paymentMethod: String, token: String?): OrderResponse {
        val cartItems = getCartFromLocal()
        return try {

            val cartRequestItems = cartItems.map { Cart(it.productId, it.quantity) }
            val orderRequest = OrderRequest(paymentMethod, cartRequestItems)

            val cartResponse = if (token.isNullOrBlank()) {
                val response = productApiService.sendOrderWithoutToken(orderRequest)
                if (response.isSuccessful) {
                    response.body() ?: OrderResponse(success = false, points = 0.0)
                } else {
                    OrderResponse(success = false, points = 0.0)
                }
            } else {
                val response = productApiService.sendOrder("Bearer $token", orderRequest)
                if (response.isSuccessful) {
                    response.body() ?: OrderResponse(success = false, points = 0.0)
                } else {
                    OrderResponse(success = false, points = 0.0)
                }
            }

            cartResponse
        } catch (e: Exception) {
            OrderResponse(success = false, points = 0.0)
        }
    }
}