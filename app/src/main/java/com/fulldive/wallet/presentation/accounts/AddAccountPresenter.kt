package com.fulldive.wallet.presentation.accounts

import android.content.Context
import android.content.Intent
import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.presentation.accounts.create.CreateAccountActivity
import com.fulldive.wallet.presentation.accounts.restore.MnemonicRestoreActivity
import com.fulldive.wallet.presentation.accounts.restore.PrivateKeyRestoreActivity
import com.fulldive.wallet.presentation.accounts.watch.WatchAccountActivity
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.joom.lightsaber.ProvidedBy
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class AddAccountPresenter @Inject constructor() : BaseMoxyPresenter<AddAccountMoxyView>() {
    lateinit var chain: String

    fun onImportKeyClicked(packageContext: Context) {
        showActivity(packageContext, PrivateKeyRestoreActivity::class.java)
    }

    fun onImportMnemonicClicked(packageContext: Context) {
        showActivity(packageContext, MnemonicRestoreActivity::class.java)
    }

    fun onWatchAddressClicked(packageContext: Context) {
        showActivity(packageContext, WatchAccountActivity::class.java)
    }

    fun onCreateClicked(packageContext: Context) {
        showActivity(packageContext, CreateAccountActivity::class.java)
    }

    private fun showActivity(packageContext: Context, clazz: Class<*>) {
        val intent = Intent(packageContext, clazz)
        if (chain.isNotEmpty()) {
            intent.putExtra(CreateAccountActivity.KEY_CHAIN, chain)
        }
        viewState.startActivity(intent)
        viewState.dismiss()
    }
}