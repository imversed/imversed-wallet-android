package com.fulldive.wallet.presentation.main.tokens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fulldive.wallet.components.BaseDiffComparator
import com.fulldive.wallet.extensions.getColorCompat
import com.squareup.picasso.Picasso
import wannabit.io.cosmostaion.R

class MainTokensAdapter(
    private val onItemClickListener: (TokenWrappedItem) -> Unit
) : RecyclerView.Adapter<MainTokensAdapter.TokenHolder>() {
    private var items: List<TokenWrappedItem> = emptyList()

    private val diffCallback = DiffComparator()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TokenHolder {
        return TokenHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_token, viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: TokenHolder, position: Int) {
        val item = items[position]

        with(holder) {
            cardView.setOnClickListener { onItemClickListener(item) }
            if (item.title.isEmpty() && item.titleRes > 0) {
                titleTextView.setText(item.titleRes)
            } else {
                titleTextView.text = item.title
            }
            titleTextView.setTextColor(titleTextView.getColorCompat(item.titleColorRes))
            hintTextView.text = item.hint
            descriptionTextView.text = item.description
            balanceTextView.text = item.balance
            valueTextView.text = item.value

            if (item.iconUrl.isEmpty() && item.iconResId > 0) {
                Picasso.get().load(item.iconResId).fit().placeholder(R.drawable.token_ic)
                    .error(R.drawable.token_ic).into(holder.imageView)
            } else if (item.iconUrl.isNotEmpty()) {
                Picasso.get().load(item.iconUrl).fit().placeholder(R.drawable.token_ic)
                    .error(R.drawable.token_ic).into(holder.imageView)
            } else {
                Picasso.get().cancelRequest(holder.imageView)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<TokenWrappedItem>) {
        val diffs = diffCallback.calculateDiff(this.items, items)
        this.items = items
        diffs.dispatchUpdatesTo(this)
    }

    class TokenHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView = itemView.findViewById<CardView>(R.id.cardView)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
        val hintTextView = itemView.findViewById<TextView>(R.id.hintTextView)
        val descriptionTextView =
            itemView.findViewById<TextView>(R.id.descriptionTextView)
        val balanceTextView = itemView.findViewById<TextView>(R.id.balanceTextView)
        val valueTextView = itemView.findViewById<TextView>(R.id.valueTextView)
    }

    private class DiffComparator : BaseDiffComparator<TokenWrappedItem>() {
        override fun compareItems(oldItem: TokenWrappedItem, newItem: TokenWrappedItem) =
            oldItem.id == newItem.id

        override fun compareContents(
            oldItem: TokenWrappedItem,
            newItem: TokenWrappedItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}