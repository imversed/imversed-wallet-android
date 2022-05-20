package com.fulldive.wallet.presentation.main.tokens

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fulldive.wallet.extensions.getColorCompat
import com.fulldive.wallet.models.BaseChain
import com.fulldive.wallet.models.BaseChain.*
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.models.WalletBalance
import com.fulldive.wallet.presentation.accounts.AccountShowDialogFragment
import com.fulldive.wallet.presentation.base.BaseMvpFragment
import com.fulldive.wallet.presentation.main.MainActivity
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.base.BaseConstant
import wannabit.io.cosmostaion.base.IRefreshTabListener
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.dao.Cw20Assets
import wannabit.io.cosmostaion.databinding.FragmentMainTokensBinding
import wannabit.io.cosmostaion.utils.PriceProvider
import wannabit.io.cosmostaion.utils.WDp
import wannabit.io.cosmostaion.utils.WUtil

class MainTokensFragment : BaseMvpFragment<FragmentMainTokensBinding>(),
    MainTokensMoxyView,
    IRefreshTabListener {
    private var balances: List<WalletBalance> = emptyList()
    private var currency: Currency = Currency.getDefault()

    private var adapter: MainTokensAdapter? = null
    private var recyclerViewHeader: RecyclerViewHeader? = null

    private val ibcAuthedItems = ArrayList<WalletBalance>()
    private val osmosisPoolItems = ArrayList<WalletBalance>()
    private val etherItems = ArrayList<WalletBalance>()
    private val ibcUnknownItems = ArrayList<WalletBalance>()
    private val gravityDexItems = ArrayList<WalletBalance>()
    private val injectivePoolItems = ArrayList<WalletBalance>()
    private val kavaBep2Items = ArrayList<WalletBalance>()
    private val nativeItems = ArrayList<WalletBalance>()
    private val etcItems = ArrayList<WalletBalance>()
    private val unknownItems = ArrayList<WalletBalance>()
    private val CW20Items = ArrayList<Cw20Assets>()

    private var account: Account? = null
    private var baseChain: BaseChain? = null

    private var priceProvider: PriceProvider? = null

    private val mainActivity: MainActivity?
        get() = getBaseActivity() as? MainActivity

    private val presenter by moxyPresenter {
        getInjector().getInstance<MainTokensPresenter>()
    }

    override fun getViewBinding() = FragmentMainTokensBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        priceProvider = PriceProvider { denom: String? -> mainActivity?.getPrice(denom) }
        setHasOptionsMenu(true)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding {
            swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(
                    view.context,
                    R.color.colorPrimary
                )
            )
            swipeRefreshLayout.setOnRefreshListener {
                presenter.onRefresh()
            }
            recyclerView.setOnTouchListener { _, _ -> swipeRefreshLayout.isRefreshing }
            recyclerView.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            recyclerView.setHasFixedSize(true)
            val adapter = MainTokensAdapter { item ->

            }
//            adapter.itemsClickListeners = object : TokensAdapter.OnItemsClickListeners {
//                override fun onStackingTokenClicked(denom: String) {
//                    val intent = Intent(mainActivity, StakingTokenGrpcActivity::class.java)
//                    intent.putExtra("denom", denom)
//                    startActivity(intent)
//                }
//
//                override fun onNativeTokenClicked(denom: String) {
//                    val intent = Intent(mainActivity, NativeTokenGrpcActivity::class.java)
//                    intent.putExtra("denom", denom)
//                    startActivity(intent)
//                }
//
//                override fun onIbcTokenClicked(denom: String) {
//                    val intent = Intent(mainActivity, IBCTokenDetailActivity::class.java)
//                    intent.putExtra("denom", denom)
//                    startActivity(intent)
//                }
//
//                override fun onOsmoPoolTokenClicked(denom: String) {
//                    val intent = Intent(mainActivity, POOLTokenDetailActivity::class.java)
//                    intent.putExtra("denom", denom)
//                    startActivity(intent)
//                }
//
//                override fun onErcTokenClicked(denom: String) {
//                    val intent = Intent(mainActivity, BridgeTokenGrpcActivity::class.java)
//                    intent.putExtra("denom", denom)
//                    startActivity(intent)
//                }
//
//                override fun onCW20TokenClicked(cw20Asset: Cw20Assets) {
//                    val intent = Intent(mainActivity, ContractTokenGrpcActivity::class.java)
//                    intent.putExtra("cw20Asset", cw20Asset)
//                    startActivity(intent)
//                }
//
//                override fun onNativeStackingTokenClicked() {
//                    val intent = Intent(mainActivity, StakingTokenDetailActivity::class.java)
//                    startActivity(intent)
//                }
//
//                override fun onEtcTokenClicked(denom: String) {
//                    val intent = Intent(mainActivity, NativeTokenDetailActivity::class.java)
//                    intent.putExtra("denom", denom)
//                    startActivity(intent)
//                }
//            }
            this@MainTokensFragment.adapter = adapter
            recyclerView.adapter = adapter
            recyclerViewHeader = RecyclerViewHeader(
                mainActivity, true, sectionGrpcCall
            )
            recyclerView.addItemDecoration(recyclerViewHeader!!)

            addressTextView.setOnClickListener {
                presenter.onAddressClicked()
            }
        }
    }

    override fun onDestroy() {
        priceProvider = null
        super.onDestroy()
    }

    override fun showAddress(address: String, @ColorRes background: Int, @ColorRes keyColor: Int) {
        binding {
            addressTextView.text = address
            cardView.setCardBackgroundColor(cardView.getColorCompat(background))
            keyStatusImageView.setColorFilter(
                ContextCompat.getColor(keyStatusImageView.context, keyColor),
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    override fun showAccount(account: Account, chain: BaseChain) {
        this.account = account
        this.baseChain = chain

        recyclerViewHeader?.baseChain = chain
        updateBalance()
    }

    override fun setCurrency(currency: Currency) {
        this.currency = currency
        updateBalance()
    }

    override fun setBalances(balances: List<WalletBalance>) {
        this.balances = balances
        updateBalance()
    }

    override fun showItems(items: List<TokenWrappedItem>) {
        adapter?.setItems(items)
        binding {
            if (items.isNotEmpty()) {
                emptyLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            } else {
                emptyLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        }
    }

    override fun hideProgress() {
        binding {
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun showAddressDialog(account: Account) {
        AccountShowDialogFragment
            .newInstance(
                account.getAccountTitle(requireContext()),
                account.address
            )
            .let(::showDialog)
    }

    private val sectionGrpcCall: SectionCallback
        get() = object : SectionCallback {
            override fun isSection(baseChain: BaseChain, position: Int): Boolean {
                return if (baseChain == OSMOSIS_MAIN) {
                    position == 0 || position == nativeItems.size || position == nativeItems.size + ibcAuthedItems.size || position == nativeItems.size + ibcAuthedItems.size + osmosisPoolItems.size || position == nativeItems.size + ibcAuthedItems.size + osmosisPoolItems.size + ibcUnknownItems.size
                } else if (baseChain == COSMOS_MAIN) {
                    position == 0 || position == nativeItems.size || position == nativeItems.size + ibcAuthedItems.size || position == nativeItems.size + ibcAuthedItems.size + gravityDexItems.size || position == nativeItems.size + ibcAuthedItems.size + gravityDexItems.size + ibcUnknownItems.size
                } else if (baseChain == SIF_MAIN || baseChain == GRABRIDGE_MAIN) {
                    position == 0 || position == nativeItems.size || position == nativeItems.size + ibcAuthedItems.size || position == nativeItems.size + ibcAuthedItems.size + etherItems.size || position == nativeItems.size + ibcAuthedItems.size + etherItems.size + ibcUnknownItems.size
                } else if (baseChain == INJ_MAIN) {
                    position == 0 || position == nativeItems.size || position == nativeItems.size + ibcAuthedItems.size || position == nativeItems.size + ibcAuthedItems.size + etherItems.size || position == nativeItems.size + ibcAuthedItems.size + etherItems.size + injectivePoolItems.size || position == nativeItems.size + ibcAuthedItems.size + etherItems.size + injectivePoolItems.size + ibcUnknownItems.size
                } else if (baseChain == KAVA_MAIN) {
                    position == 0 || position == nativeItems.size || position == nativeItems.size + ibcAuthedItems.size || position == nativeItems.size + ibcAuthedItems.size + kavaBep2Items.size || position == nativeItems.size + ibcAuthedItems.size + kavaBep2Items.size + etcItems.size || position == nativeItems.size + ibcAuthedItems.size + kavaBep2Items.size + etcItems.size + ibcUnknownItems.size
                } else if (baseChain == JUNO_MAIN) {
                    position == 0 || position == nativeItems.size || position == nativeItems.size + ibcAuthedItems.size || position == nativeItems.size + ibcAuthedItems.size + CW20Items.size || position == nativeItems.size + ibcAuthedItems.size + CW20Items.size + ibcUnknownItems.size
                } else if (baseChain.isGRPC) {
                    position == 0 || position == nativeItems.size || position == nativeItems.size + ibcAuthedItems.size || position == nativeItems.size + ibcAuthedItems.size + ibcUnknownItems.size
                } else {
                    position == 0 || position == nativeItems.size || position == nativeItems.size + etcItems.size
                }
            }

            override fun getSectionHeader(section: Int): Int {
                return when (section) {
                    SECTION_NATIVE -> Section.SECTION_NATIVE.titleResId
                    SECTION_IBC_AUTHED_GRPC -> Section.SECTION_IBC_AUTHED_GRPC.titleResId
                    SECTION_IBC_UNKNOWN_GRPC -> Section.SECTION_IBC_UNKNOWN_GRPC.titleResId
                    SECTION_OSMOSIS_POOL_GRPC -> Section.SECTION_OSMOSIS_POOL_GRPC.titleResId
                    SECTION_INJECTIVE_POOL_GRPC -> Section.SECTION_INJECTIVE_POOL_GRPC.titleResId
                    SECTION_GRAVICTY_DEX_GRPC -> Section.SECTION_GRAVICTY_DEX_GRPC.titleResId
                    SECTION_KAVA_BEP2_GRPC -> Section.SECTION_KAVA_BEP2_GRPC.titleResId
                    SECTION_ETC -> Section.SECTION_ETC.titleResId
                    SECTION_OKEX_ETC -> Section.SECTION_OKEX_ETC.titleResId
                    SECTION_CW20_GRPC -> Section.SECTION_CW20_GRPC.titleResId
                    else -> Section.SECTION_UNKNOWN.titleResId
                }
            }

            override fun getSectionCount(section: Int): Int {
                return when (section) {
                    SECTION_NATIVE -> nativeItems.size
                    SECTION_IBC_AUTHED_GRPC -> ibcAuthedItems.size
                    SECTION_IBC_UNKNOWN_GRPC -> ibcUnknownItems.size
                    SECTION_UNKNOWN -> unknownItems.size
                    SECTION_OSMOSIS_POOL_GRPC -> osmosisPoolItems.size
                    SECTION_INJECTIVE_POOL_GRPC -> injectivePoolItems.size
                    SECTION_ETHER_GRPC -> etherItems.size
                    SECTION_GRAVICTY_DEX_GRPC -> gravityDexItems.size
                    SECTION_KAVA_BEP2_GRPC -> kavaBep2Items.size
                    SECTION_ETC, SECTION_OKEX_ETC -> etcItems.size
                    SECTION_CW20_GRPC -> CW20Items.size
                    else -> 0
                }
            }
        }

    override fun onRefreshTab() {
        presenter.onRefresh()
    }

    private fun onUpdateView() {
        val mainDenom = baseChain!!.mainDenom
        ibcAuthedItems.clear()
        osmosisPoolItems.clear()
        etherItems.clear()
        ibcUnknownItems.clear()
        gravityDexItems.clear()
        injectivePoolItems.clear()
        kavaBep2Items.clear()
        nativeItems.clear()
        etcItems.clear()
        CW20Items.clear()
        CW20Items.addAll(mainActivity?.baseDao?.getCw20sGrpc(baseChain).orEmpty())

        for (balance in balances) {
            if (balance.denom.equals(mainDenom, ignoreCase = true)) {
                nativeItems.add(balance)
            } else if (baseChain == BNB_MAIN || baseChain == OKEX_MAIN) {
                etcItems.add(balance)
            } else if (balance.isIbc()) {
                val ibcToken = mainActivity?.baseDao?.getIbcToken(balance.getIbcHash())
                if (ibcToken != null && ibcToken.auth) {
                    ibcAuthedItems.add(balance)
                } else {
                    ibcUnknownItems.add(balance)
                }
            } else if (baseChain == OSMOSIS_MAIN && balance.osmosisAmm()) {
                osmosisPoolItems.add(balance)
            } else if (baseChain == OSMOSIS_MAIN && balance.denom.equals(
                    BaseConstant.TOKEN_ION,
                    ignoreCase = true
                ) ||
                baseChain == EMONEY_MAIN && balance.denom.startsWith("e")
            ) {
                nativeItems.add(balance)
            } else if (baseChain == SIF_MAIN && balance.denom.startsWith("c") || baseChain == GRABRIDGE_MAIN && balance.denom.startsWith(
                    "gravity"
                ) || baseChain == INJ_MAIN && balance.denom.startsWith("peggy")
            ) {
                etherItems.add(balance)
            } else if (baseChain == COSMOS_MAIN && balance.denom.startsWith("pool")) {
                gravityDexItems.add(balance)
            } else if (baseChain == INJ_MAIN && balance.denom.startsWith("share")) {
                injectivePoolItems.add(balance)
            } else if (baseChain == KAVA_MAIN) {
                if (balance.denom == BaseConstant.TOKEN_HARD || balance.denom.equals(
                        BaseConstant.TOKEN_USDX,
                        ignoreCase = true
                    ) || balance.denom.equals(BaseConstant.TOKEN_SWP, ignoreCase = true)
                ) {
                    nativeItems.add(balance)
                } else if (balance.denom.equals(
                        BaseConstant.TOKEN_HTLC_KAVA_BNB,
                        ignoreCase = true
                    ) || balance.denom.equals(
                        BaseConstant.TOKEN_HTLC_KAVA_BTCB,
                        ignoreCase = true
                    ) ||
                    balance.denom.equals(
                        BaseConstant.TOKEN_HTLC_KAVA_XRPB,
                        ignoreCase = true
                    ) || balance.denom.equals(BaseConstant.TOKEN_HTLC_KAVA_BUSD, ignoreCase = true)
                ) {
                    kavaBep2Items.add(balance)
                } else if (balance.denom.equals(
                        "btch",
                        ignoreCase = true
                    ) || balance.denom.equals("hbtc", ignoreCase = true)
                ) {
                    etcItems.add(balance)
                }
            } else {
                unknownItems.add(balance)
            }
        }
        if (baseChain!!.isGRPC) {
            WUtil.onSortingBalance(nativeItems, baseChain)
            WUtil.onSortingGravityPool(gravityDexItems, mainActivity?.baseDao)
            WUtil.onSortingOsmosisPool(osmosisPoolItems)
            WUtil.onSortingInjectivePool(injectivePoolItems)
        } else if (baseChain == BNB_MAIN || baseChain == OKEX_MAIN) {
            WUtil.onSortingNativeCoins(etcItems, baseChain)
        } else {
            WUtil.onSortingNativeCoins(nativeItems, baseChain)
        }
        binding {
            if (balances.isNotEmpty()) {
//                adapter!!.balances = balances
//                adapter!!.ibcAuthedItems = ibcAuthedItems
//                adapter!!.poolItems = osmosisPoolItems
//                adapter!!.etherItems = etherItems
//                adapter!!.ibcUnknownItems = ibcUnknownItems
//                adapter!!.gravityDexItems = gravityDexItems
//                adapter!!.injectivePoolItems = injectivePoolItems
//                adapter!!.kavaBep2Items = kavaBep2Items
//                adapter!!.nativeItems = nativeItems
//                adapter!!.etcItems = etcItems
//                adapter!!.unknownItems = unknownItems
//                adapter!!.CW20Items = CW20Items
                adapter!!.notifyDataSetChanged()
                emptyLayout.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            } else {
                emptyLayout.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        }
    }

    private fun updateBalance() {
        binding?.totalValueTextView?.text = currency.format(
            WDp.allAssetToUserCurrency(
                baseChain,
                currency,
                mainActivity?.baseDao,
                balances,
                priceProvider
            )
        )
    }

    companion object {
        const val SECTION_NATIVE = 0
        const val SECTION_IBC_AUTHED_GRPC = 1
        const val SECTION_OSMOSIS_POOL_GRPC = 2
        const val SECTION_ETHER_GRPC = 3
        const val SECTION_IBC_UNKNOWN_GRPC = 4
        const val SECTION_GRAVICTY_DEX_GRPC = 5
        const val SECTION_INJECTIVE_POOL_GRPC = 6
        const val SECTION_KAVA_BEP2_GRPC = 7
        const val SECTION_ETC = 8
        const val SECTION_OKEX_ETC = 9
        const val SECTION_CW20_GRPC = 10
        const val SECTION_UNKNOWN = 11

        fun newInstance() = MainTokensFragment()
    }
}