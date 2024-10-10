package com.example.burgerapplication.ui


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
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
        val token = appAuth.getAuthToken()


        cartAdapter = CartAdapter(emptyList(), cartViewModel, cart)
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
                Log.d("BasketFragment", "Продукты: ${cartResponse.products}")
            } ?: run {
                Log.e("BasketFragment", "Ошибка загрузки корзины")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}