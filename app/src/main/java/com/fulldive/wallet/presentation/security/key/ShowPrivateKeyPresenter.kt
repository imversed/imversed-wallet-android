package com.fulldive.wallet.presentation.security.key

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.combine
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.extensions.withPrefix
import com.fulldive.wallet.interactors.ClipboardInteractor
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.interactors.secret.SecretInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.fulldive.wallet.rx.AppSchedulers
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Single
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.dialog.Dialog_Safe_Copy_pKey
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class ShowPrivateKeyPresenter @Inject constructor(
    private val accountsInteractor: AccountsInteractor,
    private val secretInteractor: SecretInteractor,
    private val clipboardInteractor: ClipboardInteractor
) : BaseMoxyPresenter<ShowPrivateKeyMoxyView>() {

    var accountId: Long = -1
    var entropy = ""
    private var privateKey: String = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        val accountSingle = accountsInteractor.getAccount(accountId)
        val chainSingle = accountSingle
            .map(Account::baseChain)
            .flatMap { chainName ->
                safeSingle {
                    BaseChain.getChain(chainName)
                }
            }
        Single.zip(accountSingle, chainSingle, ::combine)
            .flatMap { (account, chain) ->
                if (account.fromMnemonic) {
                    secretInteractor
                        .createKeyWithPathFromEntropy(
                            chain, entropy, account.path, account.customPath
                        )
                        .map { key ->
                            key.privateKeyAsHex
                        }
                } else {
                    secretInteractor.entropyToPrivateKey(
                        account.uuid,
                        account.resource,
                        account.spec
                    )
                }
            }
            .map { key ->
                key.withPrefix(SecretInteractor.PRIVATE_KEY_PREFIX)
            }
            .withDefaults()
            .compositeSubscribe(
                onSuccess = { key ->
                    this.privateKey = key
                    if (key.isNotEmpty()) {
                        viewState.showPrivateKey(key)
                    }
                }
            )
        chainSingle
            .withDefaults()
            .compositeSubscribe(
                onSuccess = viewState::showChain
            )
    }

    fun onOkClicked() {
        viewState.finish()
    }

    fun onCopyClicked() {
        viewState.showDialog(Dialog_Safe_Copy_pKey.newInstance(), "dialog", true)
    }

    fun onRawCopyClicked() {
        if (privateKey.isNotEmpty()) {
            clipboardInteractor
                .copyToClipboard(privateKey)
                .subscribeOn(AppSchedulers.ui())
                .observeOn(AppSchedulers.ui())
                .compositeSubscribe(
                    onSuccess = {
                        viewState.showMessage(R.string.str_copied)
                    }
                )
        }
    }

    fun onSafeCopyClicked() {
        if (privateKey.isNotEmpty()) {
            clipboardInteractor
                .copyToSafeClipboard(privateKey)
                .withDefaults()
                .compositeSubscribe(
                    onSuccess = {
                        viewState.showMessage(R.string.str_safe_key_copied)
                    }
                )
        }
    }
}
