package com.example.burgerapplication.api


import com.example.burgerapplication.dto.Burger
import com.example.burgerapplication.dto.Offer
import retrofit2.http.GET
import retrofit2.http.Path

interface BurgerApiService {
    @GET("api/products?lang=en")
    suspend fun getBurgers(): List<Burger>

    @GET("offers")
    suspend fun getOffers(): List<Offer>

    @GET("api/product/{id}?lang=en")
    suspend fun getBurgerById(@Path("id") id: Int): Burger
}