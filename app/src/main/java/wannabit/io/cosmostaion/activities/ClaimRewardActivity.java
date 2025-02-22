package wannabit.io.cosmostaion.activities;

import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_PURPOSE;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_SIMPLE_REWARD;
import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_ALL_REWARDS;
import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_WITHDRAW_ADDRESS;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fulldive.wallet.extensions.ActivityExtensionsKt;

import java.util.ArrayList;

import cosmos.distribution.v1beta1.Distribution;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseBroadCastActivity;
import wannabit.io.cosmostaion.base.BaseFragment;
import wannabit.io.cosmostaion.base.IRefreshTabListener;
import wannabit.io.cosmostaion.fragment.RewardStep0Fragment;
import wannabit.io.cosmostaion.fragment.RewardStep3Fragment;
import wannabit.io.cosmostaion.fragment.StepFeeSetFragment;
import wannabit.io.cosmostaion.fragment.StepMemoFragment;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.task.gRpcTask.AllRewardGrpcTask;
import wannabit.io.cosmostaion.task.gRpcTask.WithdrawAddressGrpcTask;

public class ClaimRewardActivity extends BaseBroadCastActivity implements TaskListener {

    private ImageView mIvStep;
    private TextView mTvStep;
    private ViewPager viewPager;
    private RewardPageAdapter mPageAdapter;

    public String mWithdrawAddress;
    private int mTaskCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitleTextView = findViewById(R.id.toolbarTitleTextView);
        mIvStep = findViewById(R.id.send_step);
        mTvStep = findViewById(R.id.send_step_msg);
        viewPager = findViewById(R.id.view_pager);
        toolbarTitleTextView.setText(R.string.str_reward_c);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIvStep.setImageResource(R.drawable.step_4_img_1);
        mTvStep.setText(R.string.str_reward_step_1);

        mTxType = CONST_PW_TX_SIMPLE_REWARD;

        mValAddresses = getIntent().getStringArrayListExtra("valOpAddresses");

        mPageAdapter = new RewardPageAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(mPageAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    mIvStep.setImageResource(R.drawable.step_4_img_1);
                    mTvStep.setText(R.string.str_reward_step_1);

                } else if (i == 1) {
                    mIvStep.setImageResource(R.drawable.step_4_img_2);
                    mTvStep.setText(R.string.str_reward_step_2);

                } else if (i == 2) {
                    mIvStep.setImageResource(R.drawable.step_4_img_3);
                    mTvStep.setText(R.string.str_reward_step_3);
                    ((IRefreshTabListener) mPageAdapter.mCurrentFragment).onRefreshTab();

                } else if (i == 3) {
                    mIvStep.setImageResource(R.drawable.step_4_img_4);
                    mTvStep.setText(R.string.str_reward_step_4);
                    ((IRefreshTabListener) mPageAdapter.mCurrentFragment).onRefreshTab();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        viewPager.setCurrentItem(0);
        onFetchReward();
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
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        } else {
            super.onBackPressed();
        }
    }

    public void onNextStep() {
        if (viewPager.getCurrentItem() < viewPager.getChildCount()) {
            ActivityExtensionsKt.hideKeyboard(this);
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        }
    }

    public void onBeforeStep() {
        if (viewPager.getCurrentItem() > 0) {
            ActivityExtensionsKt.hideKeyboard(this);
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        } else {
            onBackPressed();
        }
    }

    private void onFetchReward() {
        if (mTaskCount > 0) return;
        mTaskCount = 2;
        new AllRewardGrpcTask(getBaseApplication(), this, getBaseChain(), getAccount()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new WithdrawAddressGrpcTask(getBaseApplication(), this, getBaseChain(), getAccount()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onStartReward() {
        Intent intent = new Intent(ClaimRewardActivity.this, PasswordCheckActivity.class);
        intent.putExtra(CONST_PW_PURPOSE, CONST_PW_TX_SIMPLE_REWARD);
        intent.putExtra("valOpAddresses", mValAddresses);
        intent.putExtra("memo", mTxMemo);
        intent.putExtra("fee", mTxFee);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
    }


    @Override
    public void onTaskResponse(TaskResult result) {
//        WLog.w("onTaskResponse " + result.taskType + " " + mTaskCount);
        mTaskCount--;
        if (isFinishing()) return;
        if (result.taskType == TASK_GRPC_FETCH_ALL_REWARDS) {
            ArrayList<Distribution.DelegationDelegatorReward> rewards = (ArrayList<Distribution.DelegationDelegatorReward>) result.resultData;
            if (rewards != null) {
                getBaseDao().mGrpcRewards = rewards;
            }

        } else if (result.taskType == TASK_GRPC_FETCH_WITHDRAW_ADDRESS) {
            mWithdrawAddress = (String) result.resultData;

        }

        if (mTaskCount == 0) {
            ((IRefreshTabListener) mPageAdapter.mCurrentFragment).onRefreshTab();
        }
    }

    private class RewardPageAdapter extends FragmentPagerAdapter {

        private final ArrayList<BaseFragment> mFragments = new ArrayList<>();
        private BaseFragment mCurrentFragment;

        public RewardPageAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            mFragments.add(RewardStep0Fragment.newInstance(null));
            mFragments.add(StepMemoFragment.newInstance(null));
            mFragments.add(StepFeeSetFragment.newInstance(null));
            mFragments.add(RewardStep3Fragment.newInstance(null));
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
