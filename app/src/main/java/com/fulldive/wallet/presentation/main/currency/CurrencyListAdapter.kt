package com.fulldive.wallet.presentation.main.currency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fulldive.wallet.models.Currency
import wannabit.io.cosmostaion.R

class CurrencyListAdapter(
    private var items: List<Currency>,
    private val onItemClicked: (Currency) -> Unit
) : RecyclerView.Adapter<CurrencyListAdapter.CurrencyHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CurrencyHolder {
        return CurrencyHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_currency, viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.title
        holder.symbolTextView.text = item.symbol
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CurrencyHolder(
        itemView: View,
        val textView: TextView = itemView.findViewById(R.id.textView),
        val symbolTextView: TextView = itemView.findViewById(R.id.symbolTextView)
    ) : RecyclerView.ViewHolder(itemView)
}