package com.example.burgerapplication.dto

data class OrderRequest(
    val paymentMethod: String,
    val basket: List<Cart>
)