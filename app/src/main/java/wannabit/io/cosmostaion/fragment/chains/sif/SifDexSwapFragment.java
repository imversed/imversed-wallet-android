package wannabit.io.cosmostaion.fragment.chains.sif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fulldive.wallet.models.BaseChain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import sifnode.clp.v1.Types;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.chains.sif.SifDexListActivity;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.dialog.Dialog_Swap_Coin_List;
import wannabit.io.cosmostaion.utils.PriceProvider;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WUtil;

public class SifDexSwapFragment extends BaseFragment implements View.OnClickListener, IRefreshTabListener {
    public final static int SELECT_INPUT_CHAIN = 8500;
    public final static int SELECT_OUTPUT_CHAIN = 8501;

    private RelativeLayout mBtnInputCoinList, mBtnOutputCoinList;
    private ImageView mInputImg;
    private TextView mInputCoin, mInputAmount;
    private TextView mSwapSlippage;
    private TextView mSwapTitle;
    private ImageView mOutputImg;
    private TextView mOutputCoin;
    private TextView mSwapInputCoinRate, mSwapInputCoinSymbol, mSwapOutputCoinRate, mSwapOutputCoinSymbol;
    private TextView mSwapInputCoinExRate, mSwapInputCoinExSymbol, mSwapOutputCoinExRate, mSwapOutputCoinExSymbol;

    private ImageButton mBtnToggle;
    private Button mBtnSwapStart;

    public ArrayList<Types.Pool> mPoolList = new ArrayList<>();
    public ArrayList<String> mAllDenoms = new ArrayList<>();
    public Types.Pool mSelectedPool;
    public ArrayList<String> mSwapableDenoms = new ArrayList<>();

    public String mInputCoinDenom;
    public String mOutputCoinDenom;
    public int mInPutDecimal = 18;
    public int mOutPutDecimal = 18;

    public static SifDexSwapFragment newInstance(Bundle bundle) {
        SifDexSwapFragment fragment = new SifDexSwapFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_swap_list, container, false);
        mBtnInputCoinList = rootView.findViewById(R.id.btn_to_input_coin);
        mBtnOutputCoinList = rootView.findViewById(R.id.btn_to_output_coin);

        mInputImg = rootView.findViewById(R.id.img_input_coin);
        mInputCoin = rootView.findViewById(R.id.txt_input_coin);
        mInputAmount = rootView.findViewById(R.id.inpus_amount);
        mOutputImg = rootView.findViewById(R.id.img_output_coin);
        mOutputCoin = rootView.findViewById(R.id.txt_output_coin);

        mSwapTitle = rootView.findViewById(R.id.swap_title);
        mSwapInputCoinRate = rootView.findViewById(R.id.inputs_rate);
        mSwapInputCoinSymbol = rootView.findViewById(R.id.inputs_rate_symbol);
        mSwapOutputCoinRate = rootView.findViewById(R.id.outputs_rate);
        mSwapOutputCoinSymbol = rootView.findViewById(R.id.outputs_rate_symbol);

        mSwapInputCoinExRate = rootView.findViewById(R.id.global_inputs_rate);
        mSwapInputCoinExSymbol = rootView.findViewById(R.id.global_inputs_rate_symbol);
        mSwapOutputCoinExRate = rootView.findViewById(R.id.global_outputs_rate);
        mSwapOutputCoinExSymbol = rootView.findViewById(R.id.global_outputs_rate_symbol);

        mSwapSlippage = rootView.findViewById(R.id.swap_slippage);
        mBtnToggle = rootView.findViewById(R.id.btn_toggle);
        mBtnSwapStart = rootView.findViewById(R.id.btn_start_swap);

        mBtnInputCoinList.setOnClickListener(this);
        mBtnOutputCoinList.setOnClickListener(this);
        mBtnToggle.setOnClickListener(this);
        mBtnSwapStart.setOnClickListener(this);

        mBtnToggle.setBackgroundTintList(getResources().getColorStateList(R.color.colorSif));
        mSwapTitle.setText(getString(R.string.str_sif_swap_rate));
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        mPoolList = getSActivity().mPoolList;
        mAllDenoms = getSActivity().mAllDenoms;

