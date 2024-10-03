package com.example.burgerapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.burgerapplication.auth.AppAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appAuth:AppAuth,
):ViewModel() {
    val authData= appAuth
        .data.asLiveData()

    val isAuthenticated: Boolean
        get()=authData.value?.token != null
}