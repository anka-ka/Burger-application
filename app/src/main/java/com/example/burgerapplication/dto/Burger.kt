package com.example.burgerapplication.dto

data class Burger(
    val id:Int,
    val name: String,
    val shortDescription: String,
    val price:Float,
    val imageUrl: String
)