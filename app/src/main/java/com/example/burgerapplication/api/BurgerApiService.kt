package com.example.burgerapplication.api

import com.example.burgerapplication.dto.Burger
import com.example.burgerapplication.dto.Offer
import retrofit2.http.GET

interface BurgerApiService {
    @GET("api/products?lang=en")
    suspend fun getBurgers(): List<Burger>

    @GET("offers")
    suspend fun getOffers(): List<Offer>
}