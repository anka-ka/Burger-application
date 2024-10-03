package com.example.burgerapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.burgerapplication.R
import com.example.burgerapplication.databinding.LoginAndPasswordFragmentBinding
import com.example.burgerapplication.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginAndPasswordFragment:Fragment(R.layout.login_and_password_fragment) {

    private lateinit var binding: LoginAndPasswordFragmentBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = LoginAndPasswordFragmentBinding.inflate(inflater, container, false)

        binding.backToMenuFromLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginAndPassordFragment_to_menuFragment)
        }


        binding.signInButton.setOnClickListener {
            val login = binding.login.text.toString()
            val password = binding.password.text.toString()
            loginViewModel.login(login, password)
        }

        loginViewModel.isAuthenticated.observe(viewLifecycleOwner, Observer { isAuthenticated ->
            if (isAuthenticated) {
                val intent = Intent(requireContext(), AppActivity::class.java)
                startActivity(intent)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}