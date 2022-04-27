package com.fulldive.wallet.presentation.chains.switcher

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fulldive.wallet.components.BaseDiffComparator
import com.fulldive.wallet.extensions.getColorCompat
import wannabit.io.cosmostaion.R

class AccountListAdapter(
    private val onChainHeaderClickListener: (String) -> Unit,
    private val onAccountClickListener: (Long) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<ChainsAccountItem> = emptyList()
    private val diffCallback = DiffComparator()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> ChainHeaderHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.item_chain_header, viewGroup, false)
            )
            TYPE_ACCOUNT -> AccountHolder(
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.item_chain_account, viewGroup, false)
            )
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val chain = item.chain
        val account = item.account
        val context = viewHolder.itemView.context
        if (viewHolder is ChainHeaderHolder) {
            viewHolder.apply {
                cardView.setCardBackgroundColor(context.getColorCompat(chain.chainBackground))
                if (item.count > 0) {
                    cardView.setOnClickListener {
                        onChainHeaderClickListener(chain.chainName)
                    }
                } else {
                    cardView.setOnClickListener(null)
                }
                chainImageView.setImageResource(chain.chainIcon)
                chainNameTextView.setText(chain.chainAlterTitle)
                accountsCountTextView.text = context
                    .getString(
                        if (item.expanded) R.string.str_collapse_group else R.string.str_expand_group,
                        item.count
                    )
            }
        } else if (viewHolder is AccountHolder && account != null) {
            viewHolder.apply {
                cardView.setOnClickListener {
                    onAccountClickListener(account.id)
                }
                contentContainer.setBackgroundResource(
                    if (item.selected) R.drawable.box_round_selected_white else R.drawable.box_round_darkgray
                )
                val chainColor = ContextCompat.getColor(context, chain.chainColor)
                addressTextView.text = account.address
                amountTextView.text = account.getLastTotal(chain, item.lastTotal)
                keyImageView.setColorFilter(
                    ContextCompat.getColor(context, R.color.colorGray0),
                    PorterDuff.Mode.SRC_IN
                )

                denomTextView.setTextColor(chainColor)
                denomTextView.text = context.getString(chain.symbolTitle)

                if (account.hasPrivateKey) {
                    keyImageView.setColorFilter(
                        chainColor,
                        PorterDuff.Mode.SRC_IN
                    )
                }

                walletNameTextView.text = account.getAccountTitle(context)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when {
            item.account != null -> TYPE_ACCOUNT
            else -> TYPE_HEADER
        }
    }

    fun setItems(items: List<ChainsAccountItem>) {
        val diffs = diffCallback.calculateDiff(this.items, items)
        this.items = items
        diffs.dispatchUpdatesTo(this)
    }

    private class DiffComparator : BaseDiffComparator<ChainsAccountItem>() {
        override fun compareItems(oldItem: ChainsAccountItem, newItem: ChainsAccountItem) =
            oldItem.chain == newItem.chain && oldItem.account?.id == newItem.account?.id

        override fun compareContents(
            oldItem: ChainsAccountItem,
            newItem: ChainsAccountItem
        ): Boolean {
            return oldItem == newItem
        }
    }


    class ChainHeaderHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView = itemView.findViewById<CardView>(R.id.cardView)
        val chainImageView = itemView.findViewById<ImageView>(R.id.chainImageView)
        val chainNameTextView = itemView.findViewById<TextView>(R.id.chainNameTextView)
        val accountsCountTextView = itemView.findViewById<TextView>(R.id.accountsCountTextView)
    }

    class AccountHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView = itemView.findViewById<CardView>(R.id.cardView)
        val contentContainer = itemView.findViewById<LinearLayout>(R.id.contentContainer)
        val keyImageView = itemView.findViewById<ImageView>(R.id.keyImageView)
        val walletNameTextView = itemView.findViewById<TextView>(R.id.walletNameTextView)
        val addressTextView = itemView.findViewById<TextView>(R.id.addressTextView)
        val amountTextView = itemView.findViewById<TextView>(R.id.amountTextView)
        val denomTextView = itemView.findViewById<TextView>(R.id.denomTextView)
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ACCOUNT = 1
    }
}