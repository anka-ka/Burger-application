package com.example.burgerapplication.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.burgerapplication.R
import com.example.burgerapplication.adapter.OfferAdapter
import com.example.burgerapplication.viewmodel.OfferViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OfferFragment: Fragment(R.layout.special_offer_fragment) {
    private lateinit var recyclerView: RecyclerView
    private val offerViewModel: OfferViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.offerRecyclerView)
        setupRecyclerView()
        view.findViewById<View>(R.id.back).setOnClickListener {
            findNavController().navigate(R.id.action_offerFragment_to_menuFragment)
        }

        offerViewModel.loadOffers()
        offerViewModel.offers.observe(viewLifecycleOwner) { offers ->
            (recyclerView.adapter as OfferAdapter).submitList(offers)
        }
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = OfferAdapter()
        }
    }
}