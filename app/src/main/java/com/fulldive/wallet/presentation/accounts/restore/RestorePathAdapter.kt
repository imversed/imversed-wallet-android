package com.fulldive.wallet.presentation.accounts.restore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fulldive.wallet.components.BaseDiffComparator
import com.fulldive.wallet.extensions.getColorCompat
import com.fulldive.wallet.presentation.accounts.restore.RestorePathAdapter.NewWalletHolder
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.utils.WDp

class RestorePathAdapter(
    private val onItemClickListener: (WalletItem) -> Unit
) : RecyclerView.Adapter<NewWalletHolder>() {
    private var items: List<WalletItem> = emptyList()

    private val diffCallback = DiffComparator()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): NewWalletHolder {
        return NewWalletHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_new_wallet, viewGroup, false)
        )
    }

    override fun onBindViewHolder(holder: NewWalletHolder, position: Int) {
        val item = items[position]

        val context = holder.itemView.context

        holder.newPath.text = item.mnemonicPath
        holder.newAddress.text = item.address
        holder.newState.setText(item.state.stateText)
        holder.newState.setTextColor(context.getColorCompat(item.state.stateColor))
        holder.cardView.setOnClickListener { onItemClickListener(item) }

        holder.coinDenom.setText(item.symbol)
        holder.coinDenom.setTextColor(context.getColorCompat(item.color))
        holder.coinAmount.text = WDp.getDpAmount2(
            item.amount,
            item.divideDecimal,
            item.displayDecimal
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(items: List<WalletItem>) {
        val diffs = diffCallback.calculateDiff(this.items, items)
        this.items = items
        diffs.dispatchUpdatesTo(this)
    }

    class NewWalletHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val newPath: TextView = itemView.findViewById(R.id.newPathLayout)
        val newState: TextView = itemView.findViewById(R.id.new_state)
        val newAddress: TextView = itemView.findViewById(R.id.new_address)
        val coinDenom: TextView = itemView.findViewById(R.id.coin_denom)
        val coinAmount: TextView = itemView.findViewById(R.id.coin_amount)
    }

    private class DiffComparator : BaseDiffComparator<WalletItem>() {
        override fun compareItems(oldItem: WalletItem, newItem: WalletItem) =
            oldItem.address == newItem.address

        override fun compareContents(oldItem: WalletItem, newItem: WalletItem): Boolean {
            return oldItem == newItem
        }
    }
}