package wannabit.io.cosmostaion.task.FetchTask;

import static com.fulldive.wallet.models.BaseChain.BNB_MAIN;
import static com.fulldive.wallet.models.BaseChain.OKEX_MAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.TASK_FETCH_NODE_INFO;

import kotlin.Deprecated;
import retrofit2.Response;
import wannabit.io.cosmostaion.base.BaseApplication;
import com.fulldive.wallet.models.BaseChain;
import wannabit.io.cosmostaion.network.ApiClient;
import wannabit.io.cosmostaion.network.res.ResNodeInfo;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WLog;

@Deprecated(message = "Migrate to rx")
public class NodeInfoTask extends CommonTask {
    private final BaseChain mChain;

    public NodeInfoTask(BaseApplication app, TaskListener listener, BaseChain chain) {
        super(app, listener);
        this.mChain = chain;
        this.result.taskType = TASK_FETCH_NODE_INFO;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mChain.equals(BNB_MAIN.INSTANCE)) {
                Response<ResNodeInfo> response = ApiClient.getBnbChain(context).getNodeInfo().execute();
                if (response.isSuccessful() && response.body() != null && response.body().node_info != null) {
                    result.resultData = response.body().node_info;
                    result.isSuccess = true;

                } else {
                    WLog.w("NodeInfoTask : NOk");
                }

            } else if (mChain.equals(OKEX_MAIN.INSTANCE)) {
                Response<ResNodeInfo> response = ApiClient.getOkexChain(context).getNodeInfo().execute();
                if (response.isSuccessful() && response.body() != null && response.body().node_info != null) {
                    result.resultData = response.body().node_info;
                    result.isSuccess = true;

                } else {
                    WLog.w("NodeInfoTask : NOk");
                }

            }

        } catch (Exception e) {
            WLog.w("NodeInfoTask Error " + e.getMessage());
        }
        return result;
    }
}
