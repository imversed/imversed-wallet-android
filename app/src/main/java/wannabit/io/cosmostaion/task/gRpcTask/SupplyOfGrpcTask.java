package wannabit.io.cosmostaion.task.gRpcTask;

import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_SUPPLY_OF_INFO;
import static wannabit.io.cosmostaion.network.ChannelBuilder.TIME_OUT;

import com.fulldive.wallet.models.BaseChain;

import java.util.concurrent.TimeUnit;

import cosmos.bank.v1beta1.QueryGrpc;
import cosmos.bank.v1beta1.QueryOuterClass;
import wannabit.io.cosmostaion.base.BaseApplication;
import wannabit.io.cosmostaion.network.ChannelBuilder;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WLog;

public class SupplyOfGrpcTask extends CommonTask {
    private final BaseChain mChain;
    private final String mDenom;
    private final QueryGrpc.QueryBlockingStub mStub;

    public SupplyOfGrpcTask(BaseApplication app, TaskListener listener, BaseChain chain, String denom) {
        super(app, listener);
        this.mChain = chain;
        this.mDenom = denom;
        this.result.taskType = TASK_GRPC_FETCH_SUPPLY_OF_INFO;
        this.mStub = cosmos.bank.v1beta1.QueryGrpc.newBlockingStub(ChannelBuilder.getChain(mChain)).withDeadlineAfter(TIME_OUT, TimeUnit.SECONDS);
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            QueryOuterClass.QuerySupplyOfRequest request = QueryOuterClass.QuerySupplyOfRequest.newBuilder().setDenom(mDenom).build();
            QueryOuterClass.QuerySupplyOfResponse response = mStub.supplyOf(request);

            result.isSuccess = true;
            result.resultData = response;

        } catch (Exception e) {
            WLog.e("SupplyOfGrpcTask " + e.getMessage());
        }
        return result;
    }
}
