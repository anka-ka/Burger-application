package com.example.burgerapplication.ui


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.burgerapplication.R
import com.example.burgerapplication.adapter.CartAdapter
import com.example.burgerapplication.auth.AppAuth
import com.example.burgerapplication.databinding.BasketFragmentBinding
import com.example.burgerapplication.dto.Cart
import com.example.burgerapplication.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BasketFragment : Fragment(R.layout.basket_fragment) {

    private val cartViewModel: CartViewModel by activityViewModels()

    private var _binding: BasketFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var cartAdapter: CartAdapter
    private lateinit var appAuth: AppAuth
    private var cart = Cart(0, 0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = BasketFragmentBinding.bind(view)
        val progressBar = binding.progress
        appAuth = context?.let { AppAuth(it) }!!

        setupViews(progressBar)
        setupObservers()
        setupListeners()
    }

    private fun setupViews(progressBar: ProgressBar) {
        cartAdapter = CartAdapter(emptyList(), cartViewModel, cart, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope, progressBar)
        binding.burgerRecyclerView.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    private fun setupObservers() {
        cartViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progress.visibility = View.VISIBLE
                binding.progress.bringToFront()
            } else {
                binding.progress.visibility = View.GONE
            }
            binding.refresh.isRefreshing = isLoading
            binding.proceedToPayment.isEnabled = !isLoading
        }

        cartViewModel.cartResponse.observe(viewLifecycleOwner) { cartResponse ->
            cartResponse?.let {
                cartAdapter.updateCart(cartResponse.products)
                binding.points.text = cartResponse.points.toString()
                binding.finalPrice.text = cartResponse.finalPrice.toString()
            }
        }
    }

    private fun setupListeners() {
        binding.clearCart.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                cartViewModel.clearCart()
                cartViewModel.updateCartData()
            }
        }

        binding.backToMenuFromBasket.setOnClickListener {
            findNavController().navigate(R.id.action_basketFragment_to_menuFragment)
        }

        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.action_basketFragment_to_settingsFragment)
        }

        binding.proceedToPayment.setOnClickListener {
            if (cartViewModel.isProcessing.value) {
                Toast.makeText(requireContext(), "Still loading...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    binding.proceedToPayment.isEnabled = false
                    binding.progress.visibility = View.VISIBLE
                    cartViewModel._isProcessing.value = true

                    cartViewModel.updateCartData()

                    cartViewModel.isLoading.asFlow()
                        .combine(cartViewModel.isProcessing) { loading, processing -> loading || processing }
                        .filter { !it }
                        .first()

                    val cartResponse = cartViewModel.cartResponse.value
                    when {
                        cartResponse == null -> {
                            Toast.makeText(requireContext(), "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                        }
                        cartResponse.products.isEmpty() -> {
                            Toast.makeText(requireContext(), R.string.empty_cart, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            findNavController().navigate(R.id.action_basketFragment_to_paymentFragment)
                        }
                    }
                } finally {
                    binding.proceedToPayment.isEnabled = true
                    binding.progress.visibility = View.GONE
                }
            }
        }

        binding.refresh.setOnRefreshListener {
            cartViewModel.updateCartData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}