package com.fulldive.wallet.interactors.chains.grpc

import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.completeCallable
import com.fulldive.wallet.extensions.safeCompletable
import com.fulldive.wallet.extensions.singleCallable
import com.fulldive.wallet.models.BaseChain
import com.google.protobuf2.Any
import com.joom.lightsaber.ProvidedBy
import cosmos.distribution.v1beta1.Distribution
import cosmos.staking.v1beta1.Staking
import io.reactivex.Completable
import io.reactivex.Single
import osmosis.gamm.poolmodels.balancer.BalancerPool
import tendermint.liquidity.v1beta1.Liquidity
import tendermint.p2p.Types
import wannabit.io.cosmostaion.base.BaseData
import wannabit.io.cosmostaion.dao.Assets
import wannabit.io.cosmostaion.dao.Balance
import wannabit.io.cosmostaion.dao.Cw20Assets
import wannabit.io.cosmostaion.model.kava.IncentiveParam
import wannabit.io.cosmostaion.model.kava.IncentiveReward
import wannabit.io.cosmostaion.model.type.Coin
import javax.inject.Inject

@ProvidedBy(DefaultLocalStorageModule::class)
class GrpcLocalSource @Inject constructor(
    private val baseData: BaseData
) {

    fun setNodeInfo(chain: BaseChain, nodeInfo: Types.NodeInfo): Completable {
        return safeCompletable {
            baseData.mGRpcNodeInfo = nodeInfo
        }
    }

    fun setAccount(chain: BaseChain, account: Any): Completable {
        return safeCompletable {
            baseData.mGRpcAccount = account
        }
    }

    fun setBalances(chain: BaseChain, balances: List<Coin>): Completable {
        return safeCompletable {
            baseData.mGrpcBalance = balances
        }
    }

    fun setValidators(
        chain: BaseChain,
        status: Staking.BondStatus,
        items: List<Staking.Validator>
    ): Completable {
        return safeCompletable {
            when (status) {
                Staking.BondStatus.BOND_STATUS_BONDED -> baseData.mGRpcTopValidators = items
                Staking.BondStatus.BOND_STATUS_UNBONDED -> baseData.mGRpcOtherValidators = items
                Staking.BondStatus.BOND_STATUS_UNBONDING -> baseData.mGRpcOtherValidators = items
            }
        }
    }

    fun setDelegations(chain: BaseChain, items: List<Staking.DelegationResponse>): Completable {
        return safeCompletable {
            baseData.mGrpcDelegations = items
        }
    }

    fun setUndelegations(chain: BaseChain, items: List<Staking.UnbondingDelegation>): Completable {
        return safeCompletable {
            baseData.mGrpcUndelegations = items
        }
    }

    fun getTopValidators(): Single<List<Staking.Validator>> {
        return singleCallable {
            baseData.mGRpcTopValidators
        }
    }

    fun getOtherValidators(): Single<List<Staking.Validator>> {
        return singleCallable {
            baseData.mGRpcOtherValidators
        }
    }

    fun setAllValidators(chain: BaseChain, validators: List<Staking.Validator>): Completable {
        return completeCallable {
            baseData.mGRpcAllValidators = validators
        }
    }

    fun setMyValidators(chain: BaseChain, validators: List<Staking.Validator>): Completable {
        return completeCallable {
            baseData.mGRpcMyValidators = validators
        }
    }

    fun getDelegations(): Single<List<Staking.DelegationResponse>> {
        return singleCallable {
            baseData.mGrpcDelegations
        }
    }

    fun getUndelegations(): Single<List<Staking.UnbondingDelegation>> {
        return singleCallable {
            baseData.mGrpcUndelegations
        }
    }

    fun setRewards(
        chain: BaseChain,
        items: List<Distribution.DelegationDelegatorReward>
    ): Completable {
        return completeCallable {
            baseData.mGrpcRewards = items
        }
    }

    fun setMintScanAssets(chain: BaseChain, items: List<Assets>): Completable {
        return completeCallable {
            baseData.mAssets = items
        }
    }

    fun setCw20Assets(items: List<Cw20Assets>): Completable {
        return completeCallable {
            baseData.mCw20Assets = items
        }
    }

    fun setGravityPools(chain: BaseChain, items: List<Liquidity.Pool>): Completable {
        return completeCallable {
            baseData.mGrpcGravityPools = items
        }
    }

    fun setStarNameFees(items: starnamed.x.configuration.v1beta1.Types.Fees): Completable {
        return completeCallable {
            baseData.mGrpcStarNameFee = items
        }
    }

    fun setStarNameConfig(config: starnamed.x.configuration.v1beta1.Types.Config): Completable {
        return completeCallable {
            baseData.mGrpcStarNameConfig = config
        }
    }

    fun setOsmosisPools(items: List<BalancerPool.Pool>): Completable {
        return completeCallable {
            baseData.mGrpcOsmosisPool = items
        }
    }

    fun setKavaMarketPrice(items: List<kava.pricefeed.v1beta1.QueryOuterClass.CurrentPriceResponse>): Completable {
        return completeCallable {
            val pricesMap =
                mutableMapOf<String, kava.pricefeed.v1beta1.QueryOuterClass.CurrentPriceResponse>()
            items.forEach { item ->
                pricesMap[item.marketId] = item
            }
            baseData.mKavaTokenPrice = pricesMap
        }
    }

    fun setKavaIncentiveParam(param: IncentiveParam): Completable {
        return completeCallable {
            baseData.mIncentiveParam5 = param
        }
    }

    fun setKavaIncentiveReward(reward: IncentiveReward): Completable {
        return completeCallable {
            baseData.mIncentiveRewards = reward
        }
    }

    companion object {
        private const val TAG = "GrpcLocalSource"
    }
}