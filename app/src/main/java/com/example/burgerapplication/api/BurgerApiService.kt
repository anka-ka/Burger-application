package com.example.burgerapplication.api

import com.example.burgerapplication.dto.Burger
import retrofit2.http.GET

interface BurgerApiService {
    @GET("path/to/burgers")
    suspend fun getBurgers(): List<Burger>
}