package com.example.burgerapplication.ui

import com.example.burgerapplication.adapter.ProductAdapter
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.burgerapplication.R
import com.example.burgerapplication.auth.AppAuth
import com.example.burgerapplication.repository.ProductRepository
import com.example.burgerapplication.viewmodel.CartViewModel
import com.example.burgerapplication.viewmodel.LoginViewModel
import com.example.burgerapplication.viewmodel.ProductViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.sidesheet.SideSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.menu_fragment) {

    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var repository: ProductRepository

    @Inject
    lateinit var appAuth: AppAuth

    private val productViewModel: ProductViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel.errorEvent.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                findNavController().navigate(R.id.action_menuFragment_to_internetCheckActivity)
            }
        }

        recyclerView = view.findViewById(R.id.burgerRecyclerView)
        setupRecyclerView()


        view.findViewById<MaterialButton>(R.id.bell).setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_offerFragment)
        }

        view.findViewById<MaterialButton>(R.id.settings).setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_settingsFragment)
        }

        view.findViewById<MaterialButton>(R.id.basket).setOnClickListener {
            cartViewModel.updateCartData()

            findNavController().navigate(R.id.action_menuFragment_to_basketFragment)
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

        view.findViewById<MaterialButton>(R.id.bank).setOnClickListener {
            if (appAuth.isAuthenticated()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    loginViewModel.loadUserData()
                    showAccountSideSheet()
                }
            } else {
                Toast.makeText(requireContext(), R.string.not_auth, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = ProductAdapter(cartViewModel) { burger ->
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

    private suspend fun showAccountSideSheet() {
        val sideSheetDialog = SideSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.account_sidesheet, null)
        sideSheetDialog.setContentView(view)

        sideSheetDialog.setSheetEdge(Gravity.START)

        val firstNameTextView: TextView = view.findViewById(R.id.firstName)
        val lastNameTextView: TextView = view.findViewById(R.id.lastName)
        val userNameTextView: TextView = view.findViewById(R.id.userName)
        val pointsNumberTextView: TextView = view.findViewById(R.id.pointsNumber)

        appAuth.user.collect { user ->
            user?.let {
                firstNameTextView.text = it.firstName
                lastNameTextView.text = it.lastName
                userNameTextView.text = it.username
                pointsNumberTextView.text = it.points.toString()

                sideSheetDialog.show()
            }
        }
    }
}