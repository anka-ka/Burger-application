package com.example.burgerapplication.repository

import com.example.burgerapplication.api.ProductApiService
import com.example.burgerapplication.dto.Offer
import javax.inject.Inject

class OfferRepository @Inject constructor(private val apiService: ProductApiService) {
    suspend fun getOffers(): List<Offer> {
        return apiService.getOffers()
    }
}