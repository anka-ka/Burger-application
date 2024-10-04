package com.example.burgerapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapplication.auth.AppAuth
import com.example.burgerapplication.dto.Offer
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

    fun loadOffers() {
        viewModelScope.launch {
            if (appAuth.isAuthenticated()) {
                try {
                    val authToken = appAuth.getAuthToken()
                    if (authToken != null) {
                        val offerList = repository.getOffers(authToken)
                        _offers.postValue(offerList)
                    } else {
                        _authError.postValue("Ошибка авторизации. Попробуйте снова.")
                    }
                } catch (e: Exception) {
                    _authError.postValue("Ошибка авторизации. Попробуйте снова.")
                }
            } else {
                _authError.postValue("Вы не авторизованы.")
            }
        }
    }
}