package com.fulldive.wallet.presentation.main.tokens;

import static wannabit.io.cosmostaion.base.BaseConstant.ASSET_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.BINANCE_TOKEN_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.COSMOS_COIN_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.EMONEY_COIN_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.KAVA_COIN_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.OKEX_COIN_IMG_URL;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_HARD;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_ION;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_SWP;
import static wannabit.io.cosmostaion.base.BaseConstant.TOKEN_USDX;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.models.Currency;
import com.fulldive.wallet.models.WalletBalance;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import tendermint.liquidity.v1beta1.Liquidity;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.dao.Assets;
import wannabit.io.cosmostaion.dao.BnbToken;
import wannabit.io.cosmostaion.dao.Cw20Assets;
import wannabit.io.cosmostaion.dao.IbcToken;
import wannabit.io.cosmostaion.dao.OkToken;
import wannabit.io.cosmostaion.utils.PriceProvider;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;

class TokensAdapter extends RecyclerView.Adapter<TokensAdapter.AssetHolder> {
    public List<WalletBalance> balances = new ArrayList<>();
    public List<WalletBalance> ibcAuthedItems = new ArrayList<>();
    public List<WalletBalance> osmosisPoolItems = new ArrayList<>();
    public List<WalletBalance> etherItems = new ArrayList<>();
    public List<WalletBalance> ibcUnknownItems = new ArrayList<>();
    public List<WalletBalance> GravityDexItems = new ArrayList<>();
    public List<WalletBalance> injectivePoolItems = new ArrayList<>();
    public List<WalletBalance> kavaBep2Items = new ArrayList<>();
    public List<WalletBalance> nativeItems = new ArrayList<>();
    public List<WalletBalance> etcItems = new ArrayList<>();
    public List<WalletBalance> unknownItems = new ArrayList<>();
    public List<Cw20Assets> CW20Items = new ArrayList<>();
    public BaseChain baseChain;
    public Currency currency = Currency.Companion.getDefault();

    public PriceProvider priceProvider;
    public BaseData baseData;
    public OnItemsClickListeners itemsClickListeners;
    public OnBalanceProvider onBalanceProvider;

