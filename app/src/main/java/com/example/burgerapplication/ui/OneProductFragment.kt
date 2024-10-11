package com.example.burgerapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.burgerapplication.R
import com.example.burgerapplication.databinding.OneBurgerCardBinding
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.viewmodel.CartViewModel
import com.example.burgerapplication.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OneProductFragment : Fragment(R.layout.one_burger_card) {

    private lateinit var binding: OneBurgerCardBinding
    private val productViewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OneBurgerCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel.errorEvent.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                findNavController().navigate(R.id.action_oneBurgerFragment_to_internetCheckActivity)
            }
        }

        productViewModel.errorEvent.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                findNavController().navigate(R.id.action_oneBurgerFragment_to_internetCheckActivity)
            }
        }

        val burgerId = arguments?.getInt("id") ?: return

        productViewModel.loadProductById(burgerId)

        productViewModel.product.observe(viewLifecycleOwner) { burger ->
            burger?.let {
                bindBurger(it)

                val currentProduct = it

                binding.basketButton.setOnClickListener {
                    findNavController().navigate(R.id.action_oneBurgerFragment_to_basketFragment)
                }
                binding.numberPickerOnOneProduct.setValueChangedListener { value, _ ->
                    cartViewModel.updateCartQuantity(currentProduct, value)
                }
            }
        }
        binding.numberPickerOnOneProduct.apply {
            min = 0
            max = 100
            unit = 1
        }

        cartViewModel.getProductQuantity(burgerId)
        cartViewModel.productQuantities.observe(viewLifecycleOwner) { quantities ->
            val quantity = quantities[burgerId] ?: 0
            binding.numberPickerOnOneProduct.setValue(quantity)
        }

        binding.backToMenu?.setOnClickListener {
            findNavController().navigate(R.id.action_oneBurgerFragment_to_menuFragment)
        }
        binding.settingsOnOneProduct?.setOnClickListener {
            findNavController().navigate(R.id.action_oneBurgerFragment_to_settingsFragment)
        }
    }

    private fun bindBurger(product: Product) {

        binding.burgerName.text = product.name
        binding.burgerPrice.text = product.price
        binding.longBurgerDescription.text = product.longDescription
        Glide.with(this)
            .load(product.imageUrl ?: R.drawable.baseline_error_24)
            .placeholder(R.drawable.baseline_settings_suggest_24)
            .timeout(30_000)
            .into(binding.burgerImage)
    }
}
