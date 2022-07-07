package wannabit.io.cosmostaion.activities.chains.kava;

import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_CREATE_CDP;
import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_KAVA_PRICE_TOKEN;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fulldive.wallet.extensions.ActivityExtensionsKt;

import java.math.BigDecimal;
import java.util.ArrayList;

import kava.cdp.v1beta1.Genesis;
import kava.pricefeed.v1beta1.QueryOuterClass;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.PasswordCheckActivity;
import wannabit.io.cosmostaion.base.BaseBroadCastActivity;
import wannabit.io.cosmostaion.base.BaseConstant;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.fragment.StepFeeSetFragment;
import wannabit.io.cosmostaion.fragment.StepMemoFragment;
import wannabit.io.cosmostaion.fragment.chains.kava.CreateCdpStep0Fragment;
import wannabit.io.cosmostaion.fragment.chains.kava.CreateCdpStep3Fragment;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.task.gRpcTask.KavaMarketPriceTokenGrpcTask;
import wannabit.io.cosmostaion.utils.WLog;

public class CreateCdpActivity extends BaseBroadCastActivity implements TaskListener {

    private RelativeLayout mRootView;
    private Toolbar mToolbar;
    private TextView mTitle;
    private ImageView mIvStep;
    private TextView mTvStep;
    private ViewPager mViewPager;
    private CreateCdpPageAdapter mPageAdapter;

    private String mMaketId;
    public Genesis.Params mCdpParams;
    public Genesis.CollateralParam mCollateralParam;
    public QueryOuterClass.CurrentPriceResponse mKavaTokenPrice;

    public BigDecimal toCollateralAmount = BigDecimal.ZERO;
    public BigDecimal toPrincipalAmount = BigDecimal.ZERO;
    public BigDecimal mLiquidationPrice = BigDecimal.ZERO;
    public BigDecimal mRiskRate = BigDecimal.ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        mRootView = findViewById(R.id.root_view);
        mToolbar = findViewById(R.id.toolbar);
        mTitle = findViewById(R.id.toolbarTitleTextView);
        mIvStep = findViewById(R.id.send_step);
        mTvStep = findViewById(R.id.send_step_msg);
        mViewPager = findViewById(R.id.view_pager);
        mTitle.setText(R.string.str_create_cdp_c);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIvStep.setImageResource(R.drawable.step_4_img_1);
        mTvStep.setText(R.string.str_create_cdp_step_1);

        mTxType = CONST_PW_TX_CREATE_CDP;

        mCollateralType = getIntent().getStringExtra("collateralParamType");
        mMaketId = getIntent().getStringExtra("marketId");
        mCdpParams = getBaseDao().mCdpParams;
        mCollateralParam = getBaseDao().getCollateralParamByType(mCollateralType);
        if (mCdpParams == null || mCollateralParam == null) {
            WLog.e("ERROR No cdp param data");
            onBackPressed();
            return;
        }

        mPageAdapter = new CreateCdpPageAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPageAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    mIvStep.setImageResource(R.drawable.step_4_img_1);
                    mTvStep.setText(R.string.str_create_cdp_step_1);
                } else if (i == 1) {
                    mIvStep.setImageResource(R.drawable.step_4_img_2);
                    mTvStep.setText(R.string.str_create_cdp_step_2);
                } else if (i == 2) {
                    mIvStep.setImageResource(R.drawable.step_4_img_3);
                    mTvStep.setText(R.string.str_create_cdp_step_3);
                    if (mPageAdapter.mCurrentFragment instanceof IRefreshTabListener) {
                        ((IRefreshTabListener) mPageAdapter.mCurrentFragment).onRefreshTab();
                    }
                } else if (i == 3) {
                    mIvStep.setImageResource(R.drawable.step_4_img_4);
                    mTvStep.setText(R.string.str_create_cdp_step_4);
                    if (mPageAdapter.mCurrentFragment instanceof IRefreshTabListener) {
                        ((IRefreshTabListener) mPageAdapter.mCurrentFragment).onRefreshTab();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        mViewPager.setCurrentItem(0);

        mRootView.setOnClickListener(v -> ActivityExtensionsKt.hideKeyboard(this));

        onFetchCdpInfo();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        ActivityExtensionsKt.hideKeyboard(this);
        if (mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        } else {
            super.onBackPressed();
        }
    }

    public void onNextStep() {
        if (mViewPager.getCurrentItem() < mViewPager.getChildCount()) {
            ActivityExtensionsKt.hideKeyboard(this);
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        }
    }

    public void onBeforeStep() {
        if (mViewPager.getCurrentItem() > 0) {
            ActivityExtensionsKt.hideKeyboard(this);
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        } else {
            onBackPressed();
        }
    }

    public void onStartCreateCdp() {
        Intent intent = new Intent(CreateCdpActivity.this, PasswordCheckActivity.class);
        intent.putExtra(BaseConstant.CONST_PW_PURPOSE, CONST_PW_TX_CREATE_CDP);
        intent.putExtra("mCollateral", mCollateral);
        intent.putExtra("mPrincipal", mPrincipal);
        intent.putExtra("mCollateralType", mCollateralType);
        intent.putExtra("fee", mTxFee);
        intent.putExtra("memo", mTxMemo);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);

    }

    private class CreateCdpPageAdapter extends FragmentPagerAdapter {

        private final ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public CreateCdpPageAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            mFragments.add(CreateCdpStep0Fragment.newInstance(null));
            mFragments.add(StepMemoFragment.newInstance(null));
            mFragments.add(StepFeeSetFragment.newInstance(null));
            mFragments.add(CreateCdpStep3Fragment.newInstance(null));
        }

        @Override
        public BaseFragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((BaseFragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        public BaseFragment getCurrentFragment() {
            return mCurrentFragment;
        }

        public ArrayList<BaseFragment> getFragments() {
            return mFragments;
        }

    }

    private int mTaskCount = 0;

    public void onFetchCdpInfo() {
        showWaitDialog();
        mTaskCount = 1;
        new KavaMarketPriceTokenGrpcTask(getBaseApplication(), this, getBaseChain(), mMaketId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onTaskResponse(TaskResult result) {
        if (isFinishing()) return;
        mTaskCount--;
        if (result.taskType == TASK_GRPC_FETCH_KAVA_PRICE_TOKEN) {
            if (result.isSuccess && result.resultData != null) {
                mKavaTokenPrice = (QueryOuterClass.CurrentPriceResponse) result.resultData;
            }
        }

        if (mTaskCount == 0) {
            hideWaitDialog();
            if (mCdpParams == null || mKavaTokenPrice == null) {
                Toast.makeText(getBaseContext(), R.string.str_network_error_title, Toast.LENGTH_SHORT).show();
                onBackPressed();
                return;
            }
            if (mPageAdapter.mCurrentFragment instanceof IRefreshTabListener) {
                ((IRefreshTabListener) mPageAdapter.mCurrentFragment).onRefreshTab();
            }
        }
    }
}
