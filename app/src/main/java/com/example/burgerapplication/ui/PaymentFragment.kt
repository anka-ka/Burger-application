package com.example.burgerapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.burgerapplication.R
import com.example.burgerapplication.databinding.PaymentBinding
import com.example.burgerapplication.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentFragment:Fragment(R.layout.payment) {


    private lateinit var binding: PaymentBinding
    private val cartViewModel: CartViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToCartFromPayment.setOnClickListener {
            findNavController().navigate(R.id.action_paymentFragment_to_basketFragment)
        }
        binding.settingsOnPaymentProduct.setOnClickListener {
            findNavController().navigate(R.id.action_paymentFragment_to_settingsFragment)
        }

        cartViewModel.cartResponse.observe(viewLifecycleOwner) { cartResponse ->
            cartResponse?.let {
                binding.totalPriceFromCart.text = cartResponse.finalPrice.toString()
                binding.acceptPayment.setOnClickListener {
                    val selectedPaymentMethod =
                        when (binding.radioGroupPayment.checkedRadioButtonId) {
                            R.id.radioCash -> "cash"
                            R.id.radioBonus -> "points"
                            else -> "cash"
                        }

                    cartViewModel.sendOrder(selectedPaymentMethod)

                }

                cartViewModel.orderResponse.observe(viewLifecycleOwner) { orderResponse ->
                    orderResponse?.let {
                        if (it.success) {
                            findNavController().navigate(R.id.action_paymentFragment_to_successPaymentFragment)
                            viewLifecycleOwner.lifecycleScope.launch {
                                cartViewModel.clearCart()
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                 R.string.order_error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}