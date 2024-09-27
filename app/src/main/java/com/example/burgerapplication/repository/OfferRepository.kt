package com.example.burgerapplication.repository

import com.example.burgerapplication.api.BurgerApiService
import com.example.burgerapplication.dto.Offer
import javax.inject.Inject

class OfferRepository @Inject constructor(private val apiService: BurgerApiService) {
    suspend fun getOffers(): List<Offer> {
        return apiService.getOffers()
    }
}