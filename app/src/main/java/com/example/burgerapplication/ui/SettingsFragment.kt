package com.example.burgerapplication.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.burgerapplication.R
import com.example.burgerapplication.api.ProductApiService
import com.example.burgerapplication.viewmodel.OfferViewModel
import com.example.burgerapplication.viewmodel.ProductViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment: Fragment(R.layout.settings) {

    @Inject
    lateinit var productApiService: ProductApiService

    private val productViewModel: ProductViewModel by viewModels()

    private val offerViewModel:OfferViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val saveButton: Button = view.findViewById(R.id.save_settings)
        val radioGroupLanguages: RadioGroup = view.findViewById(R.id.radio_group_languages)
        val radioGroupThemes: RadioGroup = view.findViewById(R.id.radio_group_themes)

        view.findViewById<MaterialButton>(R.id.backMenu).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_menuFragment)
        }

        saveButton.setOnClickListener {
            val selectedLanguage = when (radioGroupLanguages.checkedRadioButtonId) {
                R.id.radio_russian -> "ru"
                R.id.radio_english -> "en"
                else -> {
                    "en"
                }
            }
            val selectedTheme = when (radioGroupThemes.checkedRadioButtonId) {
                R.id.radio_dark -> "dark"
                R.id.radio_light -> "light"
                else -> {"dark"
                }
            }
            productViewModel.saveThemePreference(selectedTheme)

            productViewModel.saveLanguagePreference(selectedLanguage)

            productViewModel.loadProductsBasedOnLanguage()

            offerViewModel.loadOffersBasedOnLanguage()

            updateLocale(selectedLanguage)
            productViewModel.updateTheme(selectedTheme)
        }
    }

    private fun updateLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = requireContext().resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        requireActivity().recreate()
    }
}