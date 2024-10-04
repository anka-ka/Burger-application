package com.example.burgerapplication.repository

import android.util.Log
import com.example.burgerapplication.api.ProductApiService
import com.example.burgerapplication.dto.Offer
import retrofit2.HttpException
import javax.inject.Inject

class OfferRepository @Inject constructor(
    private val productApiService: ProductApiService) {

    suspend fun getOffers(token: String): List<Offer> {
        val response = productApiService.getOffers("Bearer $token")
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw HttpException(response)
        }
    }
}