package com.example.burgerapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.burgerapplication.viewmodel.BurgerViewModel


class MainActivity : AppCompatActivity() {
    @SuppressLint("DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        val burgerViewModel: BurgerViewModel by viewModels()


        val gridLayout: GridLayout = findViewById(R.id.productGrid)


        val burgers = burgerViewModel.loadBurgers(this)


        burgers.forEach { burger ->
            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                val imageView = ImageView(context).apply {

                    val imageResId = context.resources.getIdentifier(burger.imageUrl, "drawable", context.packageName)
                    setImageResource(imageResId)
                }
                val nameTextView = TextView(context).apply {
                    text = burger.name

                }
                val priceTextView = TextView(context).apply {
                    text = burger.price

                }
                addView(imageView)
                addView(nameTextView)
                addView(priceTextView)
            }
            gridLayout.addView(layout)
        }
    }
}
