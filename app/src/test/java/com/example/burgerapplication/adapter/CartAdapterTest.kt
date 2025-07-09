package com.example.burgerapplication.adapter

import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.example.burgerapplication.dto.Cart
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.viewmodel.CartViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class CartAdapterTest {

    private val lifecycleOwner: LifecycleOwner = mockk(relaxed = true)
    private val cartViewModel = mockk<CartViewModel>()
    private val cart: Cart = mockk(relaxed = true)
    private val items: List<Product> = listOf()
    private val progressBar: ProgressBar = mockk(relaxed = true)
    private val shimmerFinalPrice: ShimmerFrameLayout = mockk(relaxed = true)
    private val finalPrice: TextView = mockk(relaxed = true)

    @Test
    fun calculateTotalPriceFor100(): Unit = runTest {
        val price = "100"
        val quantity = 2
        val adapter = CartAdapter(items, cartViewModel, cart, lifecycleOwner, this, progressBar, shimmerFinalPrice, finalPrice)
        val result = adapter.calculateTotalPrice(price, quantity)

        assertEquals(200.0, result)
    }
}