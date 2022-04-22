package com.fulldive.wallet.presentation.accounts.restore

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.toSingle
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.ClipboardInteractor
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.interactors.accounts.DuplicateAccountException
import com.fulldive.wallet.interactors.secret.MnemonicUtils
import com.fulldive.wallet.interactors.secret.SecretInteractor
import com.fulldive.wallet.interactors.secret.WalletUtils
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.fulldive.wallet.rx.AppSchedulers
import com.google.zxing.integration.android.IntentResult
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Single
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.dialog.Dialog_Choice_Type_OKex
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class PrivateKeyRestorePresenter @Inject constructor(
    private val accountsInteractor: AccountsInteractor,
    private val secretInteractor: SecretInteractor,
    private val clipboardInteractor: ClipboardInteractor
) : BaseMoxyPresenter<PrivateKeyRestoreMoxyView>() {

    lateinit var chain: BaseChain

    private var address = ""
    private var privateKey = ""
    private var customPath = -1
    private var accountId = -1L

    fun onCancelClicked() {
        viewState.finish()
    }

    fun onNextClicked(userInput: String) {
        this.privateKey = userInput
        when {
            !WalletUtils.isValidStringPrivateKey(userInput) -> {
                viewState.showMessage(R.string.error_invalid_private_key)
            }
            chain == BaseChain.OKEX_MAIN -> {
                val dialog = Dialog_Choice_Type_OKex.newInstance()
                viewState.showDialog(dialog, "dialog", false)
            }
            else -> {
                onAddressFetched(
                    MnemonicUtils.createAddress(chain, userInput)
                )
            }
        }
    }

    fun onPasteClicked() {
        clipboardInteractor
            .getClip()
            .subscribeOn(AppSchedulers.ui())
            .observeOn(AppSchedulers.ui())
            .compositeSubscribe(
                onSuccess = { data ->
                    if (data.isNotEmpty()) {
                        viewState.setPrivateKey(data)
                    } else {
                        viewState.showMessage(R.string.error_clipboard_no_data)
                    }
                }
            )
    }

    fun onScanQRResultsReceived(result: IntentResult) {
        result
            .contents
            ?.trim()
            ?.takeIf(String::isNotEmpty)
            ?.let(viewState::setPrivateKey)
    }

    fun onCheckPasswordSuccessfully() {
        if (accountId == -1L) {
            accountsInteractor.createAccount(
                chain,
                address,
                privateKey,
                customPath
            )
        } else {
            accountsInteractor.updateAccount(
                accountId,
                chain.chainName,
                address,
                privateKey,
                customPath
            )
        }
            .withDefaults()
            .compositeSubscribe(
                onSuccess = viewState::showMainActivity
            )
    }

    fun onCheckOecAddressType(okAddressType: Int, userInput: String) {
        this.customPath = okAddressType
        this.privateKey = userInput
        onAddressFetched(
            MnemonicUtils.createAddress(chain, userInput, customPath)
        )
    }


    private fun onAddressFetched(address: String) {
        if (address.isEmpty()) {
            viewState.showMessage(R.string.error_invalid_private_key)
        } else {
            this.address = address
            accountsInteractor
                .getAccount(chain, address)
                .flatMap { account ->
                    if (account.hasPrivateKey) {
                        Single.error(DuplicateAccountException())
                    } else {
                        account.id.toSingle()
                    }
                }
                .onErrorReturnItem(-1L)
                .withDefaults()
                .compositeSubscribe(
                    onSuccess = { accountId ->
                        this.accountId = accountId
                        checkPassword()
                    },
                    onError = object : OnErrorConsumer() {
                        override fun onError(error: Throwable) {
                            if (error is DuplicateAccountException) {
                                viewState.showMessage(R.string.error_already_imported_address)
                            } else {
                                super.onError(error)
                            }
                        }
                    }
                )
        }
    }

    private fun checkPassword() {
        secretInteractor
            .hasPassword()
            .withDefaults()
            .compositeSubscribe(
                onSuccess = { hasPassword ->
                    if (hasPassword) {
                        viewState.requestCheckPassword()
                    } else {
                        viewState.requestCreatePassword()
                    }
                }
            )
    }
}
