package wannabit.io.cosmostaion.task.gRpcTask;

import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_GRAVITY_POOL_INFO;
import static wannabit.io.cosmostaion.network.ChannelBuilder.TIME_OUT;

import com.fulldive.wallet.models.BaseChain;

import java.util.concurrent.TimeUnit;

import tendermint.liquidity.v1beta1.QueryGrpc;
import tendermint.liquidity.v1beta1.QueryOuterClass;
import wannabit.io.cosmostaion.base.BaseApplication;
import wannabit.io.cosmostaion.network.ChannelBuilder;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WLog;

public class GravityDexPoolInfoGrpcTask extends CommonTask {
    private final BaseChain mChain;
    private final long mPoolId;
    private final QueryGrpc.QueryBlockingStub mStub;

    public GravityDexPoolInfoGrpcTask(BaseApplication app, TaskListener listener, BaseChain chain, long poolId) {
        super(app, listener);
        this.mChain = chain;
        this.mPoolId = poolId;
        this.result.taskType = TASK_GRPC_FETCH_GRAVITY_POOL_INFO;
        this.mStub = QueryGrpc.newBlockingStub(ChannelBuilder.getChain(mChain)).withDeadlineAfter(TIME_OUT, TimeUnit.SECONDS);
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            QueryOuterClass.QueryLiquidityPoolRequest request = QueryOuterClass.QueryLiquidityPoolRequest.newBuilder().setPoolId(mPoolId).build();
            QueryOuterClass.QueryLiquidityPoolResponse response = mStub.liquidityPool(request);

            result.resultData = response.getPool();
            result.isSuccess = true;

        } catch (Exception e) {
            WLog.e("GravityDexPoolGrpcTask " + e.getMessage());
        }
        return result;
    }
}
