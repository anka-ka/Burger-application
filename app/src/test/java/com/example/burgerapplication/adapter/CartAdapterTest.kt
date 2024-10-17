package com.example.burgerapplication.adapter

import androidx.lifecycle.LifecycleOwner
import com.example.burgerapplication.dto.Cart
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.viewmodel.CartViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.Test
import org.mockito.Mockito

class CartAdapterTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testScope = TestCoroutineScope()
    private val lifecycleOwner: LifecycleOwner = Mockito.mock(LifecycleOwner::class.java)
    private val cartViewModel: CartViewModel = Mockito.mock(CartViewModel::class.java)
    private val cart: Cart = Mockito.mock(Cart::class.java)
    private val items: List<Product> = listOf()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun calculateTotalPriceFor100() {
        val price = "100"
        val quantity = 2
        val adapter = CartAdapter(items, cartViewModel, cart, lifecycleOwner, testScope)
        val result = adapter.calculateTotalPrice(price, quantity)

        assertEquals(200.0, result)
    }
}