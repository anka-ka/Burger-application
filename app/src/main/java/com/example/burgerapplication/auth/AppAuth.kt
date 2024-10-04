package com.example.burgerapplication.auth

import android.content.Context
import androidx.core.content.edit
import com.example.burgerapplication.dto.Token
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext val context: Context,
    ) {

    private val ID_KEY = "ID_KEY"
    private val TOKEN_KEY = "TOKEN_KEY"

    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val _data = MutableStateFlow<Token?>(null)
    val data: StateFlow<Token?> = _data.asStateFlow()

    init {
        val id = prefs.getString(ID_KEY, null)
        val token = prefs.getString(TOKEN_KEY, null)

        if (id != null && token != null) {
            _data.value = Token(id, token)
        } else {
            prefs.edit { clear() }
        }
    }


    @Synchronized
    fun setAuth(id: String, token: String) {
        prefs.edit()
            .putString(ID_KEY, id)
            .putString(TOKEN_KEY, token)
            .apply()
        _data.value = Token(id, token)

    }

    @Synchronized
    fun clearAuth() {
        prefs.edit()
            .clear()
            .apply()
        _data.value = null

    }

    @Synchronized
    fun isAuthenticated(): Boolean {
        return _data.value?.token != null
    }


    @Synchronized
    fun getAuthToken(): String? {
        return _data.value?.token
    }
}