package wannabit.io.cosmostaion.widget.mainWallet;

import static com.fulldive.wallet.models.BaseChain.BNB_MAIN;
import static com.fulldive.wallet.models.BaseChain.COSMOS_MAIN;
import static com.fulldive.wallet.models.BaseChain.KAVA_MAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.SUPPORT_MOONPAY;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.fulldive.wallet.interactors.settings.SettingsInteractor;
import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.presentation.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.dao.Price;
import wannabit.io.cosmostaion.utils.PriceProvider;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.widget.BaseHolder;

public class WalletPriceHolder extends BaseHolder {
    private final CardView itemRoot;
    private final TextView itemPerPrice;
    private final TextView itemUpDownPrice;
    private final ImageView itemUpDownImg;
    private final LinearLayout itemBuyLayer;
    private final RelativeLayout itemBuyCoinBtn;
    private final TextView itemBuyCoinTv;

    public WalletPriceHolder(@NonNull View itemView) {
        super(itemView);
        itemRoot = itemView.findViewById(R.id.cardView);
        itemPerPrice = itemView.findViewById(R.id.per_price);
        itemUpDownPrice = itemView.findViewById(R.id.dash_price_updown_tx);
        itemUpDownImg = itemView.findViewById(R.id.ic_price_updown);
        itemBuyLayer = itemView.findViewById(R.id.buy_layer);
        itemBuyCoinBtn = itemView.findViewById(R.id.btn_buy_coin);
        itemBuyCoinTv = itemView.findViewById(R.id.tv_buy_coin);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        final SettingsInteractor settingsInteractor = mainActivity.getAppInjector().getInstance(SettingsInteractor.class);
        final BaseData data = mainActivity.getBaseDao();
        final BaseChain baseChain = mainActivity.getBaseChain();
        final String denom = baseChain.getMainDenom();

        final PriceProvider priceProvider = mainActivity::getPrice;
        itemPerPrice.setText(WDp.dpPerUserCurrencyValue(data, settingsInteractor.getCurrency(), denom, priceProvider));
        final Price price = mainActivity.getPrice(denom);
        itemUpDownPrice.setText(WDp.dpValueChange(price));
        final BigDecimal lastUpDown = WDp.valueChange(price);
        if (lastUpDown.compareTo(BigDecimal.ZERO) > 0) {
            itemUpDownImg.setVisibility(View.VISIBLE);
            itemUpDownImg.setImageResource(R.drawable.ic_price_up);
        } else if (lastUpDown.compareTo(BigDecimal.ZERO) < 0) {
            itemUpDownImg.setVisibility(View.VISIBLE);
            itemUpDownImg.setImageResource(R.drawable.ic_price_down);
        } else {
            itemUpDownImg.setVisibility(View.INVISIBLE);
        }

        if (SUPPORT_MOONPAY) {
            itemBuyLayer.setVisibility(View.VISIBLE);
            if (mainActivity.getBaseChain().equals(COSMOS_MAIN.INSTANCE)) {
                itemBuyCoinTv.setText(R.string.str_buy_atom);
            } else if (mainActivity.getBaseChain().equals(BNB_MAIN.INSTANCE)) {
                itemBuyCoinTv.setText(R.string.str_buy_bnb);
            } else if (mainActivity.getBaseChain().equals(KAVA_MAIN.INSTANCE)) {
                itemBuyCoinTv.setText(R.string.str_buy_kava);
            }
            itemBuyCoinBtn.setOnClickListener(v -> {
                if (mainActivity.getAccount().hasPrivateKey) {
                    mainActivity.onShowBuySelectFiat();
                } else {
                    mainActivity.onShowBuyWarnNoKey();
                }
            });
        } else {
            itemBuyCoinBtn.setVisibility(View.GONE);
            itemBuyLayer.setVisibility(View.GONE);
        }

        itemRoot.setOnClickListener(v -> {
            final BaseChain chain = mainActivity.getBaseChain();
            final String url = chain.getCoingeckoUrl();
            if (!url.isEmpty()) {
                mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }
}
