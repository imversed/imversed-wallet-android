package com.fulldive.wallet.presentation.security.mnemonic.layout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.fulldive.wallet.extensions.getColorCompat
import com.fulldive.wallet.interactors.secret.MnemonicUtils
import com.fulldive.wallet.presentation.base.BaseMvpFrameLayout
import com.joom.lightsaber.getInstance
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.base.BaseChain
import wannabit.io.cosmostaion.databinding.LayoutMnemonicBinding

class MnemonicLayout : BaseMvpFrameLayout<LayoutMnemonicBinding>, MnemonicMoxyView {
    override fun getViewBinding() = LayoutMnemonicBinding
        .inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @InjectPresenter
    lateinit var presenter: MnemonicPresenter

    @ProvidePresenter
    fun providePresenter() = appInjector.getInstance<MnemonicPresenter>()

    override fun setColors(@ColorRes backgroundResId: Int, @DrawableRes wordsBackgroundResId: Int) {
        binding {
            cardView.setCardBackgroundColor(
                context.getColorCompat(
                    backgroundResId
                )
            )
            mnemonicsContainer.applyToViews<View>(
                LAYOUT_PREFIX,
                MnemonicUtils.MNEMONIC_WORDS_COUNT
            ) { _, view ->
                view.setBackgroundResource(wordsBackgroundResId)
            }
        }
    }

    override fun showMnemonicWords(mnemonicWords: List<String>) {
        binding {
            mnemonicsContainer.applyToViews<TextView>(
                HINT_PREFIX,
                MnemonicUtils.MNEMONIC_WORDS_COUNT
            ) { index, textView ->
                textView.text = if (index < mnemonicWords.size) {
                    context.getString(R.string.str_mnemonic_counter_template, index + 1)
                } else {
                    ""
                }
            }
            mnemonicsContainer.applyToViews<TextView>(
                WORD_PREFIX,
                MnemonicUtils.MNEMONIC_WORDS_COUNT
            ) { index, textView ->
                textView.text = if (index < mnemonicWords.size) {
                    mnemonicWords[index]
                } else {
                    ""
                }
            }
            mnemonicsContainer.applyToViews<View>(
                LAYOUT_PREFIX,
                MnemonicUtils.MNEMONIC_WORDS_COUNT
            ) { index, view ->
                view.alpha = if (index < mnemonicWords.size) {
                    1.0f
                } else {
                    0.5f
                }
            }
        }
    }

    fun setChain(chain: BaseChain) {
        presenter.onChainChanged(chain)
    }

    fun setMnemonicWords(words: List<String>) {
        presenter.onMnemonicChanged(words)
    }

    private fun <T> View.applyToViews(
        prefix: String,
        count: Int,
        block: (Int, T) -> Unit
    ) {
        return (0 until count).forEach { index ->
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

    companion object {
        private const val LAYOUT_PREFIX = "mnemonicLayout"
        private const val HINT_PREFIX = "hintTextView"
        private const val WORD_PREFIX = "mnemonicTextView"
    }
}