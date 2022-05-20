package com.fulldive.wallet.interactors.chains.okex

import android.text.SpannableString
import com.fulldive.wallet.di.modules.DefaultRepositoryModule
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.models.WalletBalance
import com.joom.lightsaber.ProvidedBy
import io.reactivex.Completable
import io.reactivex.Single
import wannabit.io.cosmostaion.dao.Account
import wannabit.io.cosmostaion.network.res.ResNodeInfo
import wannabit.io.cosmostaion.network.res.ResOkAccountInfo
import wannabit.io.cosmostaion.network.res.ResOkAccountToken
import wannabit.io.cosmostaion.utils.PriceProvider
import javax.inject.Inject

@ProvidedBy(DefaultRepositoryModule::class)
class OkexRepository @Inject constructor(
    private val okexLocalSource: OkexLocalSource,
    private val okexRemoteSource: OkexRemoteSource
) {

    fun requestAccount(address: String): Single<ResOkAccountInfo> {
        return okexRemoteSource.requestAccount(address)
    }

    fun updateValidatorsList(): Completable {
        return okexRemoteSource
            .requestValidatorsList()
            .flatMapCompletable(okexLocalSource::setValidators)
    }

    fun updateTickersList(): Completable {
        return okexRemoteSource
            .requestTickersList()
            .flatMapCompletable(okexLocalSource::setTickers)
    }

    fun updateTokensList(): Completable {
        return okexRemoteSource
            .requestTokensList()
            .flatMapCompletable(okexLocalSource::setTokens)
    }

    fun updateUnbonding(account: Account): Completable {
        return okexRemoteSource
            .requestUnbonding(account.address)
            .flatMapCompletable(okexLocalSource::setUnbonding)
    }

    fun updateStakingInfo(account: Account): Completable {
        return okexRemoteSource
            .requestStakingInfo(account.address)
            .flatMapCompletable(okexLocalSource::setStakingInfo)
    }

    fun requestNodeInfo(): Single<ResNodeInfo> {
        return okexRemoteSource.requestNodeInfo()
    }

    fun requestAccountBalance(address: String): Single<ResOkAccountToken> {
        return okexRemoteSource.requestAccountBalance(address)
    }

    fun setNodeInfo(nodeInfo: ResNodeInfo): Completable {
        return okexLocalSource.setNodeInfo(nodeInfo)
    }

    fun getOkAmount(
        currency: Currency,
        balance: WalletBalance,
        priceProvider: PriceProvider
    ): SpannableString {
        return okexLocalSource.getOkAmount(currency, balance, priceProvider)
    }
}
