package com.fulldive.wallet.presentation.accounts.restore

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fulldive.wallet.extensions.getColorCompat
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.base.BaseMvpActivity
import com.fulldive.wallet.presentation.security.password.CheckPasswordActivity
import com.fulldive.wallet.presentation.security.password.SetPasswordActivity
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.databinding.ActivityRestoreBinding

class MnemonicRestoreActivity : BaseMvpActivity<ActivityRestoreBinding>(), MnemonicRestoreMoxyView {
    private var mnemonicAdapter: WordsAdapter? = null
    private val launcher = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            presenter.onPasswordStepPassed()
        }
    }

    private val chain by unsafeLazy {
        intent.getStringExtra(KEY_CHAIN)?.let(BaseChain::getChain)
            ?: throw  IllegalStateException("Chain can't be null")
    }

    private val presenter by moxyPresenter {
        appInjector.getInstance<MnemonicRestorePresenter>()
            .also {
                it.chain = chain
            }
    }

    override fun getViewBinding() = ActivityRestoreBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        binding {
            setSupportActionBar(toolbar)

            recyclerView.layoutManager = LinearLayoutManager(
                this@MnemonicRestoreActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerView.setHasFixedSize(true)
            mnemonicAdapter = WordsAdapter(
                presenter::onSuggestionClicked
            )
            recyclerView.adapter = mnemonicAdapter

            clearAllButton.setOnClickListener {
                presenter.onClearAllClicked()
            }

            keyboardLayout.setUppercase(false)
            keyboardLayout.setShuffle(false)
            keyboardLayout.setNextButtonVisible(true)
            keyboardLayout.setLeftActionButton(R.string.str_paste) {
                presenter.onPasteClicked()
            }

            keyboardLayout.setRightActionButton(R.string.str_confirm) {
                presenter.onConfirmClicked()
            }

            keyboardLayout.onKeyListener = { key ->
                presenter.onKeyboardKeyClicked(key)
            }
            keyboardLayout.onDeleteKeyListener = {
                presenter.onDeleteKeyClicked()
            }
            keyboardLayout.onNextKeyListener = {
                presenter.onNextKeyClicked()
            }

            mnemonicsLayout.onFocusChangeListener = { index ->
                presenter.onFieldClicked(index)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setDictionary(words: List<String>) {
        mnemonicAdapter?.items = words
    }

    override fun showChain(chain: BaseChain) {
        binding {
            chainImageView.setColorFilter(
                chainImageView.getColorCompat(chain.chainColor),
                PorterDuff.Mode.SRC_IN
            )
            contentsLayout.isVisible = true
            mnemonicsLayout.focusOnFirst()
        }
    }

    override fun updateField(index: Int, text: String, requestFocus: Boolean) {
        binding {
            mnemonicsLayout.updateField(index, text, requestFocus)
            mnemonicAdapter?.filter?.filter(text)
        }
    }

    override fun updateFields(items: Array<String>, focusedFieldIndex: Int) {
        binding {
            mnemonicsLayout.updateFields(items, focusedFieldIndex)
        }
    }

    override fun showWordsCount(count: Int, colorResId: Int) {
        binding {
            wordsCountView.text = getString(R.string.str_words_count, count)
            wordsCountView.setTextColor(wordsCountView.getColorCompat(colorResId))
        }
    }

    override fun checkPassword() {
        launcher.launch(
            Intent(this@MnemonicRestoreActivity, CheckPasswordActivity::class.java),
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_bottom, R.anim.fade_out)
        )
    }

    override fun requestSetPassword() {
        launcher.launch(
            Intent(this@MnemonicRestoreActivity, SetPasswordActivity::class.java),
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_bottom, R.anim.fade_out)
        )
    }

    fun onUsingCustomPath(customPath: Int) {
        presenter.onCustomPathChanged(customPath)
    }

    override fun showRestorePathActivity(extras: Bundle) {
        Intent(this@MnemonicRestoreActivity, RestorePathActivity::class.java)
            .putExtras(extras)
            .let(::startActivity)
    }

    companion object {
        const val KEY_CHAIN = "chain"
    }
}