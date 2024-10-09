package com.example.burgerapplication.dto

data class CartResponse(
    val finalPrice: Double,
    val points: Int,
    val products: List<Product>
)