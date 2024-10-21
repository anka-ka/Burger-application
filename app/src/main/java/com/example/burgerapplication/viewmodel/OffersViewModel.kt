package com.example.burgerapplication.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapplication.R
import com.example.burgerapplication.auth.AppAuth
import com.example.burgerapplication.dto.Offer
import com.example.burgerapplication.error.ApiError
import com.example.burgerapplication.error.AppUnknownError
import com.example.burgerapplication.error.NetworkError
import com.example.burgerapplication.repository.OfferRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(
    private val repository: OfferRepository,
    private val appAuth: AppAuth,

) : ViewModel() {

    private val _offers = MutableLiveData<List<Offer>>()
    val offers: LiveData<List<Offer>> = _offers

    private val _authError = MutableLiveData<String>()
    val authError: LiveData<String> = _authError

    private val _errorEvent = MutableLiveData<Boolean>()
    val errorEvent: LiveData<Boolean> get() = _errorEvent

    fun loadOffersBasedOnLanguage() {
        viewModelScope.launch {
            try {
                if (appAuth.isAuthenticated()) {
                    val authToken = appAuth.getAuthToken()
                    if (authToken != null) {
                        val currentLanguage = getCurrentLanguage()
                        val offerList = if (currentLanguage == "ru") {
                            repository.getOffersInRussian(authToken)
                        } else {
                            repository.getOffers(authToken)
                        }
                        _offers.postValue(offerList)
                    } else {
                        _authError.postValue(R.string.auth_error.toString())
                    }
                } else {
                    _authError.postValue(R.string.not_auth.toString())
                }
            } catch (e: NetworkError) {
                _errorEvent.value = true
            } catch (e: ApiError) {
                _errorEvent.value = true
            } catch (e: AppUnknownError) {
                _errorEvent.value = true
            } catch (e: Exception) {
                _authError.postValue(R.string.offers_error.toString())
            }
        }
    }
    private fun getCurrentLanguage(): String {
        val prefs = appAuth.context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return prefs.getString("selected_language", "en") ?: "en"
    }
}