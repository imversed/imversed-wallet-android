package wannabit.io.cosmostaion.widget.txDetail;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fulldive.wallet.models.BaseChain;

import cosmos.tx.v1beta1.ServiceOuterClass;
import starnamed.x.starname.v1beta1.Tx;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.utils.WLog;

public class TxStarnameRenewHolder extends TxHolder {
    ImageView itemMsgImg;
    TextView itemMsgTitle;
    TextView itemStarname, itemSigner, itemStarnameFee;


    public TxStarnameRenewHolder(@NonNull View itemView) {
        super(itemView);
        itemMsgImg = itemView.findViewById(R.id.tx_msg_icon);
        itemMsgTitle = itemView.findViewById(R.id.tx_msg_text);
        itemStarname = itemView.findViewById(R.id.tx_starname);
        itemSigner = itemView.findViewById(R.id.tx_signer);
        itemStarnameFee = itemView.findViewById(R.id.tx_starname_fee_amount);
    }

    public void onBindMsg(Context c, BaseData baseData, BaseChain baseChain, ServiceOuterClass.GetTxResponse response, int position, String address, boolean isGen) {
        if (response.getTx().getBody().getMessages(position).getTypeUrl().contains("MsgRenewAccount")) {
            try {
                Tx.MsgRenewAccount msg = Tx.MsgRenewAccount.parseFrom(response.getTx().getBody().getMessages(position).getValue());
                itemMsgImg.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_msgs_renewaccount));
                itemMsgTitle.setText(itemView.getContext().getString(R.string.tx_starname_renew_account));
                itemStarname.setText(msg.getName() + "*" + msg.getDomain());
                itemSigner.setText(msg.getSigner());
                return;
            } catch (Exception e) {
            }
        } else {
            try {
                Tx.MsgRenewDomain msg = Tx.MsgRenewDomain.parseFrom(response.getTx().getBody().getMessages(position).getValue());
                WLog.w("domain ");
                itemMsgImg.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_msgs_renewdomain));
                itemMsgTitle.setText(itemView.getContext().getString(R.string.tx_starname_renew_domain));
                itemStarname.setText("*" + msg.getDomain());
                itemSigner.setText(msg.getSigner());
                return;
            } catch (Exception e) {
            }
        }
    }
}
