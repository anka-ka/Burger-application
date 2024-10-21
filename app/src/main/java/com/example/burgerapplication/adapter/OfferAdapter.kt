package com.example.burgerapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.burgerapplication.R
import com.example.burgerapplication.dto.Offer

class OfferAdapter : RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {

    private var offers: List<Offer> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.offer_item, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(offers[position])
    }

    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descriptionTextView: TextView = itemView.findViewById(R.id.offerDescription)

        fun bind(offer: Offer) {
            descriptionTextView.text = offer.description

        }
    }

    override fun getItemCount(): Int {
        return offers.size
    }

    fun submitList(newOffers: List<Offer>) {
        offers = newOffers
        notifyDataSetChanged()
    }
}