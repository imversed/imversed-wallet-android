package com.fulldive.wallet.presentation.accounts.restore

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityOptionsCompat
import com.fulldive.wallet.extensions.hideKeyboard
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.base.BaseMvpActivity
import com.fulldive.wallet.presentation.security.password.CheckPasswordActivity
import com.fulldive.wallet.presentation.security.password.SetPasswordActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.activities.MainActivity
import wannabit.io.cosmostaion.databinding.ActivityRestoreKeyBinding

class PrivateKeyRestoreActivity : BaseMvpActivity<ActivityRestoreKeyBinding>(),
    PrivateKeyRestoreMoxyView {

    private val userInput: String
        get() {
            return binding?.addressEditText?.text?.toString()?.trim().orEmpty()
                .removePrefix(ADDRESS_PREFIX)
        }

    private val launcher = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            IntentIntegrator
                .parseActivityResult(result.resultCode, result.data)
                .let(presenter::onScanQRResultsReceived)
        }
    }

    private val launcherSetPassword = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            presenter.onCheckPasswordSuccessfully()
        }
    }

    private val chain by unsafeLazy {
        intent.getStringExtra(MnemonicRestoreActivity.KEY_CHAIN)?.let(BaseChain::getChain)
            ?: throw  IllegalStateException("Chain can't be null")
    }

    private val presenter by moxyPresenter {
        appInjector.getInstance<PrivateKeyRestorePresenter>()
            .also {
                it.chain = chain
            }
    }

    override fun getViewBinding() = ActivityRestoreKeyBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding {
            setSupportActionBar(toolbar)
            cancelButton.setOnClickListener {
                presenter.onCancelClicked()
            }
            nextButton.setOnClickListener {
                presenter.onNextClicked(userInput)
            }
            scanQRButton.setOnClickListener {
                onScanQRClicked()
            }
            pasteButton.setOnClickListener {
                presenter.onPasteClicked()
            }
        }

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setPrivateKey(text: String) {
        binding {
            addressEditText.setText(text)
            addressEditText.setSelection(addressEditText.text.length)
        }
    }

    override fun requestCheckPassword() {
        launcherSetPassword.launch(
            Intent(this, CheckPasswordActivity::class.java),
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_bottom, R.anim.fade_out)
        )
    }

    override fun requestCreatePassword() {
        launcherSetPassword.launch(
            Intent(this, SetPasswordActivity::class.java),
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_bottom, R.anim.fade_out)
        )
    }

    override fun showMainActivity() {
        hideKeyboard()
        Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .let(::startActivity)
    }

    override fun finish() {
        hideKeyboard()
        super.finish()
    }

    fun onCheckOecAddressType(okAddressType: Int) {
        presenter.onCheckOecAddressType(okAddressType, userInput)
    }

    private fun onScanQRClicked() {
        launcher.launch(
            IntentIntegrator(this)
                .setOrientationLocked(true)
                .createScanIntent()
        )
    }

    companion object {
        private const val ADDRESS_PREFIX = "0x"
        const val KEY_CHAIN = "chain"
    }
}