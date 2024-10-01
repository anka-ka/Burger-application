package com.example.burgerapplication.api


import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.dto.Offer
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("api/products?lang=en")
    suspend fun getProducts(): List<Product>

    @GET("api/products?lang=ru")
    suspend fun getProductsInRussian(): List<Product>

    @GET("offers")
    suspend fun getOffers(): List<Offer>

    @GET("api/product/{id}?lang=en")
    suspend fun getProductById(@Path("id") id: Int): Product

    @GET("api/product/{id}?lang=ru")
    suspend fun getProductByIdInRussian(@Path("id") id: Int): Product
}