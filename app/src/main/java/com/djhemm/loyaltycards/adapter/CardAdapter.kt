package com.djhemm.loyaltycards.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.djhemm.loyaltycards.R
import com.djhemm.loyaltycards.model.LoyaltyCard
import java.text.SimpleDateFormat
import java.util.Locale

class CardAdapter(
    private var cards: List<LoyaltyCard> = emptyList(),
    private val onItemClick: (LoyaltyCard) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
    
    private val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    
    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storeName: TextView = itemView.findViewById(R.id.storeName)
        val cardNumber: TextView = itemView.findViewById(R.id.cardNumber)
        val category: TextView = itemView.findViewById(R.id.category)
        val expiryDate: TextView = itemView.findViewById(R.id.expiryDate)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        
        holder.storeName.text = card.storeName
        holder.cardNumber.text = card.cardNumber ?: card.barcode ?: holder.itemView.context.getString(R.string.no_card_number)
        holder.category.text = card.category.takeIf { it.isNotBlank() } ?: holder.itemView.context.getString(R.string.category_other)
        
        card.expiryDate?.let {
            holder.expiryDate.text = "${holder.itemView.context.getString(R.string.expiry_date)}: ${dateFormat.format(it)}"
            holder.expiryDate.visibility = View.VISIBLE
        } ?: run {
            holder.expiryDate.visibility = View.GONE
        }
        
        holder.itemView.setOnClickListener { onItemClick(card) }
    }
    
    override fun getItemCount(): Int = cards.size
    
    fun updateCards(newCards: List<LoyaltyCard>) {
        cards = newCards
        notifyDataSetChanged()
    }
}
