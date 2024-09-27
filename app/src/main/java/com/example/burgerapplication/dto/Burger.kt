package com.example.burgerapplication.dto

import com.google.gson.annotations.SerializedName

data class Burger(
    val id: Int,
    val name: String,
    @SerializedName("short_description") val shortDescription: String,
    @SerializedName("long_description") val longDescription: String,
    val price: String,
    val imageUrl: String?
)