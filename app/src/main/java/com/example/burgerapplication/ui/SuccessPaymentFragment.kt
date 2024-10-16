package com.example.burgerapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.burgerapplication.R
import com.example.burgerapplication.databinding.SuccessPaymentBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessPaymentFragment:Fragment(R.layout.success_payment) {

    private lateinit var binding: SuccessPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SuccessPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToMenuFromSuccess.setOnClickListener {
            findNavController().navigate(R.id.action_successPaymentFragment_to_menuFragment)
        }
    }
}