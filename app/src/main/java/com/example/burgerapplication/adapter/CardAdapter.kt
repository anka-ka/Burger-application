package com.example.burgerapplication.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.burgerapplication.R
import com.example.burgerapplication.dto.Cart
import com.example.burgerapplication.dto.Product
import com.example.burgerapplication.viewmodel.CartViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.travijuu.numberpicker.library.NumberPicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode



class CartAdapter(
    private var items: List<Product>,
    private val cartViewModel: CartViewModel,
    private var cart: Cart,
    private val lifecycleOwner: LifecycleOwner,
    private val coroutineScope: CoroutineScope,
    private val progressBar: ProgressBar,
    private val shimmerFinalPrice: ShimmerFrameLayout,
    private val finalPrice: TextView,
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val burgerImage: ImageView = itemView.findViewById(R.id.burgerImage)
        val burgerName: TextView = itemView.findViewById(R.id.burgerName)
        val price:TextView = itemView.findViewById(R.id.price)
        val shortBurgerDescription: TextView = itemView.findViewById(R.id.shortBurgerDescription)
        val totalPrice: TextView = itemView.findViewById(R.id.totalPrice)
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

    @OptIn(FlowPreview::class)
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

            val total = calculateTotalPrice(item.price, quantity)
            holder.totalPrice.text = total.toString()
        }

        coroutineScope.launch {
            holder.numberPicker.valueChangeFlow()
                .onEach {
                    cartViewModel._isProcessing.value = true
                    progressBar.visibility = View.VISIBLE
                    shimmerFinalPrice.visibility = View.VISIBLE
                    shimmerFinalPrice.startShimmer()
                    finalPrice.visibility = View.INVISIBLE
                    holder.numberPicker.isEnabled = false
                }
                .debounce(3000)
                .distinctUntilChanged()
                .collect { value ->
                    try {
                        cartViewModel.onQuantityChanged(item, value)
                    } finally {
                        cartViewModel._isProcessing.value = false
                        progressBar.visibility = View.GONE
                        holder.numberPicker.isEnabled = true
                    }
                }
        }
    }


    override fun getItemCount(): Int = items.size

    fun NumberPicker.valueChangeFlow(): Flow<Int> = callbackFlow {
        setValueChangedListener { value, _ ->
            trySend(value).isSuccess
        }
        awaitClose { valueChangedListener = null }
    }
     fun calculateTotalPrice(price: String, quantity: Int): Double {
        val total = price.toDouble() * quantity
        return BigDecimal(total).setScale(2, RoundingMode.HALF_UP).toDouble()
    }
}