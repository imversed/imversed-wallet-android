package com.fulldive.wallet.presentation.security.mnemonic.layout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.TextView
import com.fulldive.wallet.interactors.secret.MnemonicUtils
import com.fulldive.wallet.presentation.base.BaseMvpFrameLayout
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.databinding.LayoutMnemonicEditBinding

class EditMnemonicLayout : BaseMvpFrameLayout<LayoutMnemonicEditBinding> {

    var onFocusChangeListener: ((Int) -> Unit)? = null

    override fun getViewBinding() = LayoutMnemonicEditBinding
        .inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun afterInitLayout() {
        super.afterInitLayout()
        binding {
            mnemonicsContainer.applyToViews<TextView>(
                HINT_PREFIX,
                MnemonicUtils.MNEMONIC_WORDS_COUNT
            ) { index, textView ->
                textView.text = context.getString(R.string.str_mnemonic_counter_template, index + 1)
            }
            mnemonicsContainer
                .applyToViews<EditText>(EDIT_FIELD_PREFIX, 24) { position, editText ->
                    editText.showSoftInputOnFocus = false
                    editText.onFocusChangeListener =
                        OnFocusChangeListener { _: View?, hasFocus: Boolean ->
                            if (hasFocus) {
                                onFocusChangeListener?.invoke(position)
                            }
                        }
                }
        }
    }

    fun focusOnFirst() {
        binding?.mnemonicsEditText0?.requestFocus()
    }

    fun updateField(index: Int, text: String, requestFocus: Boolean) {
        binding {
            mnemonicsContainer.applyToView<EditText>(EDIT_FIELD_PREFIX, index) { editText ->
                editText.setText(text)
                if (requestFocus) {
                    editText.requestFocus()
                }
                editText.setSelection(editText.text.length)
            }
        }
    }

    fun updateFields(items: Array<String>, focusedFieldIndex: Int) {
        binding {
            mnemonicsContainer
                .applyToViews<EditText>(EDIT_FIELD_PREFIX, items.size) { index, editText ->
                    editText.setText(items[index])
                    if (index == focusedFieldIndex) {
                        editText.requestFocus()
                    }
                }
        }
    }

    private fun <T> View.applyToViews(
        prefix: String,
        count: Int,
        block: (Int, T) -> Unit
    ) {
        (0 until count).forEach { index ->
            block(
                index,
                findViewById(
                    resources.getIdentifier(
                        "$prefix$index",
                        "id",
                        context.packageName
                    )
                )
            )
        }
    }

    private fun <T> View.applyToView(
        prefix: String,
        index: Int,
        block: (T) -> Unit
    ) {
        block(
            findViewById(
                resources.getIdentifier(
                    "$prefix$index",
                    "id",
                    context.packageName
                )
            )
        )
    }

    companion object {
        private const val EDIT_FIELD_PREFIX = "mnemonicsEditText"
        private const val LAYOUT_PREFIX = "mnemonicLayout"
        private const val HINT_PREFIX = "hintTextView"
        private const val WORD_PREFIX = "mnemonicTextView"
    }
}