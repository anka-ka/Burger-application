package com.example.burgerapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.burgerapplication.dto.Burger
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BurgerViewModel : ViewModel() {
    fun loadBurgers(context: Context): List<Burger> {
        val jsonFile = "burgers.json"
        val inputStream = context.assets.open(jsonFile)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonString, object : TypeToken<List<Burger>>() {}.type)
    }
}