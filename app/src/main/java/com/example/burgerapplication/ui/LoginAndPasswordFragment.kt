package com.example.burgerapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.navigation.fragment.findNavController
import com.example.burgerapplication.R
import com.example.burgerapplication.databinding.LoginAndPasswordFragmentBinding
import com.example.burgerapplication.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginAndPasswordFragment : Fragment(R.layout.login_and_password_fragment) {

    private var _binding: LoginAndPasswordFragmentBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginAndPasswordFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToMenuFromLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginAndPassordFragment_to_menuFragment)
        }

        binding.signInButton.setOnClickListener {
            val login = binding.login.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (login.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(login, password)
            } else {
                Toast.makeText(requireContext(), R.string.need_both, Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.isAuthenticated.observe(viewLifecycleOwner) { isAuthenticated ->
            if (isAuthenticated) {
                val intent = Intent(requireContext(), AppActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), R.string.auth_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}