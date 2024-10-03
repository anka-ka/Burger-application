package com.example.burgerapplication.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.burgerapplication.R
import com.example.burgerapplication.auth.AppAuth
import com.example.burgerapplication.repository.ProductRepository
import com.example.burgerapplication.viewmodel.AuthViewModel
import com.example.burgerapplication.viewmodel.ProductViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.app_actvity) {

    @Inject
    lateinit var appAuth: AppAuth

    private val productViewModel: ProductViewModel by viewModels()

    private val authViewModel: AuthViewModel by viewModels()

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

        addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.auth_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return handleMenuItemSelected(menuItem)
                }
            }
        )

        productViewModel.menuButtonClickEvent.observe(this) {
            showPopupMenu(findViewById(R.id.menu))
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.auth_menu)

        authViewModel.authData.observe(this) {
            val isAuthenticated = authViewModel.isAuthenticated
            popupMenu.menu.setGroupVisible(R.id.authenticated, !isAuthenticated)
            popupMenu.menu.setGroupVisible(R.id.unauthenticated, isAuthenticated)
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
            handleMenuItemSelected(menuItem)
        }
        popupMenu.show()
    }

    private fun handleMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.sign_in -> {
                findNavController(R.id.nav_host_fragment)
                    .navigate(R.id.action_appActivity_to_loginAndPasswordFragment)
                appAuth.setAuth("9505b0f9-544a-4a1c-9383-f545730013ab", "token")
                true
            }
            R.id.sign_up -> {
                true
            }
            R.id.logout -> {
                appAuth.clearAuth()
                true
            }
            else -> false
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