    @NonNull
    @Override
    public AssetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_token, viewGroup, false);
        return new AssetHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetHolder viewHolder, int position) {
        if (baseChain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE)) {
            if (getItemViewType(position) == MainTokensFragment.SECTION_NATIVE) {
                onNativeGrpcItem(viewHolder, position);
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_AUTHED_GRPC) {
                onBindIbcAuthToken(viewHolder, position - nativeItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_OSMOSIS_POOL_GRPC) {
                onBindOsmoPoolToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC) {
                onBindIbcUnknownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - osmosisPoolItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_UNKNOWN) {
                onBindUnKnownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - osmosisPoolItems.size() - ibcUnknownItems.size());
            }

        } else if (baseChain.equals(BaseChain.SIF_MAIN.INSTANCE) || baseChain.equals(BaseChain.GRABRIDGE_MAIN.INSTANCE)) {
            if (getItemViewType(position) == MainTokensFragment.SECTION_NATIVE) {
                onNativeGrpcItem(viewHolder, position);
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_AUTHED_GRPC) {
                onBindIbcAuthToken(viewHolder, position - nativeItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_ETHER_GRPC) {
                onBindErcToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC) {
                onBindIbcUnknownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - etherItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_UNKNOWN) {
                onBindUnKnownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - etherItems.size() - ibcUnknownItems.size());
            }

        } else if (baseChain.equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
            if (getItemViewType(position) == MainTokensFragment.SECTION_NATIVE) {
                onNativeGrpcItem(viewHolder, position);
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_AUTHED_GRPC) {
                onBindIbcAuthToken(viewHolder, position - nativeItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_GRAVICTY_DEX_GRPC) {
                onBindGravityDexToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC) {
                onBindIbcUnknownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - GravityDexItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_UNKNOWN) {
                onBindUnKnownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - GravityDexItems.size() - ibcUnknownItems.size());
            }

        } else if (baseChain.equals(BaseChain.INJ_MAIN.INSTANCE)) {
            if (getItemViewType(position) == MainTokensFragment.SECTION_NATIVE) {
                onNativeGrpcItem(viewHolder, position);
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_AUTHED_GRPC) {
                onBindIbcAuthToken(viewHolder, position - nativeItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_ETHER_GRPC) {
                onBindErcToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_INJECTIVE_POOL_GRPC) {
                onBindInjectivePoolToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - etherItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC) {
                onBindIbcUnknownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - etherItems.size() - injectivePoolItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_UNKNOWN) {
                onBindUnKnownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - etherItems.size() - injectivePoolItems.size() - ibcUnknownItems.size());
            }

        } else if (baseChain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            if (getItemViewType(position) == MainTokensFragment.SECTION_NATIVE) {
                onNativeGrpcItem(viewHolder, position);
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_AUTHED_GRPC) {
                onBindIbcAuthToken(viewHolder, position - nativeItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_KAVA_BEP2_GRPC) {
                onBindKavaBep2Token(viewHolder, position - nativeItems.size() - ibcAuthedItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_ETC) {
                onBindEtcGrpcToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - kavaBep2Items.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC) {
                onBindIbcUnknownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - kavaBep2Items.size() - etcItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_UNKNOWN) {
                onBindUnKnownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - kavaBep2Items.size() - etcItems.size() - ibcUnknownItems.size());
            }

        } else if (baseChain.equals(BaseChain.JUNO_MAIN.INSTANCE)) {
            if (getItemViewType(position) == MainTokensFragment.SECTION_NATIVE) {
                onNativeGrpcItem(viewHolder, position);
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_AUTHED_GRPC) {
                onBindIbcAuthToken(viewHolder, position - nativeItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_CW20_GRPC) {
                onBindCw20GrpcToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC) {
                onBindIbcUnknownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - CW20Items.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_UNKNOWN) {
                onBindUnKnownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - CW20Items.size() - ibcUnknownItems.size());
            }

        } else if (baseChain.isGRPC()) {
            if (getItemViewType(position) == MainTokensFragment.SECTION_NATIVE) {
                onNativeGrpcItem(viewHolder, position);
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_AUTHED_GRPC) {
                onBindIbcAuthToken(viewHolder, position - nativeItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC) {
                onBindIbcUnknownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_UNKNOWN) {
                onBindUnKnownToken(viewHolder, position - nativeItems.size() - ibcAuthedItems.size() - ibcUnknownItems.size());
            }

        } else if (baseChain.equals(BaseChain.OKEX_MAIN.INSTANCE)) {
            if (getItemViewType(position) == MainTokensFragment.SECTION_NATIVE) {
                onBindNativeItem(viewHolder, position);
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_ETC) {
                onBindEtcToken(viewHolder, position - nativeItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_UNKNOWN) {
                onBindUnknownCoin(viewHolder, position - nativeItems.size() - etcItems.size());
            }
        } else if (baseChain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
            if (getItemViewType(position) == MainTokensFragment.SECTION_NATIVE) {
                onBindNativeItem(viewHolder, position);
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_ETC) {
                onBindEtcToken(viewHolder, position - nativeItems.size());
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_UNKNOWN) {
                onBindUnknownCoin(viewHolder, position - nativeItems.size() - etcItems.size());
            }
        } else {
            if (getItemViewType(position) == MainTokensFragment.SECTION_NATIVE) {
                onBindNativeItem(viewHolder, position);
            } else if (getItemViewType(position) == MainTokensFragment.SECTION_UNKNOWN) {
                onBindUnknownCoin(viewHolder, position - nativeItems.size());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (baseChain.equals(BaseChain.JUNO_MAIN.INSTANCE)) {
            return balances.size() + CW20Items.size();
        } else {
            return balances.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (baseChain.equals(BaseChain.OSMOSIS_MAIN.INSTANCE)) {
            if (position < nativeItems.size()) {
                return MainTokensFragment.SECTION_NATIVE;
            } else if (position < nativeItems.size() + ibcAuthedItems.size()) {
                return MainTokensFragment.SECTION_IBC_AUTHED_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + osmosisPoolItems.size()) {
                return MainTokensFragment.SECTION_OSMOSIS_POOL_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + osmosisPoolItems.size() + ibcUnknownItems.size()) {
                return MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + osmosisPoolItems.size() + ibcUnknownItems.size() + unknownItems.size()) {
                return MainTokensFragment.SECTION_UNKNOWN;
            }

        } else if (baseChain.equals(BaseChain.SIF_MAIN.INSTANCE) || baseChain.equals(BaseChain.GRABRIDGE_MAIN.INSTANCE)) {
            if (position < nativeItems.size()) {
                return MainTokensFragment.SECTION_NATIVE;
            } else if (position < nativeItems.size() + ibcAuthedItems.size()) {
                return MainTokensFragment.SECTION_IBC_AUTHED_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + etherItems.size()) {
                return MainTokensFragment.SECTION_ETHER_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + etherItems.size() + ibcUnknownItems.size()) {
                return MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + etherItems.size() + ibcUnknownItems.size() + unknownItems.size()) {
                return MainTokensFragment.SECTION_UNKNOWN;
            }

        } else if (baseChain.equals(BaseChain.COSMOS_MAIN.INSTANCE)) {
            if (position < nativeItems.size()) {
                return MainTokensFragment.SECTION_NATIVE;
            } else if (position < nativeItems.size() + ibcAuthedItems.size()) {
                return MainTokensFragment.SECTION_IBC_AUTHED_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + GravityDexItems.size()) {
                return MainTokensFragment.SECTION_GRAVICTY_DEX_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + GravityDexItems.size() + ibcUnknownItems.size()) {
                return MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + GravityDexItems.size() + ibcUnknownItems.size() + unknownItems.size()) {
                return MainTokensFragment.SECTION_UNKNOWN;
            }

        } else if (baseChain.equals(BaseChain.INJ_MAIN.INSTANCE)) {
            if (position < nativeItems.size()) {
                return MainTokensFragment.SECTION_NATIVE;
            } else if (position < nativeItems.size() + ibcAuthedItems.size()) {
                return MainTokensFragment.SECTION_IBC_AUTHED_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + etherItems.size()) {
                return MainTokensFragment.SECTION_ETHER_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + etherItems.size() + injectivePoolItems.size()) {
                return MainTokensFragment.SECTION_INJECTIVE_POOL_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + etherItems.size() + injectivePoolItems.size() + ibcUnknownItems.size()) {
                return MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + etherItems.size() + injectivePoolItems.size() + ibcUnknownItems.size() + unknownItems.size()) {
                return MainTokensFragment.SECTION_UNKNOWN;
            }

        } else if (baseChain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
            if (position < nativeItems.size()) {
                return MainTokensFragment.SECTION_NATIVE;
            } else if (position < nativeItems.size() + ibcAuthedItems.size()) {
                return MainTokensFragment.SECTION_IBC_AUTHED_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + kavaBep2Items.size()) {
                return MainTokensFragment.SECTION_KAVA_BEP2_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + kavaBep2Items.size() + etcItems.size()) {
                return MainTokensFragment.SECTION_ETC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + kavaBep2Items.size() + etcItems.size() + ibcUnknownItems.size()) {
                return MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + kavaBep2Items.size() + etcItems.size() + ibcUnknownItems.size() + unknownItems.size()) {
                return MainTokensFragment.SECTION_UNKNOWN;
            }

        } else if (baseChain.equals(BaseChain.JUNO_MAIN.INSTANCE)) {
            if (position < nativeItems.size()) {
                return MainTokensFragment.SECTION_NATIVE;
            } else if (position < nativeItems.size() + ibcAuthedItems.size()) {
                return MainTokensFragment.SECTION_IBC_AUTHED_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + CW20Items.size()) {
                return MainTokensFragment.SECTION_CW20_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + CW20Items.size() + ibcUnknownItems.size()) {
                return MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + CW20Items.size() + ibcUnknownItems.size() + unknownItems.size()) {
                return MainTokensFragment.SECTION_UNKNOWN;
            }

        } else if (baseChain.isGRPC()) {
            if (position < nativeItems.size()) {
                return MainTokensFragment.SECTION_NATIVE;
            } else if (position < nativeItems.size() + ibcAuthedItems.size()) {
                return MainTokensFragment.SECTION_IBC_AUTHED_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + ibcUnknownItems.size()) {
                return MainTokensFragment.SECTION_IBC_UNKNOWN_GRPC;
            } else if (position < nativeItems.size() + ibcAuthedItems.size() + ibcUnknownItems.size() + unknownItems.size()) {
                return MainTokensFragment.SECTION_UNKNOWN;
            }
        } else if (baseChain.equals(BaseChain.OKEX_MAIN.INSTANCE)) {
            if (nativeItems != null) {
                if (position < nativeItems.size()) {
                    return MainTokensFragment.SECTION_NATIVE;
                } else if (position < nativeItems.size() + etcItems.size()) {
                    return MainTokensFragment.SECTION_ETC;
                } else if (position < nativeItems.size() + etcItems.size() + unknownItems.size()) {
                    return MainTokensFragment.SECTION_UNKNOWN;
                }
            } else {
                if (position < etcItems.size()) {
                    return MainTokensFragment.SECTION_ETC;
                } else if (position < etcItems.size() + unknownItems.size()) {
                    return MainTokensFragment.SECTION_UNKNOWN;
                }
            }

        } else if (baseChain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
            if (position < nativeItems.size()) {
                return MainTokensFragment.SECTION_NATIVE;
            } else if (position < nativeItems.size() + etcItems.size()) {
                return MainTokensFragment.SECTION_ETC;
            } else if (position < nativeItems.size() + etcItems.size() + unknownItems.size()) {
                return MainTokensFragment.SECTION_UNKNOWN;
            }
        } else {
            return MainTokensFragment.SECTION_NATIVE;
        }
        return 0;
    }

    //with Native gRPC
    private void onNativeGrpcItem(AssetHolder holder, final int position) {
        final WalletBalance balance = nativeItems.get(position);
        final BaseChain chain = BaseChain.Companion.getChainByDenom(balance.getDenom());

        Picasso.get().cancelRequest(holder.itemImg);
        final Context context = holder.itemView.getContext();

        if (chain != null) {
            holder.itemSymbol.setText(chain.getSymbolTitle());
            holder.itemSymbol.setTextColor(ContextCompat.getColor(context, chain.getChainColor()));
            holder.itemFullName.setText(chain.getFullNameCoin());
            if (balance.getDenom().equals(BaseChain.SECRET_MAIN.INSTANCE.getMainDenom())) {
                holder.itemInnerSymbol.setText("(" + balance.getDenom() + ")");
            } else {
                holder.itemInnerSymbol.setText("");
            }
            holder.itemImg.setImageDrawable(ContextCompat.getDrawable(context, chain.getCoinIcon()));
        } else if (balance.getDenom().equals(TOKEN_ION)) {
            holder.itemSymbol.setText(R.string.str_uion_c);
            holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorIon));
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText("Ion Coin");
            holder.itemImg.setImageResource(R.drawable.token_ion);
        } else if (balance.getDenom().equals(TOKEN_HARD)) {
            Picasso.get().load(KAVA_COIN_IMG_URL + balance.getDenom() + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
            holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorHard));
            holder.itemSymbol.setText(balance.getDenom().toUpperCase());
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText("HardPool Gov. Coin");
        } else if (balance.getDenom().equals(TOKEN_USDX)) {
            Picasso.get().load(KAVA_COIN_IMG_URL + balance.getDenom() + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
            holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorUsdx));
            holder.itemSymbol.setText(balance.getDenom().toUpperCase());
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText("USD Stable Asset");
        } else if (balance.getDenom().equals(TOKEN_SWP)) {
            Picasso.get().load(KAVA_COIN_IMG_URL + balance.getDenom() + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
            holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorSwp));
            holder.itemSymbol.setText(balance.getDenom().toUpperCase());
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText("Kava Swap Coin");
        } else if (balance.getDenom().startsWith("e")) {
            holder.itemSymbol.setText(balance.getDenom().toUpperCase());
            holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText(balance.getDenom().substring(1).toUpperCase() + " on E-Money Network");
            Picasso.get().load(EMONEY_COIN_IMG_URL + balance.getDenom() + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
        }

        BigDecimal amount = balance.getBalanceAmount().add(baseData.getAllMainAsset(balance.getDenom()));  //TODO: add vesting
        int divideDecimal = 6;
        int displayDecimal = 6;
        int divider = 6;

        if (balance.getDenom().equals(TOKEN_ION) || balance.getDenom().startsWith("e")) {
            amount = onBalanceProvider.getFullBalance(balance.getDenom()).getBalanceAmount();
        } else if (balance.getDenom().equals(BaseChain.FETCHAI_MAIN.INSTANCE.getMainDenom()) || balance.getDenom().equals(BaseChain.INJ_MAIN.INSTANCE.getMainDenom()) || balance.getDenom().equals(BaseChain.SIF_MAIN.INSTANCE.getMainDenom())) {
            divideDecimal = 18;
            divider = 18;
        } else if (balance.getDenom().equals(BaseChain.EVMOS_MAIN.INSTANCE.getMainDenom()) || balance.getDenom().equals(BaseChain.CUDOS_MAIN.INSTANCE.getMainDenom())) {
            divideDecimal = 18;
        } else if (balance.getDenom().equals(BaseChain.PROVENANCE_MAIN.INSTANCE.getMainDenom())) {
            divideDecimal = 9;
        } else if (balance.getDenom().equals(BaseChain.CRYPTO_MAIN.INSTANCE.getMainDenom())) {
            divideDecimal = 8;
            divider = 8;
        } else if (balance.getDenom().equals(TOKEN_HARD) || balance.getDenom().equals(TOKEN_USDX) || balance.getDenom().equals(TOKEN_SWP)) {
            amount = balance.getBalanceAmount(); // .add(baseData.getVesting(balance.getSymbol()));
            divideDecimal = WUtil.getKavaCoinDecimal(baseData, balance.getDenom());
        }

        holder.itemBalance.setText(WDp.getDpAmount2(amount, divideDecimal, displayDecimal));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, balance.getDenom(), amount, divider, priceProvider));

        holder.itemRoot.setOnClickListener(v -> {
            if (nativeItems.get(position).getDenom().equalsIgnoreCase(baseChain.getMainDenom())) {
                itemsClickListeners.onStackingTokenClicked(balance.getDenom());
            } else {
                itemsClickListeners.onNativeTokenClicked(balance.getDenom());
            }
        });
    }

    //with Authed IBC gRPC
    private void onBindIbcAuthToken(AssetHolder holder, int position) {
        final WalletBalance balance = ibcAuthedItems.get(position);
        final IbcToken ibcToken = baseData.getIbcToken(balance.getDenom());
        final Context context = holder.itemView.getContext();
        holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        holder.itemFullName.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        holder.itemInnerSymbol.setText("");
        if (ibcToken == null) {
            holder.itemSymbol.setText(R.string.str_unknown);
            holder.itemFullName.setText("");
            holder.itemImg.setImageResource(R.drawable.token_default_ibc);
            holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, balance.getDenom(), BigDecimal.ZERO, 6, priceProvider));
        } else {
            holder.itemSymbol.setText(ibcToken.display_denom.toUpperCase());
            holder.itemFullName.setText(ibcToken.channel_id);
            holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), ibcToken.decimal, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, baseData.getBaseDenom(balance.getDenom()), balance.getBalanceAmount(), ibcToken.decimal, priceProvider));
            try {
                Picasso.get().load(ibcToken.moniker).fit().placeholder(R.drawable.token_default_ibc).error(R.drawable.token_default_ibc).into(holder.itemImg);
            } catch (Exception e) {
            }
        }

        holder.itemRoot.setOnClickListener(v -> {
            itemsClickListeners.onIbcTokenClicked(balance.getDenom());
        });
    }

    //with Unknown IBC gRPC
    private void onBindIbcUnknownToken(AssetHolder holder, int position) {
        final WalletBalance balance = ibcUnknownItems.get(position);
        final IbcToken ibcToken = baseData.getIbcToken(balance.getDenom());
        holder.itemInnerSymbol.setText("");
        holder.itemSymbol.setText(R.string.str_unknown);
        if (ibcToken == null) {
            holder.itemFullName.setText("");
            holder.itemImg.setImageResource(R.drawable.token_default_ibc);
            holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, balance.getDenom(), BigDecimal.ZERO, 6, priceProvider));
        } else {
            holder.itemFullName.setText(ibcToken.channel_id);
            holder.itemImg.setImageResource(R.drawable.token_default_ibc);
            holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, balance.getDenom(), balance.getBalanceAmount(), 6, priceProvider));
        }

        holder.itemRoot.setOnClickListener(v -> {
            itemsClickListeners.onIbcTokenClicked(balance.getDenom());
        });
    }

    //with Osmosis Pool gRPC
    private void onBindOsmoPoolToken(AssetHolder holder, int position) {
        final WalletBalance balance = osmosisPoolItems.get(position);
        final Context context = holder.itemView.getContext();
        holder.itemSymbol.setText(balance.osmosisAmmDpDenom());
        holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText(balance.getDenom());
        holder.itemImg.setImageResource(R.drawable.token_pool);
        holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 18, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, balance.getDenom(), balance.getBalanceAmount(), 18, priceProvider));

        holder.itemRoot.setOnClickListener(v -> {
            itemsClickListeners.onOsmoPoolTokenClicked(balance.getDenom());
        });
    }

    //with Cosmos Gravity Dex gRPC
    private void onBindGravityDexToken(AssetHolder holder, int position) {
        final WalletBalance balance = GravityDexItems.get(position);
        final Context context = holder.itemView.getContext();

        Picasso.get().load(COSMOS_COIN_IMG_URL + "gravitydex.png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
        holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, balance.getDenom(), balance.getBalanceAmount(), 6, priceProvider));
        holder.itemInnerSymbol.setText("");
        Liquidity.Pool poolInfo = baseData.getGravityPoolByDenom(balance.getDenom());
        if (poolInfo != null) {
            holder.itemSymbol.setText("GDEX-" + poolInfo.getId());
            holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.itemFullName.setText("pool/" + poolInfo.getId());
        }

        holder.itemRoot.setOnClickListener(v -> itemsClickListeners.onOsmoPoolTokenClicked(balance.getDenom()));
    }

    //with Injective Pool gRPC
    private void onBindInjectivePoolToken(AssetHolder holder, int position) {
        final WalletBalance balance = injectivePoolItems.get(position);
        final Context context = holder.itemView.getContext();

        holder.itemSymbol.setText("SHARE" + balance.getDenom().substring(5));
        holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText("Pool Asset");
        holder.itemImg.setImageResource(R.drawable.token_ic);
        holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 18, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, balance.getDenom(), BigDecimal.ZERO, 6, priceProvider));

        holder.itemRoot.setOnClickListener(v -> {
            itemsClickListeners.onOsmoPoolTokenClicked(balance.getDenom());
        });
    }

    //with Erc gRPC
    private void onBindErcToken(AssetHolder holder, int position) {
        final WalletBalance balance = etherItems.get(position);
        final Assets assets = baseData.getAsset(balance.getDenom());
        final Context context = holder.itemView.getContext();

        if (assets != null) {
            holder.itemSymbol.setText(assets.origin_symbol);
            holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.itemInnerSymbol.setText("");
            holder.itemFullName.setText(assets.display_symbol);
            Picasso.get().load(ASSET_IMG_URL + assets.logo).fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);

            BigDecimal totalAmount = onBalanceProvider.getFullBalance(assets.denom).getBalanceAmount();
            holder.itemBalance.setText(WDp.getDpAmount2(totalAmount, assets.decimal, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, assets.origin_symbol, totalAmount, assets.decimal, priceProvider));

            holder.itemRoot.setOnClickListener(v -> {
                itemsClickListeners.onErcTokenClicked(balance.getDenom());
            });
        }
    }

    //bind kava bep2 tokens with gRPC
    private void onBindKavaBep2Token(AssetHolder holder, int position) {
        final WalletBalance balance = kavaBep2Items.get(position);
        final Context context = holder.itemView.getContext();

        Picasso.get().load(KAVA_COIN_IMG_URL + balance.getDenom() + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
        holder.itemSymbol.setText(balance.getDenom().toUpperCase());
        holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText(balance.getDenom().toUpperCase() + " on Kava Chain");

        BigDecimal totalAmount = onBalanceProvider.getFullBalance(balance.getDenom()).getBalanceAmount();
        String baseDenom = WDp.getKavaBaseDenom(balance.getDenom());
        int bep2decimal = WUtil.getKavaCoinDecimal(baseData, balance.getDenom());
        holder.itemBalance.setText(WDp.getDpAmount2(totalAmount, bep2decimal, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, baseDenom, totalAmount, bep2decimal, priceProvider));

        holder.itemRoot.setOnClickListener(v -> {
            itemsClickListeners.onNativeTokenClicked(balance.getDenom());
        });
    }

    //bind kava etc tokens with gRPC
    private void onBindEtcGrpcToken(AssetHolder holder, int position) {
        final WalletBalance balance = etcItems.get(position);
        final Context context = holder.itemView.getContext();

        Picasso.get().load(KAVA_COIN_IMG_URL + "hbtc.png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
        holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        holder.itemSymbol.setText(balance.getDenom().toUpperCase());
        holder.itemInnerSymbol.setText("(" + balance.getDenom() + ")");
        holder.itemFullName.setText(balance.getDenom().toUpperCase() + " on Kava Chain");

        BigDecimal tokenTotalAmount = balance.getBalanceAmount(); //getBalance(balance.getSymbol()).add(baseData.getVesting(balance.getSymbol()));
        BigDecimal convertedKavaAmount = WDp.convertTokenToKava(baseData, balance, priceProvider);
        holder.itemBalance.setText(WDp.getDpAmount2(tokenTotalAmount, WUtil.getKavaCoinDecimal(baseData, balance.getDenom()), 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, BaseChain.KAVA_MAIN.INSTANCE.getMainDenom(), convertedKavaAmount, 6, priceProvider));
    }

    //bind cw20 tokens with gRPC
    private void onBindCw20GrpcToken(AssetHolder holder, int position) {
        final Cw20Assets cw20Asset = CW20Items.get(position);
        final Context context = holder.itemView.getContext();

        Picasso.get().load(cw20Asset.logo).fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
        holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        holder.itemSymbol.setText(cw20Asset.denom.toUpperCase());
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText(cw20Asset.contract_address);

        int decimal = cw20Asset.decimal;
        holder.itemBalance.setText(WDp.getDpAmount2(cw20Asset.getAmount(), decimal, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, cw20Asset.denom, cw20Asset.getAmount(), decimal, priceProvider));

        holder.itemRoot.setOnClickListener(v -> {
            itemsClickListeners.onCW20TokenClicked(cw20Asset);
        });
    }

    //with Unknown Token gRPC
    private void onBindUnKnownToken(AssetHolder holder, int position) {
        final WalletBalance balance = unknownItems.get(position);
        final Context context = holder.itemView.getContext();

        holder.itemSymbol.setText(R.string.str_unknown);
        holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText("");
        holder.itemImg.setImageResource(R.drawable.token_ic);
        holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, balance.getDenom(), BigDecimal.ZERO, 6, priceProvider));
    }


    //with native tokens
    private void onBindNativeItem(AssetHolder holder, int position) {
        final WalletBalance balance = nativeItems.get(position);
        final Context context = holder.itemView.getContext();

        if (baseChain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
            final String denom = balance.getDenom();

            final BigDecimal amount = balance.getTotalAmount();
            final BnbToken bnbToken = baseData.getBnbToken(denom);
            if (bnbToken != null) {
                holder.itemSymbol.setText(bnbToken.original_symbol.toUpperCase());
                holder.itemInnerSymbol.setText("(" + bnbToken.symbol + ")");
                holder.itemFullName.setText(BaseChain.BNB_MAIN.INSTANCE.getFullNameCoin());
                holder.itemImg.setImageDrawable(ContextCompat.getDrawable(context, BaseChain.BNB_MAIN.INSTANCE.getCoinIcon()));
                holder.itemSymbol.setTextColor(WDp.getChainColor(context, BaseChain.BNB_MAIN.INSTANCE));
                holder.itemBalance.setText(WDp.getDpAmount2(amount, 0, 6));
                holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, BaseChain.BNB_MAIN.INSTANCE.getMainDenom(), amount, 0, priceProvider));
            }
            holder.itemRoot.setOnClickListener(v -> {
                itemsClickListeners.onNativeStackingTokenClicked();
            });

        } else if (baseChain.equals(BaseChain.OKEX_MAIN.INSTANCE)) {
            final OkToken okToken = baseData.okToken(balance.getDenom());
            holder.itemSymbol.setText(okToken.original_symbol.toUpperCase());
            holder.itemInnerSymbol.setText("(" + okToken.symbol + ")");
            holder.itemFullName.setText(BaseChain.OKEX_MAIN.INSTANCE.getFullNameCoin());
            if (balance.getDenom().equals(BaseChain.OKEX_MAIN.INSTANCE.getMainDenom())) {
                holder.itemSymbol.setTextColor(WDp.getChainColor(context, baseChain));
                holder.itemImg.setImageDrawable(ContextCompat.getDrawable(context, BaseChain.OKEX_MAIN.INSTANCE.getCoinIcon()));

                BigDecimal totalAmount = balance.getDelegatableAmount().add(baseData.getAllExToken(balance.getDenom()));
                holder.itemBalance.setText(WDp.getDpAmount2(totalAmount, 0, 6));
                holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, balance.getDenom(), totalAmount, 0, priceProvider));
            }
            holder.itemRoot.setOnClickListener(v -> {
                itemsClickListeners.onNativeStackingTokenClicked();
            });
        }
    }

    //with Etc tokens (binance, okex)
    private void onBindEtcToken(AssetHolder holder, int position) {
        final WalletBalance balance = etcItems.get(position);
        final String denom = balance.getDenom();
        final Context context = holder.itemView.getContext();

        if (BaseChain.OKEX_MAIN.INSTANCE.equals(baseChain)) {
            final OkToken okToken = baseData.okToken(denom);
            if (okToken != null) {
                holder.itemSymbol.setText(okToken.original_symbol.toUpperCase());
                holder.itemInnerSymbol.setText("(" + okToken.symbol + ")");
                holder.itemFullName.setText(okToken.description);
                holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                Picasso.get().load(OKEX_COIN_IMG_URL + okToken.original_symbol + ".png").placeholder(R.drawable.token_ic).error(R.drawable.token_ic).fit().into(holder.itemImg);
            }

            BigDecimal totalAmount = balance.getDelegatableAmount().add(baseData.getAllExToken(denom));
            BigDecimal convertAmount = WDp.convertTokenToOkt(onBalanceProvider.getFullBalance(denom), baseData, denom, priceProvider);
            holder.itemBalance.setText(WDp.getDpAmount2(totalAmount, 0, 6));
            holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, BaseChain.OKEX_MAIN.INSTANCE.getMainDenom(), convertAmount, 0, priceProvider));
            holder.itemRoot.setOnClickListener(v -> {
                itemsClickListeners.onEtcTokenClicked(denom);
            });
        } else if (BaseChain.BNB_MAIN.INSTANCE.equals(baseChain)) {
            final BigDecimal amount = balance.getTotalAmount();
            final BnbToken bnbToken = baseData.getBnbToken(denom);

            holder.itemSymbol.setText(bnbToken.original_symbol.toUpperCase());
            holder.itemInnerSymbol.setText("(" + bnbToken.symbol + ")");
            holder.itemFullName.setText(bnbToken.name);
            Picasso.get().load(BINANCE_TOKEN_IMG_URL + bnbToken.original_symbol + ".png").fit().placeholder(R.drawable.token_ic).error(R.drawable.token_ic).into(holder.itemImg);
            holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.itemBalance.setText(WDp.getDpAmount2(amount, 0, 6));

            final BigDecimal convertAmount = WUtil.getBnbConvertAmount(baseData, denom, amount);
            holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, BaseChain.BNB_MAIN.INSTANCE.getMainDenom(), convertAmount, 0, priceProvider));
            holder.itemRoot.setOnClickListener(v -> {
                itemsClickListeners.onEtcTokenClicked(denom);
            });
        }
    }

    //with Unknown coin oec, bnb
    private void onBindUnknownCoin(AssetHolder holder, int position) {
        final WalletBalance balance = unknownItems.get(position);
        final Context context = holder.itemView.getContext();

        holder.itemSymbol.setText(R.string.str_unknown);
        holder.itemSymbol.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        holder.itemInnerSymbol.setText("");
        holder.itemFullName.setText("");
        holder.itemImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.token_ic));
        holder.itemBalance.setText(WDp.getDpAmount2(balance.getBalanceAmount(), 6, 6));
        holder.itemValue.setText(WDp.dpUserCurrencyValue(baseData, currency, balance.getDenom(), BigDecimal.ZERO, 6, priceProvider));
    }

    interface OnItemsClickListeners {
        void onStackingTokenClicked(String denom);

        void onNativeTokenClicked(String denom);

        void onIbcTokenClicked(String denom);

        void onOsmoPoolTokenClicked(String denom);

        void onErcTokenClicked(String denom);

        void onCW20TokenClicked(Cw20Assets cw20Asset);

        void onNativeStackingTokenClicked();

        void onEtcTokenClicked(String denom);
    }

    interface OnBalanceProvider {
        WalletBalance getFullBalance(String denom);
    }

    public static class AssetHolder extends RecyclerView.ViewHolder {
        private final CardView itemRoot;
        private final ImageView itemImg;
        private final TextView itemSymbol;
        private final TextView itemInnerSymbol;
        private final TextView itemFullName;
        private final TextView itemBalance;
        private final TextView itemValue;

        public AssetHolder(View v) {
            super(v);
            itemRoot = itemView.findViewById(R.id.token_card);
            itemImg = itemView.findViewById(R.id.token_img);
            itemSymbol = itemView.findViewById(R.id.token_symbol);
            itemInnerSymbol = itemView.findViewById(R.id.token_inner_symbol);
            itemFullName = itemView.findViewById(R.id.token_fullname);
            itemBalance = itemView.findViewById(R.id.token_balance);
            itemValue = itemView.findViewById(R.id.token_value);
        }
    }
}
