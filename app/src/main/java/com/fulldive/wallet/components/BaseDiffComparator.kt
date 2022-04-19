package com.fulldive.wallet.components

import androidx.recyclerview.widget.DiffUtil

abstract class BaseDiffComparator<T> : DiffUtil.Callback() {
    private var itemsOld: List<T> = listOf()
    private var itemsNew: List<T> = listOf()
    private var headersCount: Int = 0
    private var footersCount: Int = 0

    override fun getOldListSize(): Int = itemsOld.size + headersCount + footersCount

    override fun getNewListSize(): Int = itemsNew.size + headersCount + footersCount

    final override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition < headersCount || newItemPosition < headersCount) {
            return oldItemPosition == newItemPosition
        }

        if (oldItemPosition >= itemsOld.size + headersCount || newItemPosition >= itemsNew.size + headersCount) {
            return oldItemPosition - itemsOld.size == newItemPosition - itemsNew.size
        }
        val oldItem = itemsOld[oldItemPosition - headersCount]
        val newItem = itemsNew[newItemPosition - headersCount]

        return compareItems(oldItem, newItem)
    }

    final override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition < headersCount || newItemPosition < headersCount) {
            return oldItemPosition == newItemPosition
        }

        if (oldItemPosition >= itemsOld.size + headersCount || newItemPosition >= itemsNew.size + headersCount) {
            return oldItemPosition - itemsOld.size == newItemPosition - itemsNew.size
        }
        val oldItem = itemsOld[oldItemPosition - headersCount]
        val newItem = itemsNew[newItemPosition - headersCount]

        return compareContents(oldItem, newItem)
    }

    final override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        if (oldItemPosition < headersCount || newItemPosition < headersCount ||
            oldItemPosition >= itemsOld.size + headersCount || newItemPosition >= itemsNew.size + headersCount
        ) {
            return null
        }
        val oldItem = itemsOld[oldItemPosition - headersCount]
        val newItem = itemsNew[newItemPosition - headersCount]

        return getChanges(oldItem, newItem)
    }

    abstract fun compareItems(oldItem: T, newItem: T): Boolean
    open fun compareContents(oldItem: T, newItem: T): Boolean = true
    open fun getChanges(oldItem: T, newItem: T): Any = Unit

    fun calculateDiff(
        itemsOld: List<T>,
        itemsNew: List<T>,
        headersCount: Int = 0,
        footersCount: Int = 0,
        detectMoves: Boolean = true
    ): DiffUtil.DiffResult {
        this.itemsNew = itemsNew
        this.itemsOld = itemsOld
        this.headersCount = headersCount
        this.footersCount = footersCount
        return DiffUtil.calculateDiff(this, detectMoves)
    }
}
