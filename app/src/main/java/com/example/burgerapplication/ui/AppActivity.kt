package com.example.burgerapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.burgerapplication.R
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.app_actvity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController(R.id.nav_host_fragment)
    }
}