package wannabit.io.cosmostaion.task.gRpcTask;

import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_STARNAME_DOMAIN_INFO;
import static wannabit.io.cosmostaion.network.ChannelBuilder.TIME_OUT;

import com.fulldive.wallet.models.BaseChain;

import java.util.concurrent.TimeUnit;

import starnamed.x.starname.v1beta1.QueryGrpc;
import starnamed.x.starname.v1beta1.QueryOuterClass;
import wannabit.io.cosmostaion.base.BaseApplication;
import wannabit.io.cosmostaion.network.ChannelBuilder;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WLog;

public class StarNameGrpcDomainInfoTask extends CommonTask {
    private final BaseChain mBaseChain;
    private final String mDomainName;
    private final QueryGrpc.QueryBlockingStub mStub;

    public StarNameGrpcDomainInfoTask(BaseApplication app, TaskListener listener, BaseChain basecahin, String domainName) {
        super(app, listener);
        this.mBaseChain = basecahin;
        this.mDomainName = domainName;
        this.result.taskType = TASK_GRPC_FETCH_STARNAME_DOMAIN_INFO;
        this.mStub = QueryGrpc.newBlockingStub(ChannelBuilder.getChain(mBaseChain)).withDeadlineAfter(TIME_OUT, TimeUnit.SECONDS);

    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            QueryOuterClass.QueryDomainRequest request = QueryOuterClass.QueryDomainRequest.newBuilder().setName(mDomainName).build();
            QueryOuterClass.QueryDomainResponse response = mStub.domain(request);
            result.resultData = response.getDomain();
            result.isSuccess = true;

        } catch (Exception e) {
            WLog.e("StarNameGrpcDomainInfoTask " + e.getMessage());
        }
        return result;
    }
}
