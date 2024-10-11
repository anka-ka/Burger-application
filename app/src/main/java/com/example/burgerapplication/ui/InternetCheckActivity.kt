package com.example.burgerapplication.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.burgerapplication.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InternetCheckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (isInternetAvailable()) {
            val intent = Intent(this, AppActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.internet_check)
            val retryButton: Button = findViewById(R.id.retry)
            retryButton.setOnClickListener {
                if (isInternetAvailable()) {
                    val intent = Intent(this, AppActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, R.string.still_no_internet, Toast.LENGTH_SHORT).show()
                }

                val closeButton: Button = findViewById(R.id.close)
                closeButton.setOnClickListener {
                    finishAffinity()
                }
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}