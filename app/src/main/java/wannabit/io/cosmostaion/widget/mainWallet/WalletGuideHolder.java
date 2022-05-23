package wannabit.io.cosmostaion.widget.mainWallet;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.fulldive.wallet.models.Guide;
import com.fulldive.wallet.presentation.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.utils.WUtil;
import wannabit.io.cosmostaion.widget.BaseHolder;

public class WalletGuideHolder extends BaseHolder {
    public CardView itemRoot;
    public ImageView itemGuideImg;
    public TextView itemGuideTitle;
    public TextView itemGuideMsg;
    public Button itemBtnGuide1;
    public Button itemBtnGuide2;

    public WalletGuideHolder(@NonNull View itemView) {
        super(itemView);
        itemRoot = itemView.findViewById(R.id.cardView);
        itemGuideImg = itemView.findViewById(R.id.img_guide);
        itemGuideTitle = itemView.findViewById(R.id.title_guide);
        itemGuideMsg = itemView.findViewById(R.id.msg_guide);
        itemBtnGuide1 = itemView.findViewById(R.id.btn_guide1);
        itemBtnGuide2 = itemView.findViewById(R.id.btn_guide2);
    }

    public void onBindHolder(@NotNull MainActivity mainActivity) {
        final Guide guide = mainActivity.getBaseChain().getGuide();
        if (guide != null) {
            itemRoot.setVisibility(View.VISIBLE);
            itemGuideImg.setImageResource(guide.getGuideIcon());
            itemGuideTitle.setText(guide.getGuideTitle());
            itemGuideMsg.setText(guide.getGuideMessage());
            itemBtnGuide1.setText(guide.getButtonText1());
            itemBtnGuide2.setText(guide.getButtonText2());

            itemBtnGuide1.setOnClickListener(v -> mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(guide.getButtonLink1()))));
            itemBtnGuide2.setOnClickListener(v -> mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(guide.getButtonLink2()))));
        } else {
            itemRoot.setVisibility(View.GONE);
        }
    }
}
