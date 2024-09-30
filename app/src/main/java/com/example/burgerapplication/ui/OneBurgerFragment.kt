package com.example.burgerapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.burgerapplication.R
import com.example.burgerapplication.databinding.OneBurgerCardBinding
import com.example.burgerapplication.dto.Burger
import com.example.burgerapplication.viewmodel.BurgerViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OneBurgerFragment: Fragment(R.layout.one_burger_card) {

    private lateinit var binding: OneBurgerCardBinding
    private val burgerViewModel: BurgerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OneBurgerCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val burgerId = arguments?.getInt("id") ?: return

        burgerViewModel.loadBurgerById(burgerId)

        burgerViewModel.burger.observe(viewLifecycleOwner) { burger ->
            burger?.let { bindBurger(it) }
        }
    }
    private fun bindBurger(burger: Burger) {

        binding.burgerName.text = burger.name
        binding.longBurgerDescription.text = burger.longDescription
        Glide.with(this)
            .load(burger.imageUrl?: R.drawable.baseline_error_24)
            .placeholder(R.drawable.baseline_settings_suggest_24)
            .timeout(30_000)
            .into(binding.burgerImage)
    }
}
