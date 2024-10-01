package com.example.burgerapplication.dto


data class Product(
    val id: Int,
    val name: String,
    val shortDescription: String,
    val longDescription: String,
    val type:String,
    val price: String,
    val imageUrl: String?
)