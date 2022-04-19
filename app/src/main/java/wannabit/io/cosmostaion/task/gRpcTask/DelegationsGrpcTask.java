package wannabit.io.cosmostaion.task.gRpcTask;

import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_DELEGATIONS;
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
public class DelegationsGrpcTask extends CommonTask {
    private final Account mAccount;
    private final ArrayList<Staking.DelegationResponse> mResultData = new ArrayList<>();
    private final QueryGrpc.QueryBlockingStub mStub;

    public DelegationsGrpcTask(BaseApplication app, TaskListener listener, BaseChain chain, Account account) {
        super(app, listener);
        this.mAccount = account;
        this.result.taskType = TASK_GRPC_FETCH_DELEGATIONS;
        this.mStub = QueryGrpc.newBlockingStub(ChannelBuilder.getChain(chain)).withDeadlineAfter(TIME_OUT, TimeUnit.SECONDS);
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            QueryOuterClass.QueryDelegatorDelegationsRequest requset = QueryOuterClass.QueryDelegatorDelegationsRequest.newBuilder().setDelegatorAddr(mAccount.address).build();
            QueryOuterClass.QueryDelegatorDelegationsResponse response = mStub.delegatorDelegations(requset);
            mResultData.addAll(response.getDelegationResponsesList());
            this.result.isSuccess = true;
            this.result.resultData = mResultData;

        } catch (Exception e) {
            WLog.e("DelegationsGrpc " + e.getMessage());
        }
        return result;
    }
}
