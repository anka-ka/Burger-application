package com.example.burgerapplication.ui


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BasketFragment : Fragment(R.layout.basket_fragment) {

    private val cartViewModel: CartViewModel by activityViewModels()

    private var _binding: BasketFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var cartAdapter: CartAdapter
    private lateinit var appAuth: AppAuth
    private var cart = Cart(0, 0)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = BasketFragmentBinding.bind(view)

        loadingIndicator = view.findViewById(R.id.progress)
        appAuth = context?.let { AppAuth(it) }!!

        cartViewModel.errorEvent.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                findNavController().navigate(R.id.action_basketFragment_to_internetCheckActivity)
            }
        }

        cartAdapter = CartAdapter(emptyList(), cartViewModel, cart, viewLifecycleOwner, viewLifecycleOwner.lifecycleScope)
        binding.burgerRecyclerView.adapter = cartAdapter
        binding.burgerRecyclerView.layoutManager = LinearLayoutManager(requireContext())

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
            cartViewModel.cartResponse.value?.let { cartResponse ->
                if (cartResponse.products.isNotEmpty()) {
                    cartViewModel.updateCartData()
                    findNavController().navigate(R.id.action_basketFragment_to_paymentFragment)
                } else {
                    Toast.makeText(requireContext(), R.string.empty_cart, Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.refresh.setOnRefreshListener {
            cartViewModel.updateCartData()
        }

        cartViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.GONE
                binding.refresh.isRefreshing = false
            }
        }

        cartViewModel.cartResponse.observe(viewLifecycleOwner) { cartResponse ->
            cartResponse?.let {
                cartAdapter.updateCart(cartResponse.products)
                binding.points.text = cartResponse.points.toString()
                binding.finalPrice.text = cartResponse.finalPrice.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}