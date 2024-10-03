package com.example.burgerapplication.ui

import com.example.burgerapplication.adapter.ProductAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.burgerapplication.R
import com.example.burgerapplication.auth.AppAuth
import com.example.burgerapplication.repository.ProductRepository
import com.example.burgerapplication.viewmodel.ProductViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.menu_fragment) {

    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var repository: ProductRepository

    @Inject
    lateinit var appAuth: AppAuth

    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.burgerRecyclerView)
        setupRecyclerView()


        view.findViewById<MaterialButton>(R.id.bell).setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_offerFragment)
        }

        view.findViewById<MaterialButton>(R.id.settings).setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_settingsFragment)
        }

        productViewModel.loadProductsBasedOnLanguage()

        view.findViewById<MaterialButton>(R.id.burgers).setOnClickListener {
            productViewModel.loadProductsByType("burger")
        }

        view.findViewById<MaterialButton>(R.id.pizza).setOnClickListener {
            productViewModel.loadProductsByType("pizza")
        }

        view.findViewById<MaterialButton>(R.id.all).setOnClickListener {
            productViewModel.loadProductsBasedOnLanguage()
        }

        productViewModel.burgers.observe(viewLifecycleOwner) { burgers ->
            (recyclerView.adapter as ProductAdapter).submitList(burgers)
        }

        view.findViewById<MaterialButton>(R.id.menu).setOnClickListener {
            productViewModel.onMenuButtonClick()
        }

    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ProductAdapter { burger ->
                val bundle = Bundle().apply {
                    putInt(
                        "id",
                        burger.id
                    )
                }
                findNavController().navigate(R.id.oneBurgerFragment, bundle)
            }
        }
    }
}