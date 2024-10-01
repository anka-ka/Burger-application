package com.example.burgerapplication.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.burgerapplication.R
import com.example.burgerapplication.api.ProductApiService
import com.example.burgerapplication.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment: Fragment(R.layout.settings) {

    @Inject
    lateinit var productApiService: ProductApiService

    private val productViewModel: ProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val saveButton: Button = view.findViewById(R.id.save_settings)
        val radioGroupLanguages: RadioGroup = view.findViewById(R.id.radio_group_languages)

        saveButton.setOnClickListener {
            val selectedLanguage = when (radioGroupLanguages.checkedRadioButtonId) {
                R.id.radio_russian -> "ru"
                R.id.radio_english -> "en"
                else -> {"en"
                }
            }

            productViewModel.saveLanguagePreference(selectedLanguage)

            productViewModel.loadProductsBasedOnLanguage()

            updateLocale(selectedLanguage)
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