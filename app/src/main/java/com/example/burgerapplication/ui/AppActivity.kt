package com.example.burgerapplication.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.burgerapplication.R
import com.example.burgerapplication.repository.ProductRepository
import com.example.burgerapplication.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale



@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.app_actvity) {

    private val productViewModel: ProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(productViewModel.getSavedLanguage())

        val navController = findNavController(R.id.nav_host_fragment)

        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val selectedTheme = sharedPreferences.getString("selected_theme", "light")

        when (selectedTheme) {
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun applyLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }


}