        if (mSelectedPool == null || mInputCoinDenom.isEmpty() || mOutputCoinDenom.isEmpty()) {
            if (mPoolList != null && mPoolList.size() > 0) {
                mSelectedPool = mPoolList.get(0);
                mInputCoinDenom = getSActivity().getBaseChain().getMainDenom();
                mOutputCoinDenom = mSelectedPool.getExternalAsset().getSymbol();
            }
        }
        onUpdateView();
    }

    private void onUpdateView() {
        mInPutDecimal = WUtil.getSifCoinDecimal(getBaseDao(), mInputCoinDenom);
        mOutPutDecimal = WUtil.getSifCoinDecimal(getBaseDao(), mOutputCoinDenom);

        mInputAmount.setText(WDp.getDpAmount2(getSActivity().getBalance(mInputCoinDenom), mInPutDecimal, mInPutDecimal));
        mSwapSlippage.setText(WDp.getPercentDp(new BigDecimal("2")));

        WUtil.dpSifTokenName(getSActivity(), getBaseDao(), mInputCoin, mInputCoinDenom);
        WUtil.DpSifTokenImg(getBaseDao(), mInputImg, mInputCoinDenom);
        WUtil.dpSifTokenName(getSActivity(), getBaseDao(), mOutputCoin, mOutputCoinDenom);
        WUtil.DpSifTokenImg(getBaseDao(), mOutputImg, mOutputCoinDenom);

        mSwapInputCoinRate.setText(WDp.getDpAmount2(BigDecimal.ONE, 0, 6));
        WUtil.dpSifTokenName(getSActivity(), getBaseDao(), mSwapInputCoinSymbol, mInputCoinDenom);
        WUtil.dpSifTokenName(getSActivity(), getBaseDao(), mSwapOutputCoinSymbol, mOutputCoinDenom);

        BigDecimal lpInputAmount = WUtil.getPoolLpAmount(mSelectedPool, mInputCoinDenom);
        BigDecimal lpOutputAmount = WUtil.getPoolLpAmount(mSelectedPool, mOutputCoinDenom);
        BigDecimal poolSwapRate = lpOutputAmount.divide(lpInputAmount, 24, RoundingMode.DOWN).movePointRight(mInPutDecimal - mOutPutDecimal);
        mSwapOutputCoinRate.setText(WDp.getDpAmount2(poolSwapRate, 0, 6));

        mSwapInputCoinExRate.setText(WDp.getDpAmount2(BigDecimal.ONE, 0, 6));
        WUtil.dpSifTokenName(getSActivity(), getBaseDao(), mSwapInputCoinExSymbol, mInputCoinDenom);
        WUtil.dpSifTokenName(getSActivity(), getBaseDao(), mSwapOutputCoinExSymbol, mOutputCoinDenom);

        final PriceProvider priceProvider = getBaseActivity()::getPrice;
        BigDecimal priceInput = WDp.perUsdValue(getBaseDao(), getBaseDao().getBaseDenom(mInputCoinDenom), priceProvider);
        BigDecimal priceOutput = WDp.perUsdValue(getBaseDao(), getBaseDao().getBaseDenom(mOutputCoinDenom), priceProvider);
        BigDecimal priceRate = BigDecimal.ZERO;
        if (priceInput.compareTo(BigDecimal.ZERO) == 0 || priceOutput.compareTo(BigDecimal.ZERO) == 0) {
            mSwapOutputCoinExRate.setText("??????");
        } else {
            priceRate = priceInput.divide(priceOutput, 6, RoundingMode.DOWN);
            mSwapOutputCoinExRate.setText(WDp.getDpAmount2(priceRate, 0, 6));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBtnInputCoinList)) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("denoms", mAllDenoms);
            Dialog_Swap_Coin_List dialog = Dialog_Swap_Coin_List.newInstance(bundle);
            dialog.setTargetFragment(this, SELECT_INPUT_CHAIN);
            showDialog(dialog);

        } else if (v.equals(mBtnOutputCoinList)) {
            mSwapableDenoms.clear();
            if (mInputCoinDenom.equals(BaseChain.SIF_MAIN.INSTANCE.getMainDenom())) {
                mSwapableDenoms = (ArrayList<String>) mAllDenoms.clone();
            } else {
                mSwapableDenoms.add(BaseChain.SIF_MAIN.INSTANCE.getMainDenom());
            }
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("denoms", mSwapableDenoms);
            Dialog_Swap_Coin_List dialog = Dialog_Swap_Coin_List.newInstance(bundle);
            dialog.setTargetFragment(this, SELECT_OUTPUT_CHAIN);
            showDialog(dialog);

        } else if (v.equals(mBtnToggle)) {
            String temp = mInputCoinDenom;
            mInputCoinDenom = mOutputCoinDenom;
            mOutputCoinDenom = temp;
            onUpdateView();

        } else if (v.equals(mBtnSwapStart)) {
            getSActivity().onStartSwap(mInputCoinDenom, mOutputCoinDenom, mSelectedPool);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECT_INPUT_CHAIN && resultCode == Activity.RESULT_OK) {
            mInputCoinDenom = mAllDenoms.get(data.getIntExtra("selectedDenom", 0));
            if (mInputCoinDenom.equals(BaseChain.SIF_MAIN.INSTANCE.getMainDenom())) {
                mSelectedPool = mPoolList.get(0);
                mOutputCoinDenom = mSelectedPool.getExternalAsset().getSymbol();
            } else {
                for (Types.Pool pool : mPoolList) {
                    if (pool.getExternalAsset().getSymbol().equals(mInputCoinDenom)) {
                        mSelectedPool = pool;
                        mOutputCoinDenom = BaseChain.SIF_MAIN.INSTANCE.getMainDenom();
                    }
                }
            }
            onUpdateView();

        } else if (requestCode == SELECT_OUTPUT_CHAIN && resultCode == Activity.RESULT_OK) {
            mOutputCoinDenom = mSwapableDenoms.get(data.getIntExtra("selectedDenom", 0));
            if (mOutputCoinDenom.equals(BaseChain.SIF_MAIN.INSTANCE.getMainDenom())) {
                mSelectedPool = mPoolList.get(0);
                mInputCoinDenom = mSelectedPool.getExternalAsset().getSymbol();
            } else {
                for (Types.Pool pool : mPoolList) {
                    if (pool.getExternalAsset().getSymbol().equals(mOutputCoinDenom)) {
                        mSelectedPool = pool;
                    }
                }
            }
            onUpdateView();
        }
    }

    private SifDexListActivity getSActivity() {
        return (SifDexListActivity) getBaseActivity();
    }
}

