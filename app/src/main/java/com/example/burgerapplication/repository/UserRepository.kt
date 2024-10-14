package com.example.burgerapplication.repository

import com.example.burgerapplication.api.ProductApiService
import com.example.burgerapplication.auth.LoginRequest
import com.example.burgerapplication.dto.Token
import com.example.burgerapplication.dto.User
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor (private val apiService: ProductApiService) {

    suspend fun authenticate(login: String, password: String): Response<Token> {
        val credentials = LoginRequest(username = login, password = password)
        return apiService.authenticate(credentials)

    }

    suspend fun getUserData (login: String, password: String): Response<User>{
        val credentials = LoginRequest(username = login, password = password)
        return apiService.getUserData(credentials)

    }
}