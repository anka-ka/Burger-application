package com.example.burgerapplication.repository

import com.example.burgerapplication.api.BurgerApiService
import com.example.burgerapplication.dto.Burger
import javax.inject.Inject

class BurgerRepository @Inject constructor(private val apiService: BurgerApiService) {

    suspend fun getBurgers(): List<Burger> {
        return apiService.getBurgers()
    }
}