package com.fulldive.wallet.presentation.accounts.restore

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.interactors.secret.MnemonicUtils
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.base.BaseMvpActivity
import com.fulldive.wallet.presentation.main.MainActivity
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.databinding.ActivityRestorePathBinding

class RestorePathActivity : BaseMvpActivity<ActivityRestorePathBinding>(), RestorePathMoxyView {
    private val entropy by unsafeLazy {
        intent.getStringExtra(KEY_ENTROPY)
            ?: throw  IllegalStateException("Entropy can't be null")
    }
    private val chain by unsafeLazy {
        intent.getStringExtra(KEY_CHAIN)?.let(BaseChain::getChain)
            ?: throw  IllegalStateException("Chain can't be null")
    }

    private val mnemonicSize by unsafeLazy {
        intent.getIntExtra(KEY_SIZE, MnemonicUtils.MNEMONIC_WORDS_COUNT)
    }

    private val customPath by unsafeLazy {
        intent.getIntExtra(KEY_CUSTOM_PATH, 0)
    }

    private val presenter by moxyPresenter {
        appInjector.getInstance<RestorePathPresenter>()
            .also {
                it.chain = chain
                it.entropy = entropy
                it.customPath = customPath
                it.mnemonicSize = mnemonicSize
            }
    }

    private lateinit var adapter: RestorePathAdapter

    override fun getViewBinding() = ActivityRestorePathBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding {
            setSupportActionBar(toolbar)

            recyclerView.layoutManager = LinearLayoutManager(
                this@RestorePathActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            recyclerView.setHasFixedSize(true)

            adapter = RestorePathAdapter { item ->
                presenter.onItemClicked(item)
            }
            recyclerView.adapter = adapter
        }

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun showItems(items: List<WalletItem>) {
        adapter.setItems(items)
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

    override fun showMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    companion object {
        const val KEY_ENTROPY = "entropy"
        const val KEY_CHAIN = "chain"
        const val KEY_SIZE = "size"
        const val KEY_CUSTOM_PATH = "customPath"
    }
}