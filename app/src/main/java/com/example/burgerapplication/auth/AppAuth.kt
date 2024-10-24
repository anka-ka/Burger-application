package com.example.burgerapplication.auth

import android.content.Context
import androidx.core.content.edit
import com.example.burgerapplication.dto.Token
import com.example.burgerapplication.dto.User
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
    private val USERNAME_KEY = "USERNAME_KEY"
    private val FIRST_NAME_KEY = "FIRST_NAME_KEY"
    private val LAST_NAME_KEY = "LAST_NAME_KEY"
    private val POINTS_KEY = "POINTS_KEY"
    private val LAST_AUTH_TIME_KEY = "LAST_AUTH_TIME_KEY"

    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val _data = MutableStateFlow<Token?>(null)
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    val data: StateFlow<Token?> = _data.asStateFlow()

    init {
        checkAuthValidity()
        val id = prefs.getString(ID_KEY, null)
        val token = prefs.getString(TOKEN_KEY, null)
        val username = prefs.getString(USERNAME_KEY, null)
        val firstName = prefs.getString(FIRST_NAME_KEY, null)
        val lastName = prefs.getString(LAST_NAME_KEY, null)
        val points = java.lang.Double.longBitsToDouble(prefs.getLong(POINTS_KEY, java.lang.Double.doubleToRawLongBits(0.0)))

        if (id != null && token != null) {
            _data.value = Token(id, token)
            if (username != null && firstName != null && lastName != null) {
                _user.value = User(username, firstName, lastName, points)
            }
        } else {
            prefs.edit { clear() }
        }
    }

    @Synchronized
    fun setAuth(id: String, token: String) {
        val currentTime = System.currentTimeMillis()
        prefs.edit()
            .putString(ID_KEY, id)
            .putString(TOKEN_KEY, token)
            .putLong(LAST_AUTH_TIME_KEY, currentTime)
            .apply()
        _data.value = Token(id, token)
    }

    @Synchronized
    fun clearAuth() {
        prefs.edit()
            .clear()
            .apply()
        _data.value = null
        _user.value = null
    }

    @Synchronized
    fun isAuthenticated(): Boolean {
        return _data.value?.token != null
    }

    @Synchronized
    fun getAuthToken(): String? {
        return _data.value?.token
    }

    @Synchronized
    fun setUser(user: User) {
        prefs.edit()
            .putString(USERNAME_KEY, user.username)
            .putString(FIRST_NAME_KEY, user.firstName)
            .putString(LAST_NAME_KEY, user.lastName)
            .putLong(POINTS_KEY, java.lang.Double.doubleToRawLongBits(user.points))
            .apply()
        _user.value = user
    }

    private fun isTokenExpired(): Boolean {
        val lastAuthTime = prefs.getLong(LAST_AUTH_TIME_KEY, 0L)
        val currentTime = System.currentTimeMillis()
        val expirationTime = 24 * 60 * 60 * 1000
        return (currentTime - lastAuthTime) > expirationTime
    }

    private fun checkAuthValidity() {
        if (isTokenExpired()) {
            clearAuth()
        }
    }
}