package wannabit.io.cosmostaion.activities;

import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_SIMPLE_SEND;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fulldive.wallet.extensions.ActivityExtensionsKt;
import com.fulldive.wallet.models.BaseChain;

import java.util.ArrayList;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseBroadCastActivity;
import wannabit.io.cosmostaion.base.BaseConstant;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.dao.BnbToken;
import wannabit.io.cosmostaion.fragment.SendStep0Fragment;
import wannabit.io.cosmostaion.fragment.SendStep1Fragment;
import wannabit.io.cosmostaion.fragment.SendStep4Fragment;
import wannabit.io.cosmostaion.fragment.StepFeeSetFragment;
import wannabit.io.cosmostaion.fragment.StepFeeSetOldFragment;
import wannabit.io.cosmostaion.fragment.StepMemoFragment;

public class SendActivity extends BaseBroadCastActivity {

    private ImageView mIvStep;
    private TextView mTvStep;
    private ViewPager mViewPager;
    private SendPageAdapter mPageAdapter;

    public String mStarName;
    public BnbToken mBnbToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        TextView titleView = findViewById(R.id.toolbarTitleTextView);
        mIvStep = findViewById(R.id.send_step);
        mTvStep = findViewById(R.id.send_step_msg);
        mViewPager = findViewById(R.id.view_pager);
        titleView.setText(R.string.str_send_c);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTvStep.setText(R.string.str_send_step_0);
        mTxType = CONST_PW_TX_SIMPLE_SEND;

        final BaseChain baseChain = getBaseChain();

        mDenom = getIntent().getStringExtra("sendTokenDenom");
        if (baseChain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
            mBnbToken = getBaseDao().getBnbToken(mDenom);
        }

        mPageAdapter = new SendPageAdapter(getSupportFragmentManager(), baseChain);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPageAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    mIvStep.setImageResource(R.drawable.step_1_img);
                    mTvStep.setText(R.string.str_send_step_0);
                } else if (i == 1) {
                    mIvStep.setImageResource(R.drawable.step_2_img);
                    mTvStep.setText(R.string.str_send_step_1);
                } else if (i == 2) {
                    mIvStep.setImageResource(R.drawable.step_3_img);
                    mTvStep.setText(R.string.str_send_step_2);
                } else if (i == 3) {
                    mIvStep.setImageResource(R.drawable.step_4_img);
                    mTvStep.setText(R.string.str_send_step_3);
                } else if (i == 4) {
                    mIvStep.setImageResource(R.drawable.step_5_img);
                    mTvStep.setText(R.string.str_send_step_4);
                }
                Fragment fragment = mPageAdapter.currentFragment;
                if (fragment instanceof IRefreshTabListener) {
                    ((IRefreshTabListener) mPageAdapter.currentFragment).onRefreshTab();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        mViewPager.setCurrentItem(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getAccount() == null) finish();
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
        ActivityExtensionsKt.hideKeyboard(this);
        if (mViewPager.getCurrentItem() < 4) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        }
    }

    public void onBeforeStep() {
        ActivityExtensionsKt.hideKeyboard(this);
        if (mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        } else {
            onBackPressed();
        }
    }

    public void onStartSend() {
        Intent intent = new Intent(SendActivity.this, PasswordCheckActivity.class);
        intent.putExtra(BaseConstant.CONST_PW_PURPOSE, CONST_PW_TX_SIMPLE_SEND);
        intent.putExtra("toAddress", mToAddress);
        intent.putParcelableArrayListExtra("amount", mAmounts);
        intent.putExtra("memo", mTxMemo);
        intent.putExtra("fee", mTxFee);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
    }

    private static class SendPageAdapter extends FragmentPagerAdapter {
        private final ArrayList<BaseFragment> fragments = new ArrayList<>();
        private BaseFragment currentFragment;

        public SendPageAdapter(FragmentManager fm, BaseChain baseChain) {
            super(fm);
            fragments.clear();
            fragments.add(SendStep0Fragment.newInstance(null));
            fragments.add(SendStep1Fragment.newInstance(null));
            fragments.add(StepMemoFragment.newInstance(null));
            if (baseChain.isGRPC()) {
                fragments.add(StepFeeSetFragment.newInstance(null));
            } else {
                fragments.add(StepFeeSetOldFragment.newInstance(null));
            }
            fragments.add(SendStep4Fragment.newInstance(null));
        }

        @Override
        public BaseFragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                currentFragment = ((BaseFragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        public BaseFragment getCurrentFragment() {
            return currentFragment;
        }

        public ArrayList<BaseFragment> getFragments() {
            return fragments;
        }
    }
}
