package com.fulldive.wallet.presentation.accounts.restore

import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.getPathString
import com.fulldive.wallet.extensions.or
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.interactors.chains.binance.BinanceInteractor
import com.fulldive.wallet.interactors.chains.grpc.GrpcInteractor
import com.fulldive.wallet.interactors.chains.okex.OkexInteractor
import com.fulldive.wallet.interactors.secret.MnemonicUtils
import com.fulldive.wallet.interactors.secret.SecretInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Single
import wannabit.io.cosmostaion.R
import java.math.BigDecimal
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class RestorePathPresenter @Inject constructor(
    private val accountsInteractor: AccountsInteractor,
    private val secretInteractor: SecretInteractor,
    private val binanceInteractor: BinanceInteractor,
    private val okexInteractor: OkexInteractor,
    private val grpcInteractor: GrpcInteractor
) : BaseMoxyPresenter<RestorePathMoxyView>() {
    lateinit var chain: BaseChain
    var entropy: String = ""
    var customPath: Int = 0
    var mnemonicSize: Int = MnemonicUtils.MNEMONIC_WORDS_COUNT

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        generateItems()
    }

    fun onItemClicked(item: WalletItem) {
        when (item.state) {
            WalletState.ImportedState -> viewState.showMessage(R.string.str_already_imported_key)
            WalletState.OverrideState -> overrideAccount(item)
            WalletState.ReadyState -> createAccount(item)
        }
    }

    private fun createAccount(item: WalletItem) {
        viewState.showWaitDialog()
        accountsInteractor
            .createAccount(
                chain.chainName,
                item.address,
                entropy,
                item.path,
                customPath,
                mnemonicSize
            )
            .withDefaults()
            .doAfterTerminate {
                viewState.hideWaitDialog()
            }
            .compositeSubscribe(
                onSuccess = viewState::showMainActivity
            )
    }

    private fun overrideAccount(item: WalletItem) {
        viewState.showWaitDialog()

        accountsInteractor
            .updateAccount(
                item.accountId,
                item.address,
                entropy,
                item.path,
                customPath,
                mnemonicSize
            )
            .withDefaults()
            .doAfterTerminate {
                viewState.hideWaitDialog()
            }
            .compositeSubscribe(
                onSuccess = viewState::showMainActivity
            )
    }

    private fun generateItems() {
        viewState.showWaitDialog()
        val walletsGenerator = safeSingle {
            (0 until MAX_PATH_COUNT)
                .map { index ->
                    WalletItem(
                        MnemonicUtils.createAddress(chain, entropy, index, customPath),
                        index,
                        chain.getPathString(index, customPath),
                        chain.symbolTitle,
                        chain.chainColor,
                        chain.chainBackground,
                        WalletState.ReadyState,
                        BigDecimal.ZERO,
                        chain.divideDecimal,
                        chain.displayDecimal
                    )
                }
        }
            .flatMap { items ->
                Single
                    .concat(
                        items.map { item ->
                            accountsInteractor
                                .getAccount(chain, item.address)
                                .map { account ->
                                    if (account.hasPrivateKey) {
                                        item.copy(
                                            state = WalletState.ImportedState,
                                            chainBackground = R.color.colorTransBg
                                        )
                                    } else {
                                        item.copy(
                                            state = WalletState.OverrideState,
                                            accountId = account.id
                                        )
                                    }
                                }
                                .onErrorReturnItem(item)
                        }
                    )
                    .toList()
                    .onErrorReturnItem(items)
            }

        walletsGenerator
            .withDefaults()
            .compositeSubscribe(
                onSuccess = { items ->
                    viewState.showItems(items)
                    viewState.hideWaitDialog()
                }
            )

        walletsGenerator
            .flatMap { items ->
                Single
                    .concat(
                        items.map { item ->
                            when (chain) {
                                BaseChain.BNB_MAIN -> binanceInteractor.getBalances(item.address)
                                BaseChain.OKEX_MAIN -> okexInteractor.getBalances(item.address)
                                else -> grpcInteractor.getBalances(chain, item.address)
                            }
                                .map { balances ->
                                    balances
                                        .find { balance ->
                                            balance.symbol == chain.mainDenom
                                        }
                                        ?.let { balance ->
                                            item.copy(
                                                amount = balance.balance
                                            )
                                        }
                                        .or(item)
                                }
                                .onErrorReturnItem(item)
                        }
                    )
                    .toList()
                    .onErrorReturnItem(items)
            }
            .withDefaults()
            .compositeSubscribe(
                onSuccess = { items ->
                    viewState.showItems(items)
                }
            )
    }

    companion object {
        private const val MAX_PATH_COUNT = 5
    }
}
