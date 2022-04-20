package com.fulldive.wallet.presentation.security.key

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.isVisible
import com.fulldive.wallet.extensions.getColorCompat
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.base.BaseMvpActivity
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.activities.MainActivity
import wannabit.io.cosmostaion.databinding.ActivityShowPrivateKeyBinding

class ShowPrivateKeyActivity : BaseMvpActivity<ActivityShowPrivateKeyBinding>(),
    ShowPrivateKeyMoxyView {

    private val accountId by unsafeLazy { intent.getLongExtra(KEY_ACCOUNT_ID, -1) }
    private val entropy by unsafeLazy {
        intent.getStringExtra(KEY_ENTROPY) ?: throw  IllegalStateException("Entropy can't be null")
    }

    private val presenter by moxyPresenter {
        appInjector.getInstance<ShowPrivateKeyPresenter>()
            .also {
                it.accountId = accountId
                it.entropy = entropy
            }
    }

    override fun getViewBinding() = ActivityShowPrivateKeyBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding {
            setSupportActionBar(toolbar)
            copyButton.setOnClickListener { presenter.onCopyClicked() }
            okButton.setOnClickListener { presenter.onOkClicked() }
        }

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun showPrivateKey(key: String) {
        binding {
            privateKeyTextView.text = key
            copyButton.isVisible = true
        }
    }

    override fun showChain(chain: BaseChain) {
        binding {
            cardView.setCardBackgroundColor(
                getColorCompat(chain.chainBackground)
            )
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
            else -> super.onOptionsItemSelected(item)
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