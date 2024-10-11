package com.example.burgerapplication.api


import com.example.burgerapplication.auth.LoginRequest
import com.example.burgerapplication.dto.Cart
import com.example.burgerapplication.dto.CartResponse
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.dto.Offer
import com.example.burgerapplication.dto.Token
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductApiService {
    @GET("api/products?lang=en")
    suspend fun getProducts(): Response<List<Product>>

    @GET("api/products?lang=ru")
    suspend fun getProductsInRussian(): Response<List<Product>>

    @GET("api/product/{id}?lang=en")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>

    @GET("api/product/{id}?lang=ru")
    suspend fun getProductByIdInRussian(@Path("id") id: Int): Response<Product>

    @POST("api/user/login")
    suspend fun authenticate(
        @Body credentials: LoginRequest
    ): Response<Token>

    @GET("api/notifications?lang=en")
    suspend fun getOffers(
        @Header("Authorization") token: String
    ): Response<List<Offer>>

    @GET("api/notifications?lang=ru")
    suspend fun getOffersInRussian(
        @Header("Authorization") token: String
    ): Response<List<Offer>>

    @POST("api/basket/calculate?lang=en")
    suspend fun sendCart(
        @Header("Authorization") token: String?,
        @Body productCartRequest: List<Cart>
    ): Response<CartResponse>

    @POST("api/basket/calculate?lang=en")
    suspend fun sendCartWithoutToken(
        @Body productCartRequest: List<Cart>
    ): Response<CartResponse>

    @POST("api/basket/calculate?lang=ru")
    suspend fun sendCartInRussian(
        @Header("Authorization") token: String?,
        @Body productCartRequest: List<Cart>
    ): Response<CartResponse>

    @POST("api/basket/calculate?lang=ry")
    suspend fun sendCartWithoutTokenInRussian(
        @Body productCartRequest: List<Cart>
    ): Response<CartResponse>
}