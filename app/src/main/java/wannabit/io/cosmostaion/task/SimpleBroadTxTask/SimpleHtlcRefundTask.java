package wannabit.io.cosmostaion.task.SimpleBroadTxTask;

import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GEN_TX_HTLC_REFUND;

import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.models.WalletBalance;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.DeterministicKey;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseApplication;
import wannabit.io.cosmostaion.base.BaseConstant;
import wannabit.io.cosmostaion.cosmos.MsgGenerator;
import wannabit.io.cosmostaion.crypto.CryptoHelper;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.dao.Balance;
import wannabit.io.cosmostaion.model.type.Fee;
import wannabit.io.cosmostaion.model.type.Msg;
import wannabit.io.cosmostaion.network.ApiClient;
import wannabit.io.cosmostaion.network.req.ReqBroadCast;
import wannabit.io.cosmostaion.network.res.ResBroadTx;
import wannabit.io.cosmostaion.network.res.ResLcdKavaAccountInfo;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WKey;
import wannabit.io.cosmostaion.utils.WLog;
import wannabit.io.cosmostaion.utils.WUtil;

public class SimpleHtlcRefundTask extends CommonTask {

    private Account mAccount;
    private final String mSwapId;
    private final String mMemo;
    private final Fee mFees;

    public SimpleHtlcRefundTask(BaseApplication app, TaskListener listener,
                                Account account, String swapid, String memo, Fee fees) {
        super(app, listener);
        this.mAccount = account;
        this.mSwapId = swapid;
        this.mMemo = memo;
        this.mFees = fees;
        this.result.taskType = TASK_GEN_TX_HTLC_REFUND;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN.INSTANCE)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaChain(context).getAccountInfo(mAccount.address).execute();
                if (!response.isSuccessful()) {
                    result.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return result;
                }
                final Account account = WUtil.getAccountFromKavaLcd(mAccount.id, response.body());
                accountsInteractor.updateAccount(
                        mAccount.id,
                        account.address,
                        account.sequenceNumber,
                        account.accountNumber
                );

                final List<WalletBalance> balances = WUtil.getBalancesFromKavaLcd(mAccount.id, response.body());
                final Throwable error = balancesInteractor.updateBalances(mAccount.id, balances).blockingGet();
                if (error != null) {
                    error.printStackTrace();
                }

                mAccount = accountsInteractor.getAccount(mAccount.id).blockingGet();
            }

            ECKey ecKey;
            if (mAccount.fromMnemonic) {
                String entropy = CryptoHelper.decryptData(context.getString(R.string.key_mnemonic) + mAccount.uuid, mAccount.resource, mAccount.spec);
                DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(mAccount, entropy);
                ecKey = ECKey.fromPrivate(new BigInteger(deterministicKey.getPrivateKeyAsHex(), 16));
            } else {
                String privateKey = CryptoHelper.decryptData(context.getString(R.string.key_private) + mAccount.uuid, mAccount.resource, mAccount.spec);
                ecKey = ECKey.fromPrivate(new BigInteger(privateKey, 16));
            }

            Msg refundMsg = MsgGenerator.genRefundAtomicSwap(mAccount.address, mSwapId, BaseChain.getChain(mAccount.baseChain));
            ArrayList<Msg> msgs = new ArrayList<>();
            msgs.add(refundMsg);

            WLog.w("refundMsg : " + WUtil.prettyPrinter(refundMsg));

            ReqBroadCast reqBroadCast = MsgGenerator.getBroadcaseReq(mAccount, msgs, mFees, mMemo, ecKey, context.getBaseDao().getChainId());
            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN.INSTANCE)) {
                Response<ResBroadTx> response = ApiClient.getKavaChain(context).broadTx(reqBroadCast).execute();
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
                    result.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            }

        } catch (Exception e) {
            WLog.w("Exception " + e.getMessage());
        }
        return result;
    }
}
