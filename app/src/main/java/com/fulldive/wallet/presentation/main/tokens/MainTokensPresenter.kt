package com.fulldive.wallet.presentation.main.tokens

import android.text.SpannableString
import android.util.Log
import com.fulldive.wallet.di.modules.DefaultPresentersModule
import com.fulldive.wallet.extensions.or
import com.fulldive.wallet.extensions.withDefaults
import com.fulldive.wallet.interactors.accounts.AccountsInteractor
import com.fulldive.wallet.interactors.balances.BalancesInteractor
import com.fulldive.wallet.interactors.chains.StationInteractor
import com.fulldive.wallet.interactors.chains.binance.BinanceInteractor
import com.fulldive.wallet.interactors.chains.grpc.GrpcInteractor
import com.fulldive.wallet.interactors.chains.okex.OkexInteractor
import com.fulldive.wallet.interactors.settings.SettingsInteractor
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.models.WalletBalance
import com.fulldive.wallet.presentation.base.BaseMoxyPresenter
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.base.BaseConstant
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.utils.WDp
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

@ProvidedBy(DefaultPresentersModule::class)
class MainTokensPresenter @Inject constructor(
    private val accountsInteractor: AccountsInteractor,
    private val balancesInteractor: BalancesInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val binanceInteractor: BinanceInteractor,
    private val okexInteractor: OkexInteractor,
    private val grpcInteractor: GrpcInteractor,
    private val stationInteractor: StationInteractor,
) : BaseMoxyPresenter<MainTokensMoxyView>() {

    private var account: Account? = null
    private val baseChain: BaseChain?
        get() {
            return account?.baseChain?.let(BaseChain::getChain)
        }
    private var balances: List<WalletBalance> = emptyList()
    private var currency: Currency = Currency.getDefault()

    private var balanceDisposable: Disposable? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        accountsInteractor
            .observeCurrentAccount()
            .distinctUntilChanged()
            .withDefaults()
            .compositeSubscribe(
                onNext = { account ->
                    this.account = account
                    observeBalance(account)
                    baseChain?.let { chain ->
                        viewState.showAddress(
                            account.address,
                            chain.chainBackground,
                            if (account.hasPrivateKey) chain.chainColor else R.color.colorGray0
                        )
                        viewState.showAccount(account, chain)
                        Log.d("fftf", "showAccount: $account, $chain")
                        updateItems()
                        request(account, chain)
                    }
                }
            )
        settingsInteractor
            .observeCurrency()
            .withDefaults()
            .compositeSubscribe(
                onNext = { currency ->
                    this.currency = currency
                    viewState.setCurrency(currency)
                    Log.d("fftf", "setCurrency: $currency")
                    updateItems()
                }
            )
    }

    fun onAddressClicked() {
        account?.let(viewState::showAddressDialog)
    }

    fun onRefresh() {
        account?.let { account ->
            baseChain?.let { chain ->
                request(account, chain)
            }
        }
    }

    private fun observeBalance(account: Account) {
        balanceDisposable?.dispose()
        balanceDisposable = balancesInteractor
            .observeBalances(account.id)
            .withDefaults()
            .compositeSubscribe(
                onNext = { balances ->
                    this.balances = balances
                    viewState.setBalances(balances)
                    updateItems()
                }
            )
    }

    private fun request(account: Account, chain: BaseChain) {
        Completable
            .mergeArray(
                stationInteractor.updatePrices(chain),
                when {
                    chain.isGRPC -> grpcInteractor.update(account, chain)
                    chain == BaseChain.BNB_MAIN -> binanceInteractor.update(account)
                    chain == BaseChain.OKEX_MAIN -> okexInteractor.update(account)
                    else -> Completable.error(IllegalStateException())
                },
                balancesInteractor
                    .requestBalances(chain, account.address, account.id)
                    .ignoreElement()
            )
            .withDefaults()
            .doAfterTerminate {
                viewState.hideProgress()
            }
            .compositeSubscribe(onSuccess = {
                Log.d("fftf", "request finished")
            })
    }

    private fun updateItems() {
        Log.d("fftf", "updateItems: $balances")
        Log.d("fftf", "updateItems.baseChain: $baseChain")
        val chain = baseChain ?: return
        val mainDenom = chain.mainDenom

        val items = balances.mapNotNull { balance ->
            when {
                balance.denom.equals(mainDenom, ignoreCase = true) -> fetchNativeCoin(
                    chain,
                    balance
                )
                else -> null
            }
        }
        Log.d("fftf", "updateItems, items: $items")

        viewState.showItems(items)

//        baseChain?.let { chain ->
//            WDp.allAssetToUserCurrency(
//                chain,
//                currency,
//                mainActivity?.baseDao,
//                balances,
//                priceProvider
//            )
//        }
    }

    private fun fetchNativeCoin(baseChain: BaseChain, balance: WalletBalance): TokenWrappedItem? {
        Log.d("fftf", "fetchNativeCoin, chain: $baseChain, balance: $balance")
        var result: TokenWrappedItem? = null

        val balanceDenom = balance.denom

        when (baseChain) {
            BaseChain.BNB_MAIN -> {
                Log.d("fftf", "aaa")
                val bnbToken = balancesInteractor.getBnbToken(balanceDenom)
                if (bnbToken != null) {
                    Log.d("fftf", "bbb")
                    val amount = balance.getTotalAmount().or(BigDecimal.ZERO)
                    Log.d("fftf", "ccc")
                    val title = bnbToken.original_symbol.uppercase(Locale.getDefault())
                    Log.d("fftf", "title: $title")
                    val balanceText = WDp.getDpAmount2(amount, 0, 6)
                    Log.d("fftf", "balanceText: $balanceText")
                    val valueText = binanceInteractor.getBnbAmount(currency, balanceDenom, amount)
                    Log.d("fftf", "valueText: $valueText")
                    result = TokenWrappedItem(
                        id = balanceDenom,
                        section = Section.SECTION_NATIVE,
                        title = title,
                        titleRes = 0,
                        hint = "(${bnbToken.symbol})",
                        description = bnbToken.name,
                        iconUrl = bnbToken.iconUrl,
                        iconResId = R.drawable.token_ic,
                        titleColorRes = R.color.colorWhite,
                        balance = balanceText,
                        value = valueText
                    )
                    Log.d("fftf", "ddd")
                }
            }
            BaseChain.OKEX_MAIN -> {
                val okToken = balancesInteractor.getOkToken(balanceDenom)
                if (okToken != null) {

                    val amount = balance.getDelegatableAmount()
                        .add(balancesInteractor.getAllExToken(balanceDenom))

                    result = TokenWrappedItem(
                        id = balanceDenom,
                        section = Section.SECTION_NATIVE,
                        title = okToken.original_symbol.uppercase(Locale.getDefault()),
                        titleRes = 0,
                        hint = "(${okToken.symbol})",
                        description = okToken.description,
                        iconUrl = okToken.iconUrl,
                        iconResId = R.drawable.token_ic,
                        titleColorRes = R.color.colorWhite,
                        balance = WDp.getDpAmount2(amount, 0, 6),
                        value = okexInteractor.getOkAmount(currency, balance)
                    )
                }
            }
            else -> {
                val chain = BaseChain.getChainByDenom(balanceDenom)
                var title: String = ""
                var titleRes = 0
                var descrition: String = ""
                var hint: String = ""
                var titleColorRes: Int = R.color.colorWhite
                var iconResId: Int = R.drawable.token_ic
                var iconUrl: String = ""
                var valueAmount: SpannableString = SpannableString.valueOf("")

                val amount: BigDecimal
                var divideDecimal = 6
                var displayDecimal = 6

                when {
                    chain != null -> {
                        titleRes = chain.symbolTitle
                        titleColorRes = chain.chainColor
                        descrition = chain.fullNameCoin
                        if (balance.denom == BaseChain.SECRET_MAIN.mainDenom) {
                            hint = "(${balance.denom})"
                        }
                        iconResId = chain.coinIcon
                        amount = balance.getBalanceAmount()
                            .add(balancesInteractor.getAllMainAsset(balance.denom)) //TODO: add vesting
                        divideDecimal = chain.divideDecimal
                        displayDecimal = chain.displayDecimal

                        valueAmount = balancesInteractor.getTokenAmount(chain, currency, balance)
                    }
                    balanceDenom == BaseConstant.TOKEN_ION -> {
                        titleRes = R.string.str_uion_c
                        titleColorRes = R.color.colorIon
                        descrition = "Ion Coin"
                        iconResId = R.drawable.token_ion
                        amount = balance.getBalanceAmount()
                    }
                    balanceDenom == BaseConstant.TOKEN_HARD -> {
                        title = balanceDenom.uppercase(Locale.getDefault())
                        titleColorRes = R.color.colorHard
                        descrition = "HardPool Gov. Coin"
                        iconUrl = BaseConstant.KAVA_COIN_IMG_URL + balanceDenom + ".png"
                        amount =
                            balance.getBalanceAmount() // .add(baseData.getVesting(balance.getSymbol()));
                        divideDecimal = balancesInteractor.getKavaCoinDecimal(balanceDenom)
                    }
                    balanceDenom == BaseConstant.TOKEN_USDX -> {
                        title = balanceDenom.uppercase(Locale.getDefault())
                        titleColorRes = R.color.colorUsdx
                        descrition = "USD Stable Asset"
                        iconUrl = BaseConstant.KAVA_COIN_IMG_URL + balanceDenom + ".png"
                        amount =
                            balance.getBalanceAmount() // .add(baseData.getVesting(balance.getSymbol()));
                    }
                    balanceDenom == BaseConstant.TOKEN_SWP -> {
                        title = balanceDenom.uppercase(Locale.getDefault())
                        titleColorRes = R.color.colorSwp
                        descrition = "Kava Swap Coin"
                        iconUrl = BaseConstant.KAVA_COIN_IMG_URL + balanceDenom + ".png"
                        amount =
                            balance.getBalanceAmount() // .add(baseData.getVesting(balance.getSymbol()));
                    }
                    balanceDenom.startsWith("e") -> {
                        title = balanceDenom.uppercase(Locale.getDefault())
                        titleColorRes = R.color.colorWhite
                        descrition = balanceDenom.substring(1)
                            .uppercase(Locale.getDefault()) + " on E-Money Network"
                        iconUrl = BaseConstant.EMONEY_COIN_IMG_URL + balanceDenom + ".png"
                        amount = balance.getBalanceAmount()
                    }
                    else -> {
                        amount = balance.getBalanceAmount()
                            .add(balancesInteractor.getAllMainAsset(balance.denom)) //TODO: add vesting
                    }
                }

                result = TokenWrappedItem(
                    id = balanceDenom,
                    section = Section.SECTION_NATIVE,
                    title = title,
                    titleRes = titleRes,
                    hint = hint,
                    description = descrition,
                    iconUrl = iconUrl,
                    iconResId = iconResId,
                    titleColorRes = titleColorRes,
                    balance = WDp.getDpAmount2(amount, divideDecimal, displayDecimal),
                    value = valueAmount
                )
            }
        }
        Log.d("fftf", "eeee: $result")
        return result
    }
}
