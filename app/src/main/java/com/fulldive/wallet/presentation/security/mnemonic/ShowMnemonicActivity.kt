package com.fulldive.wallet.presentation.security.mnemonic

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.presentation.base.BaseMvpActivity
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.activities.MainActivity
import com.fulldive.wallet.models.BaseChain
import wannabit.io.cosmostaion.databinding.ActivityMnemonicCheckBinding

class ShowMnemonicActivity : BaseMvpActivity<ActivityMnemonicCheckBinding>(), ShowMnemonicMoxyView {
    private val accountId by unsafeLazy { intent.getLongExtra(KEY_ACCOUNT_ID, -1) }
    private val entropy by unsafeLazy {
        intent.getStringExtra(KEY_ENTROPY) ?: throw  IllegalStateException("Entropy can't be null")
    }

    private val presenter by moxyPresenter {
        appInjector.getInstance<ShowMnemonicPresenter>()
            .also {
                it.accountId = accountId
                it.entropy = entropy
            }
    }

    override fun getViewBinding() = ActivityMnemonicCheckBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        super.onCreate(savedInstanceState)
        binding {
            setSupportActionBar(toolbar)
            copyButton.setOnClickListener {
                presenter.onCopyClicked()
            }
            okButton.setOnClickListener { presenter.onOkClicked() }
        }
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun showMnemonicWords(mnemonicWords: List<String>) {
        binding {
            mnemonicsLayout.setMnemonicWords(mnemonicWords)
        }
    }

    override fun showChain(chain: BaseChain) {
        binding {
            mnemonicsLayout.setChain(chain)
        }
    }

    override fun showMainActivity(tabIndex: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("page", tabIndex)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun onRawCopy() {
        presenter.onRawCopyClicked()
    }

    fun onSafeCopy() {
        presenter.onSafeCopyClicked()
    }

    companion object {
        const val KEY_ENTROPY = "entropy"
        const val KEY_ACCOUNT_ID = "accountId"
    }
}
