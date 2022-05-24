package com.fulldive.wallet.interactors.chains.grpc

import android.content.Context
import com.fulldive.wallet.di.modules.DefaultLocalStorageModule
import com.fulldive.wallet.extensions.safeSingle
import com.fulldive.wallet.models.BaseChain
import com.google.gson.Gson
import com.google.protobuf.ByteString
import com.google.protobuf2.Any
import com.joom.lightsaber.ProvidedBy
import cosmos.base.query.v1beta1.Pagination.PageRequest
import cosmos.base.tendermint.v1beta1.Query.GetNodeInfoRequest
import cosmos.base.tendermint.v1beta1.ServiceGrpc
import cosmos.base.v1beta1.CoinOuterClass
import cosmos.distribution.v1beta1.Distribution
import cosmos.staking.v1beta1.Staking
import io.reactivex.Single
import org.json.JSONObject
import osmosis.gamm.poolmodels.balancer.BalancerPool
import tendermint.liquidity.v1beta1.Liquidity
import tendermint.p2p.Types
import wannabit.io.cosmostaion.dao.Assets
import wannabit.io.cosmostaion.dao.Cw20Assets
import wannabit.io.cosmostaion.dao.Cw20BalanceReq
import wannabit.io.cosmostaion.model.kava.IncentiveParam
import wannabit.io.cosmostaion.model.kava.IncentiveReward
import wannabit.io.cosmostaion.network.ApiClient
import wannabit.io.cosmostaion.network.ChannelBuilder
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ProvidedBy(DefaultLocalStorageModule::class)
class GrpcRemoteSource @Inject constructor(
    private val context: Context
) {
    private val gson = Gson()

    fun requestAccount(chain: BaseChain, address: String): Single<Any> {
        return safeSingle {
            cosmos.auth.v1beta1.QueryGrpc.newBlockingStub(ChannelBuilder.getChain(chain))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .account(
                    cosmos.auth.v1beta1.QueryOuterClass.QueryAccountRequest.newBuilder()
                        .setAddress(address).build()
                )
                .account
        }
    }

    fun requestDelegations(
        chain: BaseChain,
        address: String
    ): Single<List<Staking.DelegationResponse>> {
        return safeSingle {
            cosmos.staking.v1beta1.QueryGrpc.newBlockingStub(ChannelBuilder.getChain(chain))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .delegatorDelegations(
                    cosmos.staking.v1beta1.QueryOuterClass.QueryDelegatorDelegationsRequest
                        .newBuilder()
                        .setDelegatorAddr(address).build()
                )
                .delegationResponsesList
        }
    }

    fun requestUndelegations(
        chain: BaseChain,
        address: String
    ): Single<List<Staking.UnbondingDelegation>> {
        return safeSingle {
            cosmos.staking.v1beta1.QueryGrpc.newBlockingStub(ChannelBuilder.getChain(chain))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .delegatorUnbondingDelegations(
                    cosmos.staking.v1beta1.QueryOuterClass.QueryDelegatorUnbondingDelegationsRequest
                        .newBuilder()
                        .setDelegatorAddr(address).build()
                )
                .unbondingResponsesList
        }
    }

    fun requestRewards(
        chain: BaseChain,
        address: String
    ): Single<List<Distribution.DelegationDelegatorReward>> {
        return safeSingle {
            cosmos.distribution.v1beta1.QueryGrpc.newBlockingStub(ChannelBuilder.getChain(chain))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .delegationTotalRewards(
                    cosmos.distribution.v1beta1.QueryOuterClass.QueryDelegationTotalRewardsRequest
                        .newBuilder()
                        .setDelegatorAddress(address)
                        .build()
                )
                .rewardsList
        }
    }

    fun requestBalance(
        chain: BaseChain,
        address: String,
        limit: Int = 1000
    ): Single<List<CoinOuterClass.Coin>> {
        return safeSingle {
            cosmos.bank.v1beta1.QueryGrpc.newBlockingStub(ChannelBuilder.getChain(chain))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .allBalances(
                    cosmos.bank.v1beta1.QueryOuterClass.QueryAllBalancesRequest
                        .newBuilder()
                        .setPagination(
                            PageRequest.newBuilder().setLimit(limit.toLong()).build()
                        )
                        .setAddress(address).build()
                )
                .balancesList
        }
    }

    fun requestNodeInfo(chain: BaseChain): Single<Types.NodeInfo> {
        return safeSingle {
            ServiceGrpc
                .newBlockingStub(ChannelBuilder.getChain(chain))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .getNodeInfo(GetNodeInfoRequest.newBuilder().build())
                .nodeInfo
        }
    }

    fun requestValidators(
        chain: BaseChain,
        status: Staking.BondStatus,
        limit: Int
    ): Single<List<Staking.Validator>> {
        return safeSingle {
            cosmos.staking.v1beta1.QueryGrpc.newBlockingStub(ChannelBuilder.getChain(chain))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .validators(
                    cosmos.staking.v1beta1.QueryOuterClass.QueryValidatorsRequest
                        .newBuilder()
                        .setPagination(
                            PageRequest.newBuilder().setLimit(limit.toLong()).build()
                        )
                        .setStatus(status.name).build()
                )
                .validatorsList
        }
    }

    fun requestMintScanAssets(chain: BaseChain): Single<List<Assets>> {
        return safeSingle {
            chain
                .mintScanChainName
                .takeIf(String::isNotEmpty)
                ?.let(ApiClient.getMintscan(context)::getAssets)
                ?.execute()
                ?.body()
                ?.assets
        }
    }

    fun requestMintScanCw20Assets(): Single<List<Cw20Assets>> {
        return safeSingle {
            ApiClient
                .getMintscan(context)
                .cw20Assets
                .execute()
                .body()
                ?.assets
        }
    }

    fun requestCw20Balance(
        chain: BaseChain,
        address: String,
        contractAddress: String
    ): Single<String> {
        return safeSingle {
            cosmwasm.wasm.v1.QueryGrpc.newBlockingStub(ChannelBuilder.getChain(chain))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .smartContractState(
                    cosmwasm.wasm.v1.QueryOuterClass.QuerySmartContractStateRequest
                        .newBuilder()
                        .setAddress(contractAddress)
                        .setQueryData(
                            gson
                                .toJson(Cw20BalanceReq(address))
                                .let(ByteString::copyFromUtf8)
                        )
                        .build()
                )
                .data.toStringUtf8()
                .let { json -> JSONObject(json) }
                .getString("balance")
        }
    }

    fun requestGravityDex(chain: BaseChain, limit: Int): Single<List<Liquidity.Pool>> {
        return safeSingle {
            tendermint.liquidity.v1beta1.QueryGrpc.newBlockingStub(ChannelBuilder.getChain(chain))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .liquidityPools(
                    tendermint.liquidity.v1beta1.QueryOuterClass.QueryLiquidityPoolsRequest
                        .newBuilder()
                        .setPagination(
                            PageRequest.newBuilder().setLimit(limit.toLong()).build()
                        )
                        .build()
                )
                .poolsList
        }
    }

    fun requestStarNameFees(): Single<starnamed.x.configuration.v1beta1.Types.Fees> {
        return safeSingle {
            starnamed.x.configuration.v1beta1.QueryGrpc
                .newBlockingStub(ChannelBuilder.getChain(BaseChain.IOV_MAIN))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .fees(
                    starnamed.x.configuration.v1beta1.QueryOuterClass.QueryFeesRequest
                        .newBuilder()
                        .build()
                )
                .fees
        }

    }

    fun requestStarNameConfig(): Single<starnamed.x.configuration.v1beta1.Types.Config> {
        return safeSingle {
            starnamed.x.configuration.v1beta1.QueryGrpc
                .newBlockingStub(ChannelBuilder.getChain(BaseChain.IOV_MAIN))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .config(
                    starnamed.x.configuration.v1beta1.QueryOuterClass.QueryConfigRequest
                        .newBuilder()
                        .build()
                )
                .config
        }
    }

    fun requestOsmosisPools(limit: Int): Single<List<BalancerPool.Pool>> {
        return safeSingle {
            osmosis.gamm.v1beta1.QueryGrpc.newBlockingStub(ChannelBuilder.getChain(BaseChain.OSMOSIS_MAIN))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .pools(
                    osmosis.gamm.v1beta1.QueryOuterClass.QueryPoolsRequest
                        .newBuilder()
                        .setPagination(
                            PageRequest
                                .newBuilder()
                                .setLimit(limit.toLong())
                                .build()
                        )
                        .build()
                )
                .poolsList.map { pool ->
                    BalancerPool.Pool.parseFrom(pool.value)
                }
        }
    }

    fun requestKavaMarketPrice(): Single<List<kava.pricefeed.v1beta1.QueryOuterClass.CurrentPriceResponse>> {
        return safeSingle {
            kava.pricefeed.v1beta1.QueryGrpc.newBlockingStub(ChannelBuilder.getChain(BaseChain.KAVA_MAIN))
                .withDeadlineAfter(ChannelBuilder.TIME_OUT, TimeUnit.SECONDS)
                .prices(
                    kava.pricefeed.v1beta1.QueryOuterClass.QueryPricesRequest
                        .newBuilder()
                        .build()
                )
                .pricesList
        }
    }

    fun requestKavaIncentiveParam(): Single<IncentiveParam> {
        return safeSingle {
            ApiClient.getKavaChain(context).incentiveParam5.execute().body()!!.result
        }
    }

    fun requestKavaIncentiveReward(address: String): Single<IncentiveReward> {
        return safeSingle {
            ApiClient.getKavaChain(context).getIncentiveReward5(address).execute().body()!!.result
        }
    }
}