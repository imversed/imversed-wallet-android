package wannabit.io.cosmostaion.task.SimpleBroadTxTask;

import static wannabit.io.cosmostaion.base.BaseConstant.ERROR_CODE_BROADCAST;
import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GEN_TX_OK_DIRECT_VOTE;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.DeterministicKey;

import java.math.BigInteger;
import java.util.ArrayList;

import retrofit2.Response;
import wannabit.io.cosmostaion.BuildConfig;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseApplication;
import wannabit.io.cosmostaion.base.BaseConstant;
import wannabit.io.cosmostaion.cosmos.MsgGenerator;
import wannabit.io.cosmostaion.crypto.CryptoHelper;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.model.type.Fee;
import wannabit.io.cosmostaion.model.type.Msg;
import wannabit.io.cosmostaion.network.ApiClient;
import wannabit.io.cosmostaion.network.req.ReqBroadCast;
import wannabit.io.cosmostaion.network.res.ResBroadTx;
import wannabit.io.cosmostaion.network.res.ResOkAccountInfo;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WKey;
import wannabit.io.cosmostaion.utils.WUtil;

public class SimpleOkDirectVoteTask extends CommonTask {

    private final Account mAccount;
    private final ArrayList<String> mToValidators;
    private final String mMemo;
    private final Fee mFees;

    public SimpleOkDirectVoteTask(BaseApplication app, TaskListener listener, Account account, ArrayList<String> toVals, String memo, Fee fees) {
        super(app, listener);
        this.mAccount = account;
        this.mToValidators = toVals;
        this.mMemo = memo;
        this.mFees = fees;
        this.result.taskType = TASK_GEN_TX_OK_DIRECT_VOTE;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (!checkPassword(strings[0])) {
                result.isSuccess = false;
                result.errorCode = BaseConstant.ERROR_CODE_INVALID_PASSWORD;
                return result;
            }

            Response<ResOkAccountInfo> accountResponse = ApiClient.getOkexChain(context).getAccountInfo(mAccount.address).execute();
            if (!accountResponse.isSuccessful()) {
                result.errorCode = ERROR_CODE_BROADCAST;
                return result;
            }
            final Account account = WUtil.getAccountFromOkLcd(mAccount.id, accountResponse.body());
            accountsInteractor.updateAccount(
                    mAccount.id,
                    account.address,
                    account.sequenceNumber,
                    account.accountNumber
            );

            ECKey ecKey;
            if (mAccount.fromMnemonic) {
                String entropy = CryptoHelper.decryptData(context.getString(R.string.key_mnemonic) + mAccount.uuid, mAccount.resource, mAccount.spec);
                DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(mAccount, entropy);
                ecKey = ECKey.fromPrivate(new BigInteger(deterministicKey.getPrivateKeyAsHex(), 16));
            } else {
                String privateKey = CryptoHelper.decryptData(context.getString(R.string.key_private) + mAccount.uuid, mAccount.resource, mAccount.spec);
                ecKey = ECKey.fromPrivate(new BigInteger(privateKey, 16));
            }

            Msg incentiveMsg = MsgGenerator.genOkVote(mAccount.address, mToValidators);
            ArrayList<Msg> msgs = new ArrayList<>();
            msgs.add(incentiveMsg);

            ReqBroadCast reqBroadCast = MsgGenerator.getOKexBroadcaseReq(mAccount, msgs, mFees, mMemo, ecKey, context.getBaseDao().getChainId());
            Response<ResBroadTx> response = ApiClient.getOkexChain(context).broadTx(reqBroadCast).execute();
            if (response.isSuccessful() && response.body() != null) {
                if (response.body().txhash != null) {
                    result.resultData = response.body().txhash;
                }
                if (response.body().code != null) {
                    result.errorCode = response.body().code;
                    result.errorMsg = response.body().raw_log;
                    return result;
                }
                result.isSuccess = true;

            } else {
                result.errorCode = ERROR_CODE_BROADCAST;
            }

        } catch (Exception e) {
            if (BuildConfig.DEBUG) e.printStackTrace();
        }

        return result;
    }
}
