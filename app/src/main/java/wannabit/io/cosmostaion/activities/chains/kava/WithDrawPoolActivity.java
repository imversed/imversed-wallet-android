package wannabit.io.cosmostaion.activities.chains.kava;

import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_KAVA_EXIT_POOL;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fulldive.wallet.extensions.ActivityExtensionsKt;

import java.util.ArrayList;

import kava.swap.v1beta1.QueryOuterClass;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.activities.PasswordCheckActivity;
import wannabit.io.cosmostaion.base.BaseBroadCastActivity;
import wannabit.io.cosmostaion.base.BaseConstant;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.fragment.StepFeeSetFragment;
import wannabit.io.cosmostaion.fragment.StepMemoFragment;
import wannabit.io.cosmostaion.fragment.chains.kava.WithdrawPoolStep0Fragment;
import wannabit.io.cosmostaion.fragment.chains.kava.WithdrawPoolStep3Fragment;

public class WithDrawPoolActivity extends BaseBroadCastActivity {

    private RelativeLayout mRootView;
    private Toolbar mToolbar;
    private TextView mTitle;
    private ImageView mIvStep;
    private TextView mTvStep;
    private ViewPager mViewPager;
    private WithDrawPoolPageAdapter mPageAdapter;

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
        mTitle.setText(R.string.str_title_pool_exit);

        mTxType = CONST_PW_TX_KAVA_EXIT_POOL;
        mKavaSwapPool = (QueryOuterClass.PoolResponse) getIntent().getSerializableExtra("mKavaPool");
        mKavaDepositPool = (QueryOuterClass.DepositResponse) getIntent().getSerializableExtra("mKavaDeposit");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIvStep.setImageResource(R.drawable.step_4_img_1);
        mTvStep.setText(R.string.str_exit_pool_step_0);

        mPageAdapter = new WithDrawPoolPageAdapter(getSupportFragmentManager());
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
                    mTvStep.setText(R.string.str_exit_pool_step_0);
                    ((IRefreshTabListener) mPageAdapter.mCurrentFragment).onRefreshTab();
                } else if (i == 1) {
                    mIvStep.setImageResource(R.drawable.step_4_img_2);
                    mTvStep.setText(R.string.str_exit_pool_step_1);
                } else if (i == 2) {
                    mIvStep.setImageResource(R.drawable.step_4_img_3);
                    mTvStep.setText(R.string.str_exit_pool_step_2);
                    ((IRefreshTabListener) mPageAdapter.mCurrentFragment).onRefreshTab();
                } else if (i == 3) {
                    mIvStep.setImageResource(R.drawable.step_4_img_4);
                    mTvStep.setText(R.string.str_exit_pool_step_3);
                    ((IRefreshTabListener) mPageAdapter.mCurrentFragment).onRefreshTab();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        mViewPager.setCurrentItem(0);

        mRootView.setOnClickListener(v -> ActivityExtensionsKt.hideKeyboard(this));
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

    public void onStartExitPool() {
        Intent intent = new Intent(WithDrawPoolActivity.this, PasswordCheckActivity.class);
        intent.putExtra(BaseConstant.CONST_PW_PURPOSE, CONST_PW_TX_KAVA_EXIT_POOL);
        intent.putExtra("mKavaShare", mKavaShareAmount.toPlainString());
        intent.putExtra("mKavaMinTokenA", mKavaMinTokenA);
        intent.putExtra("mKavaMinTokenB", mKavaMinTokenB);
        intent.putExtra("memo", mTxMemo);
        intent.putExtra("fee", mTxFee);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
    }

    private class WithDrawPoolPageAdapter extends FragmentPagerAdapter {

        private final ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public WithDrawPoolPageAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            mFragments.add(WithdrawPoolStep0Fragment.newInstance(null));
            mFragments.add(StepMemoFragment.newInstance(null));
            mFragments.add(StepFeeSetFragment.newInstance(null));
            mFragments.add(WithdrawPoolStep3Fragment.newInstance(null));
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
}
