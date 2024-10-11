package com.example.burgerapplication.repository

import com.example.burgerapplication.api.ProductApiService
import com.example.burgerapplication.dto.Offer
import com.example.burgerapplication.error.ApiError
import com.example.burgerapplication.error.AppUnknownError
import com.example.burgerapplication.error.NetworkError
import java.io.IOException
import javax.inject.Inject

class OfferRepository @Inject constructor(
    private val productApiService: ProductApiService
) {

    suspend fun getOffers(token: String): List<Offer> {
        return try {
            val response = productApiService.getOffers("Bearer $token")
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw AppUnknownError
        }
    }

    suspend fun getOffersInRussian(token: String): List<Offer> {
        return try {
            val response = productApiService.getOffersInRussian("Bearer $token")
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw AppUnknownError
        }
    }
}