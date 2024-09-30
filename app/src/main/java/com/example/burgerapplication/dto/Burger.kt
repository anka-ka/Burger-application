package com.example.burgerapplication.dto

import com.google.gson.annotations.SerializedName

data class Burger(
    val id: Int,
    val name: String,
    val shortDescription: String,
    val longDescription: String,
    val price: String,
    val imageUrl: String?
)