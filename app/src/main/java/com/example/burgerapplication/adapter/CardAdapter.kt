package com.example.burgerapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.burgerapplication.R
import com.example.burgerapplication.dto.Cart
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.viewmodel.CartViewModel


class CartAdapter(
    private var items: List<Product>,
    private val cartViewModel: CartViewModel,
    private var cart: Cart,
    private val lifecycleOwner: LifecycleOwner,
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val burgerImage: ImageView = itemView.findViewById(R.id.burgerImage)
        val burgerName: TextView = itemView.findViewById(R.id.burgerName)
        val price:TextView = itemView.findViewById(R.id.price)
        val shortBurgerDescription: TextView = itemView.findViewById(R.id.shortBurgerDescription)
        val numberPicker: com.travijuu.numberpicker.library.NumberPicker = itemView.findViewById(R.id.numberPicker)
    }

    fun updateCart(newItems: List<Product>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_added_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.burgerName.text = item.name
        holder.shortBurgerDescription.text = item.shortDescription
        holder.price.text = item.price

        Glide.with(holder.itemView.context)
            .load(item.imageUrl ?: R.drawable.baseline_error_24)
            .placeholder(R.drawable.baseline_settings_suggest_24)
            .timeout(30_000)
            .into(holder.burgerImage)

        holder.numberPicker.min = 0
        holder.numberPicker.max = 100
        holder.numberPicker.unit = 1


        cartViewModel.getProductQuantity(item.id)

        cartViewModel.productQuantities.observe(lifecycleOwner) { quantities ->
            val quantity = quantities[item.id] ?: 0
            holder.numberPicker.setValue(quantity)


            holder.numberPicker.setValueChangedListener { value, _ ->
                cartViewModel.updateCartQuantity(item, value)

            }
        }
    }

    override fun getItemCount(): Int = items.size
}