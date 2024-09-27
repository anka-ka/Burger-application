package com.example.burgerapplication.ui

import com.example.burgerapplication.adapter.BurgerAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.burgerapplication.R
import com.example.burgerapplication.repository.BurgerRepository
import com.example.burgerapplication.viewmodel.BurgerViewModel
import com.google.android.material.button.MaterialButton

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.menu_fragment) {

    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var repository: BurgerRepository

    private val burgerViewModel: BurgerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.burgerRecyclerView)
        setupRecyclerView()

        view.findViewById<MaterialButton>(R.id.bell).setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_offerFragment)
        }

        burgerViewModel.loadBurgers()
        burgerViewModel.burgers.observe(viewLifecycleOwner) { burgers ->
            (recyclerView.adapter as BurgerAdapter).submitList(burgers)
        }
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = BurgerAdapter()
        }
    }
}