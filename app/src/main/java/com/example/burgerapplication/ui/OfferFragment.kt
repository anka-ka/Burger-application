package com.example.burgerapplication.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.burgerapplication.R
import com.example.burgerapplication.adapter.OfferAdapter
import com.example.burgerapplication.auth.AppAuth
import com.example.burgerapplication.viewmodel.OfferViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OfferFragment: Fragment(R.layout.special_offer_fragment) {
    private lateinit var recyclerView: RecyclerView
    private val offerViewModel: OfferViewModel by viewModels()
    private val appAuth: AppAuth by lazy { (requireActivity() as AppActivity).appAuth }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.offerRecyclerView)
        setupRecyclerView()

        if (appAuth.isAuthenticated()) {
            offerViewModel.loadOffersBasedOnLanguage()
        } else {
            Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_offerFragment_to_loginAndPasswordFragment)
        }

        offerViewModel.offers.observe(viewLifecycleOwner) { offers ->
            (recyclerView.adapter as OfferAdapter).submitList(offers)
        }

        offerViewModel.authError.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }


        view.findViewById<View>(R.id.back).setOnClickListener {
            findNavController().navigate(R.id.action_offerFragment_to_menuFragment)
        }
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = OfferAdapter()
        }
    }
}