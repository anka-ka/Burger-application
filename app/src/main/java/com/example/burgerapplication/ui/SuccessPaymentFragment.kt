package com.example.burgerapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.burgerapplication.R
import com.example.burgerapplication.auth.AppAuth
import com.example.burgerapplication.databinding.SuccessPaymentBinding
import com.example.burgerapplication.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SuccessPaymentFragment:Fragment(R.layout.success_payment) {

    private lateinit var binding: SuccessPaymentBinding
    private val loginViewModel: LoginViewModel by activityViewModels()

    @Inject
    lateinit var appAuth: AppAuth

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

                viewLifecycleOwner.lifecycleScope.launch {
                    loginViewModel.loadUserData()


                findNavController().navigate(R.id.action_successPaymentFragment_to_menuFragment)
            }
        }
    }
}

