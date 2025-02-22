package wannabit.io.cosmostaion.task.gRpcTask;

import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_SIF_MY_PROVIDER;
import static wannabit.io.cosmostaion.network.ChannelBuilder.TIME_OUT;

import com.fulldive.wallet.models.BaseChain;

import java.util.concurrent.TimeUnit;

import sifnode.clp.v1.Querier;
import sifnode.clp.v1.QueryGrpc;
import wannabit.io.cosmostaion.base.BaseApplication;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.network.ChannelBuilder;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WLog;

public class SifDexMyProviderGrpcTask extends CommonTask {
    private final BaseChain mChain;
    private final Account mAccount;
    private final String mDenom;
    private final QueryGrpc.QueryBlockingStub mStub;

    public SifDexMyProviderGrpcTask(BaseApplication app, TaskListener listener, BaseChain chain, Account account, String denom) {
        super(app, listener);
        this.mChain = chain;
        this.mAccount = account;
        this.mDenom = denom;
        this.result.taskType = TASK_GRPC_FETCH_SIF_MY_PROVIDER;
        this.mStub = QueryGrpc.newBlockingStub(ChannelBuilder.getChain(mChain)).withDeadlineAfter(TIME_OUT, TimeUnit.SECONDS);
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Querier.LiquidityProviderReq request = Querier.LiquidityProviderReq.newBuilder().setSymbol(mDenom).setLpAddress(mAccount.address).build();
            Querier.LiquidityProviderRes response = mStub.getLiquidityProvider(request);

            result.resultData = response;
            result.isSuccess = true;

        } catch (Exception e) {
            WLog.e("SifDexMyProviderGrpcTask " + e.getMessage());
        }
        return result;
    }
}
