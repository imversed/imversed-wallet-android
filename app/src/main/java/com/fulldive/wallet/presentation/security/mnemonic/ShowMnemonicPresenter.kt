package com.fulldive.wallet.presentation.security.mnemonic

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.ClipboardInteractor
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.interactors.secret.SecretInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.fulldive.wallet.rx.AppSchedulers
import com.joom.lightsaber.ProvidedBy
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.dialog.Dialog_Safe_Copy
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class ShowMnemonicPresenter @Inject constructor(
    private val accountsInteractor: AccountsInteractor,
    private val secretInteractor: SecretInteractor,
    private val clipboardInteractor: ClipboardInteractor
) : BaseMoxyPresenter<ShowMnemonicMoxyView>() {

    var accountId: Long = -1
    var entropy = ""
    private var mnemonicWords: List<String> = emptyList()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        secretInteractor
            .getRandomMnemonic(entropy)
            .withDefaults()
            .compositeSubscribe(
                onSuccess = { words ->
                    this.mnemonicWords = words
                    viewState.showMnemonicWords(words)
                }
            )

        accountsInteractor
            .getAccount(accountId)
            .map(Account::baseChain)
            .flatMap { chainName ->
                safeSingle {
                    BaseChain.getChain(chainName)
                }
            }
            .withDefaults()
            .compositeSubscribe(
                onSuccess = viewState::showChain
            )
    }

    fun onOkClicked() {
        viewState.finish()
    }

    fun onCopyClicked() {
        val delete = Dialog_Safe_Copy.newInstance() // TODO: refactoring
        viewState.showDialog(delete, "dialog", true)
    }

    fun onRawCopyClicked() {
        if (mnemonicWords.isNotEmpty()) {
            clipboardInteractor
                .copyToClipboard(mnemonicWords.joinToString(" "))
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
        if (mnemonicWords.isNotEmpty()) {
            clipboardInteractor
                .copyToSafeClipboard(mnemonicWords.joinToString(" "))
                .withDefaults()
                .compositeSubscribe(
                    onSuccess = {
                        viewState.showMessage(R.string.str_safe_mnemonic_copied)
                    }
                )
        }
    }
}
