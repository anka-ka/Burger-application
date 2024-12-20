package com.example.burgerapplication.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapplication.auth.AppAuth
import com.example.burgerapplication.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appAuth: AppAuth
) : ViewModel() {
    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean> get() = _isAuthenticated

    var token: String? = null

    fun login(login: String, password: String) {
        viewModelScope.launch {
            val response = userRepository.authenticate(login, password)
            if (response.isSuccessful && response.body()?.token != null) {
                _isAuthenticated.value = true
                token = response.body()!!.token
                appAuth.setAuth(response.body()!!.id, token!!)

                loadUserData()
            } else {
                _isAuthenticated.value = false
            }
        }
    }

    suspend fun loadUserData() {
        val currentToken = appAuth.getAuthToken()
        if (currentToken != null) {
            val userResponse =
                userRepository.getUserData(currentToken)
            if (userResponse.isSuccessful && userResponse.body() != null) {
                appAuth.setUser(userResponse.body()!!)
            }
        }
    }
}