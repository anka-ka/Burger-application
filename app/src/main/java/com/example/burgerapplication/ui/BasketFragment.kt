package com.example.burgerapplication.ui


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import com.example.burgerapplication.R
import com.example.burgerapplication.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BasketFragment : Fragment(R.layout.basket_fragment) {

    private val cartViewModel: CartViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cartViewModel.viewModelScope.launch {
            val items = cartViewModel.repository.getAllCartItems()
            Log.d("BasketFragment", "Current cart items: $items")
        }
    }

}