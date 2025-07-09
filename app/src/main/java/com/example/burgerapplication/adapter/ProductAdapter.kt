package com.example.burgerapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.burgerapplication.R
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.ui.AnimationUtils
import com.example.burgerapplication.viewmodel.CartViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductAdapter(
    private val cartViewModel: CartViewModel,
    private val basketIcon: ImageView,
    private val onBurgerClick: (Product) -> Unit,

) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(BurgerDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_burger, parent, false)
        return ProductViewHolder(view, cartViewModel, basketIcon)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val burger = getItem(position)
        holder.bind(burger)


        holder.itemView.setOnClickListener {
            onBurgerClick(burger)
        }
    }

    class ProductViewHolder(
        itemView: View,
        private val cartViewModel: CartViewModel,
        private val basketIcon: ImageView
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val burgerImage: ImageView = itemView.findViewById(R.id.burgerImage)
        private val addToCartButton: FloatingActionButton = itemView.findViewById(R.id.addToCart)
        private val context: Context = itemView.context

        fun bind(product: Product) {
            itemView.findViewById<TextView>(R.id.burgerName).text = product.name
            itemView.findViewById<TextView>(R.id.shortBurgerDescription).text = product.shortDescription
            itemView.findViewById<TextView>(R.id.burgerPrice).text = product.price.toFloatOrNull()?.toString()

            Glide.with(context)
                .load(product.imageUrl ?: R.drawable.baseline_error_24)
                .placeholder(R.drawable.baseline_settings_suggest_24)
                .timeout(30_000)
                .into(burgerImage)

            addToCartButton.setOnClickListener {
                cartViewModel.addToCart(product)

                val rootLayout = itemView.rootView as ViewGroup

                AnimationUtils.animateProductToCart(
                    productImage = burgerImage,
                    basketIcon = basketIcon,
                    rootLayout = rootLayout,
                    context = context
                )
            }
        }
    }

    class BurgerDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}