package wannabit.io.cosmostaion.widget.txDetail.ibc;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fulldive.wallet.interactors.secret.MnemonicUtils;

import cosmos.tx.v1beta1.ServiceOuterClass;
import ibc.core.channel.v1.Tx;
import wannabit.io.cosmostaion.R;
import com.fulldive.wallet.models.BaseChain;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.widget.txDetail.TxHolder;

public class TxIBCAcknowledgeHolder extends TxHolder {
    ImageView itemIbcAcknowledgeImg;
    TextView itemIbcAcknowledgeSiner, itemIbcAcknowledge;

    public TxIBCAcknowledgeHolder(@NonNull View itemView) {
        super(itemView);
        itemIbcAcknowledgeImg = itemView.findViewById(R.id.tx_ibc_acknowledge_icon);
        itemIbcAcknowledgeSiner = itemView.findViewById(R.id.tx_ibc_acknowledge_signer);
        itemIbcAcknowledge = itemView.findViewById(R.id.tx_ibc_acknowledge_msg);
    }

    public void onBindMsg(Context c, BaseData baseData, BaseChain baseChain, ServiceOuterClass.GetTxResponse response, int position, String address, boolean isGen) {
        itemIbcAcknowledgeImg.setColorFilter(WDp.getChainColor(c, baseChain), android.graphics.PorterDuff.Mode.SRC_IN);

        try {
            Tx.MsgAcknowledgement msg = Tx.MsgAcknowledgement.parseFrom(response.getTx().getBody().getMessages(position).getValue());
            itemIbcAcknowledgeSiner.setText(msg.getSigner());
            itemIbcAcknowledge.setText(MnemonicUtils.INSTANCE.byteArrayToHexString(msg.getAcknowledgement().toByteArray()));
        } catch (Exception e) {
        }
    }
}
