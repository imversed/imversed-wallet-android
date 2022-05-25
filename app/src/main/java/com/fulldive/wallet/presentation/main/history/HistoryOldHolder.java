package com.fulldive.wallet.presentation.main.history;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.fulldive.wallet.presentation.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseConstant;
import wannabit.io.cosmostaion.model.type.BnbHistory;
import wannabit.io.cosmostaion.network.res.ResOkHistoryHit;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;
import wannabit.io.cosmostaion.widget.BaseHolder;

public class HistoryOldHolder extends BaseHolder {
    private final CardView historyRoot;
    private final TextView historyType;
    private final TextView historySuccess;
    private final TextView history_time;
    private final TextView history_block;
    private final TextView history_time_gap;

    public HistoryOldHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        historyRoot = itemView.findViewById(R.id.card_history);
        historyType = itemView.findViewById(R.id.history_type);
        historySuccess = itemView.findViewById(R.id.history_success);
        history_time = itemView.findViewById(R.id.history_time);
        history_block = itemView.findViewById(R.id.history_block_height);
        history_time_gap = itemView.findViewById(R.id.history_time_gap);
    }

    public void onBindOldBnbHistory(@NotNull MainActivity mainActivity, BnbHistory history) {
        historyType.setText(getBNBTxType(history, mainActivity.getAccount().address));
        history_time.setText(WDp.getTimeformat(mainActivity, history.timeStamp));
        history_time_gap.setText(WDp.getTimeGap(mainActivity, history.timeStamp));
        history_block.setText(history.blockHeight + "block");
        historySuccess.setVisibility(View.GONE);
        historyRoot.setOnClickListener(v -> {
            String url = WUtil.getTxExplorer(mainActivity.getBaseChain(), history.txHash);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mainActivity.startActivity(intent);
        });
    }

    public void onBindOldOkHistory(@NotNull MainActivity mainActivity, ResOkHistoryHit history) {
        String type = history.getTransactionDatas().get(0).getType();
        if (type.contains("/")) {
            historyType.setText(type.split("/")[type.split("/").length - 1].replace("Msg", ""));
        } else {
            historyType.setText(type);
        }
        historySuccess.setVisibility(View.GONE);
        history_time.setText(WDp.getDpTime(mainActivity, history.getBlockTimeU0()));
        history_time_gap.setText(WDp.getTimeTxGap(mainActivity, history.getBlockTimeU0()));
        history_block.setText(history.getBlockHash() + "block");
        historyRoot.setOnClickListener(v -> {
            String url = BaseConstant.EXPLORER_OEC_TX + "tx/" + history.getHash();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mainActivity.startActivity(intent);
        });
    }

    private int getBNBTxType(BnbHistory history, String address) {
        switch (history.txType) {
            case "NEW_ORDER":
                return R.string.tx_new_order;
            case "CANCEL_ORDER":
                return R.string.tx_Cancel_order;
            case "TRANSFER":
                if (!TextUtils.isEmpty(history.fromAddr) && address.equals(history.fromAddr)) {
                    return R.string.tx_send;
                }
                return R.string.tx_receive;
            case "HTL_TRANSFER":
                if (history.fromAddr.equals(address)) {
                    return R.string.tx_send_htlc;
                } else if (history.toAddr.equals(address)) {
                    return R.string.tx_receive_htlc;
                }
                return R.string.tx_create_htlc;
            case "CLAIM_HTL":
                return R.string.tx_claim_htlc;
            case "REFUND_HTL":
                return R.string.tx_refund_htlc;
            default:
        }
        return R.string.tx_known;

    }
}
