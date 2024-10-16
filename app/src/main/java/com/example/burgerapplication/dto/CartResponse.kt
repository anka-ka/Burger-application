package com.example.burgerapplication.dto

data class CartResponse(
    val finalPrice: Double,
    val points: Double,
    val products: List<Product>
)