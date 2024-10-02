package com.example.burgerapplication.repository

import com.example.burgerapplication.api.ProductApiService
import com.example.burgerapplication.dto.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(private val apiService: ProductApiService) {

    suspend fun getProducts(): List<Product> {
        return apiService.getProducts()
    }

    suspend fun getProductByIdInRussian(id: Int): Product {
        return apiService.getProductByIdInRussian(id)
    }

    suspend fun getProductById(id: Int): Product {
        return apiService.getProductById(id)
    }

    suspend fun getProductsByType(type: String): List<Product> {
        return apiService.getProducts().filter { it.type == type }
    }

    suspend fun getProductsByTypeInRussian(type: String): List<Product> {
        return apiService.getProductsInRussian().filter { it.type == type }
    }

    suspend fun getProductsBasedOnLanguage(language: String): List<Product> {
        return if (language == "ru") {
            apiService.getProductsInRussian()
        } else {
            apiService.getProducts()
        }
    }
}