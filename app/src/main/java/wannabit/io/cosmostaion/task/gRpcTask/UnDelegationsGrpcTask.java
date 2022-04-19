package wannabit.io.cosmostaion.task.gRpcTask;

import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_UNDELEGATIONS;
import static wannabit.io.cosmostaion.network.ChannelBuilder.TIME_OUT;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cosmos.staking.v1beta1.QueryGrpc;
import cosmos.staking.v1beta1.QueryOuterClass;
import cosmos.staking.v1beta1.Staking;
import kotlin.Deprecated;
import wannabit.io.cosmostaion.base.BaseApplication;
import com.fulldive.wallet.models.BaseChain;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.network.ChannelBuilder;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WLog;

@Deprecated(message = "Migrate to rx")
public class UnDelegationsGrpcTask extends CommonTask {
    private final Account mAccount;
    private final ArrayList<Staking.UnbondingDelegation> mResultData = new ArrayList<>();
    private final QueryGrpc.QueryBlockingStub mStub;

    public UnDelegationsGrpcTask(BaseApplication app, TaskListener listener, BaseChain chain, Account account) {
        super(app, listener);
        this.mAccount = account;
        this.result.taskType = TASK_GRPC_FETCH_UNDELEGATIONS;
        this.mStub = QueryGrpc.newBlockingStub(ChannelBuilder.getChain(chain)).withDeadlineAfter(TIME_OUT, TimeUnit.SECONDS);
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            QueryOuterClass.QueryDelegatorUnbondingDelegationsRequest request = QueryOuterClass.QueryDelegatorUnbondingDelegationsRequest.newBuilder().setDelegatorAddr(mAccount.address).build();
            QueryOuterClass.QueryDelegatorUnbondingDelegationsResponse response = mStub.delegatorUnbondingDelegations(request);
            mResultData.addAll(response.getUnbondingResponsesList());
            this.result.isSuccess = true;
            this.result.resultData = mResultData;

        } catch (Exception e) {
            WLog.e("UnDelegationsGrpcTask " + e.getMessage());
        }
        return result;
    }
}
