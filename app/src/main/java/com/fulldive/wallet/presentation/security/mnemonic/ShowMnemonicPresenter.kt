package com.fulldive.wallet.presentation.security.mnemonic

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.ClipboardInteractor
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.joom.lightsaber.ProvidedBy
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.base.BaseChain
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.dialog.Dialog_Safe_Copy
import wannabit.io.cosmostaion.utils.WKey
import wannabit.io.cosmostaion.utils.WUtil
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class ShowMnemonicPresenter @Inject constructor(
    private val accountsInteractor: AccountsInteractor,
    private val clipboardInteractor: ClipboardInteractor
) : BaseMoxyPresenter<ShowMnemonicMoxyView>() {

    var accountId: Long = -1
    var entropy = ""
    private var mnemonicWords: List<String> = emptyList()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        mnemonicWords = WKey.getRandomMnemonic(WUtil.hexStringToByteArray(entropy)).toList()
        viewState.showMnemonicWords(mnemonicWords)

        accountsInteractor
            .getAccount(accountId)
            .map(Account::baseChain)
            .map(BaseChain::getChain)
            .withDefaults()
            .compositeSubscribe(
                onSuccess = viewState::showChain
            )
    }

    fun onOkClicked() {
        viewState.showMainActivity(3)
    }

    fun onCopyClicked() {
        val delete = Dialog_Safe_Copy.newInstance() // TODO: refactoring
        viewState.showDialog(delete, "dialog", true)
    }

    fun onRawCopyClicked() {
        clipboardInteractor
            .copyToClipboard(mnemonicWords.joinToString(" "))
            .withDefaults()
            .compositeSubscribe(
                onSuccess = {
                    viewState.showMessage(R.string.str_copied)
                }
            )
    }

    fun onSafeCopyClicked() {
        clipboardInteractor
            .copyToSafeClipboard(mnemonicWords.joinToString(" "))
            .withDefaults()
            .compositeSubscribe(
                onSuccess = {
                    viewState.showMessage(R.string.str_safe_copied)
                }
            )
    }
}
