package com.fulldive.wallet.presentation.main.history;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.fulldive.wallet.extensions.ContextExtensionsKt;
import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.presentation.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.model.type.Coin;
import wannabit.io.cosmostaion.network.res.ResApiNewTxListCustom;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;
import wannabit.io.cosmostaion.widget.BaseHolder;

public class HistoryNewHolder extends BaseHolder {
    private final CardView historyRoot;
    private final TextView historyType, historySuccess, history_time, history_amount, history_amount_symbol, history_time_gap;

    public HistoryNewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        historyRoot = itemView.findViewById(R.id.card_history);
        historyType = itemView.findViewById(R.id.history_type);
        historySuccess = itemView.findViewById(R.id.history_success);
        history_time = itemView.findViewById(R.id.history_time);
        history_time_gap = itemView.findViewById(R.id.history_time_gap);
        history_amount = itemView.findViewById(R.id.history_amount);
        history_amount_symbol = itemView.findViewById(R.id.history_amount_symobl);
    }

    public void onBindNewHistory(@NotNull MainActivity mainActivity, BaseData baseData, ResApiNewTxListCustom history) {
        final BaseChain chain = mainActivity.getBaseChain();
        final Coin coin = history.getDpCoin(chain);
        final String accountAddress = mainActivity.getAccount().address;
        final Context context = historyType.getContext();

        historyType.setText(history.getMsgType(context, accountAddress));
        history_time.setText(WDp.getTimeTxformat(context, history.data.timestamp));
        history_time_gap.setText(WDp.getTimeTxGap(context, history.data.timestamp));

        if (coin != null) {
            history_amount_symbol.setVisibility(View.VISIBLE);
            history_amount.setVisibility(View.VISIBLE);
            WDp.showCoinDp(baseData, coin, history_amount_symbol, history_amount, chain);
        } else if (history.getMsgType(context, accountAddress).equals(mainActivity.getString(R.string.tx_vote))) {
            history_amount_symbol.setVisibility(View.VISIBLE);
            history_amount_symbol.setText(history.getVoteOption());
            history_amount_symbol.setTextColor(ContextExtensionsKt.getColorCompat(context, R.color.colorWhite));
            history_amount.setVisibility(View.GONE);
        } else {
            history_amount_symbol.setVisibility(View.GONE);
            history_amount.setVisibility(View.GONE);
        }
        if (history.isSuccess()) {
            historySuccess.setVisibility(View.GONE);
        } else {
            historySuccess.setVisibility(View.VISIBLE);
        }
        historyRoot.setOnClickListener(v -> {
            String url = WUtil.getTxExplorer(chain, history.data.txhash);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mainActivity.startActivity(intent);
        });
    }
}
