package com.example.burgerapplication.adapter

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
import com.example.burgerapplication.dto.Burger

class BurgerAdapter : ListAdapter<Burger, BurgerAdapter.BurgerViewHolder>(BurgerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BurgerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_burger, parent, false)
        return BurgerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BurgerViewHolder, position: Int) {
        val burger = getItem(position)
        holder.bind(burger)
    }

    class BurgerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(burger: Burger) {
            itemView.findViewById<TextView>(R.id.burgerName).text = burger.name
            itemView.findViewById<TextView>(R.id.shortBurgerDescription).text = burger.shortDescription
            itemView.findViewById<TextView>(R.id.burgerPrice).text = burger.price.toFloatOrNull()?.toString()
            Glide.with(itemView.context)
                .load(burger.imageUrl ?: R.drawable.baseline_error_24)
                .placeholder(R.drawable.loading)
                .timeout(30_000)
                .into(itemView.findViewById<ImageView>(R.id.burgerImage))
        }
    }

    class BurgerDiffCallback : DiffUtil.ItemCallback<Burger>() {
        override fun areItemsTheSame(oldItem: Burger, newItem: Burger): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Burger, newItem: Burger): Boolean {
            return oldItem == newItem
        }
    }
}