package com.example.burgerapplication.error

import android.database.SQLException
import java.io.IOException

sealed class AppError(var code: String) : RuntimeException() {
    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is SQLException -> DbError
            is IOException -> NetworkError
            else -> AppUnknownError
        }
    }
}

class ApiError(val status: Int, code: String) : AppError(code)
object NetworkError : AppError("error_network")
object DbError : AppError("error_db")
object AppUnknownError : AppError("error_unknown")