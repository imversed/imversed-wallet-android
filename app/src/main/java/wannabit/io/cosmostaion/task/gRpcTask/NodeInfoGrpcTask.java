package wannabit.io.cosmostaion.task.gRpcTask;

import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_NODE_INFO;
import static wannabit.io.cosmostaion.network.ChannelBuilder.TIME_OUT;

import com.fulldive.wallet.models.BaseChain;

import java.util.concurrent.TimeUnit;

import cosmos.base.tendermint.v1beta1.Query;
import cosmos.base.tendermint.v1beta1.ServiceGrpc;
import kotlin.Deprecated;
import wannabit.io.cosmostaion.base.BaseApplication;
import wannabit.io.cosmostaion.network.ChannelBuilder;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WLog;

@Deprecated(message = "Migrate to rx")
public class NodeInfoGrpcTask extends CommonTask {
    private final ServiceGrpc.ServiceBlockingStub mStub;

    public NodeInfoGrpcTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.result.taskType = TASK_GRPC_FETCH_NODE_INFO;
        this.mStub = ServiceGrpc.newBlockingStub(ChannelBuilder.getChain(chain)).withDeadlineAfter(TIME_OUT, TimeUnit.SECONDS);
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Query.GetNodeInfoRequest request = Query.GetNodeInfoRequest.newBuilder().build();
            Query.GetNodeInfoResponse response = mStub.getNodeInfo(request);

            this.result.isSuccess = true;
            result.resultData = response.getNodeInfo();

        } catch (Exception e) {
            WLog.e("NodeInfoGrpcTask " + e.getMessage());
        }
        return result;
    }
}
