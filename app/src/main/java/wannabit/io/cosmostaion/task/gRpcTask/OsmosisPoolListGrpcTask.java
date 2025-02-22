package wannabit.io.cosmostaion.task.gRpcTask;


import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_OSMOSIS_POOL_LIST;
import static wannabit.io.cosmostaion.network.ChannelBuilder.TIME_OUT;

import com.fulldive.wallet.models.BaseChain;
import com.google.protobuf2.Any;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cosmos.base.query.v1beta1.Pagination;
import kotlin.Deprecated;
import osmosis.gamm.poolmodels.balancer.BalancerPool;
import osmosis.gamm.v1beta1.QueryGrpc;
import osmosis.gamm.v1beta1.QueryOuterClass;
import wannabit.io.cosmostaion.base.BaseApplication;
import wannabit.io.cosmostaion.network.ChannelBuilder;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WLog;

@Deprecated(message = "Migrate to rx")
public class OsmosisPoolListGrpcTask extends CommonTask {
    private final QueryGrpc.QueryBlockingStub mStub;
    private final ArrayList<BalancerPool.Pool> mResultData = new ArrayList<>();

    public OsmosisPoolListGrpcTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.result.taskType = TASK_GRPC_FETCH_OSMOSIS_POOL_LIST;
        this.mStub = QueryGrpc.newBlockingStub(ChannelBuilder.getChain(chain)).withDeadlineAfter(TIME_OUT, TimeUnit.SECONDS);
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Pagination.PageRequest pageRequest = Pagination.PageRequest.newBuilder().setLimit(1000).build();
            QueryOuterClass.QueryPoolsRequest request = QueryOuterClass.QueryPoolsRequest.newBuilder().setPagination(pageRequest).build();
            QueryOuterClass.QueryPoolsResponse response = mStub.pools(request);

            for (Any pool : response.getPoolsList()) {
                mResultData.add(BalancerPool.Pool.parseFrom(pool.getValue()));
            }
            result.resultData = mResultData;
            result.isSuccess = true;

        } catch (Exception e) {
            WLog.e("OsmosisGrpcPoolListTask " + e.getMessage());
        }
        return result;
    }

}
