package com.example.burgerapplication.repository

import androidx.appcompat.app.AppCompatDelegate
import com.example.burgerapplication.api.ProductApiService
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.error.ApiError
import com.example.burgerapplication.error.AppUnknownError
import com.example.burgerapplication.error.NetworkError
import android.content.Context
import com.example.burgerapplication.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val apiService: ProductApiService,
    @ApplicationContext
    private val context: Context,) {

    suspend fun getProducts(): List<Product> {
        try {
            val response = apiService.getProducts()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), context.getString(
                R.string.response_error
            ))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw AppUnknownError
        }
    }

    suspend fun getProductById(id: Int): Product {
        try {
            val response = apiService.getProductById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), context.getString(
                R.string.response_error
            ))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw AppUnknownError
        }
    }

    suspend fun getProductByIdInRussian(id: Int): Product {
        try {
            val response = apiService.getProductByIdInRussian(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), context.getString(
                R.string.response_error
            ))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw AppUnknownError
        }
    }

    suspend fun getProductsByType(type: String): List<Product> {
        try {
            val products = getProducts()
            return products.filter { it.type == type }
        } catch (e: Exception) {
            throw AppUnknownError
        }
    }

    suspend fun getProductsByTypeInRussian(type: String): List<Product> {
        try {
            val products = getProductsBasedOnLanguage("ru")
            return products.filter { it.type == type }
        } catch (e: Exception) {
            throw AppUnknownError
        }
    }

    suspend fun getProductsBasedOnLanguage(language: String): List<Product> {
        return try {
            if (language == "ru") {
                val response = apiService.getProductsInRussian()
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                response.body() ?: throw ApiError(response.code(), context.getString(
                    R.string.response_error
                ))
            } else {
                val response = apiService.getProducts()
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                response.body() ?: throw ApiError(response.code(), context.getString(
                    R.string.response_error
                ))
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw AppUnknownError
        }
    }

    fun updateTheme(theme: String) {
        when (theme) {
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}