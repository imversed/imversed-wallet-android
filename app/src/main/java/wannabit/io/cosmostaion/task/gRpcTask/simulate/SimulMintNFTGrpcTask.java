package wannabit.io.cosmostaion.task.gRpcTask.simulate;

import static com.fulldive.wallet.models.BaseChain.CRYPTO_MAIN;
import static com.fulldive.wallet.models.BaseChain.IRIS_MAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_SIMULATE_MINT_NFT;

import com.fulldive.wallet.models.BaseChain;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.DeterministicKey;

import java.math.BigInteger;

import cosmos.auth.v1beta1.QueryGrpc;
import cosmos.auth.v1beta1.QueryOuterClass;
import cosmos.tx.v1beta1.ServiceGrpc;
import cosmos.tx.v1beta1.ServiceOuterClass;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseApplication;
import wannabit.io.cosmostaion.cosmos.Signer;
import wannabit.io.cosmostaion.crypto.CryptoHelper;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.model.type.Fee;
import wannabit.io.cosmostaion.network.ChannelBuilder;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WKey;
import wannabit.io.cosmostaion.utils.WLog;

public class SimulMintNFTGrpcTask extends CommonTask {

    private final Account mAccount;
    private final BaseChain mBaseChain;
    private final String mSigner;
    private final String mDenomId;
    private final String mDenomName;
    private final String mId;
    private final String mName;
    private final String mUri;
    private final String mData;
    private final Fee mFees;
    private final String mMemo;
    private final String mChainId;

    private QueryOuterClass.QueryAccountResponse mAuthResponse;
    private ECKey ecKey;

    public SimulMintNFTGrpcTask(BaseApplication app, TaskListener listener, Account account, BaseChain basechain, String signer, String denomId, String denomName,
                                String id, String name, String uri, String data, String memo, Fee fee, String chainId) {
        super(app, listener);
        this.mAccount = account;
        this.mBaseChain = basechain;
        this.mSigner = signer;
        this.mDenomId = denomId;
        this.mDenomName = denomName;
        this.mId = id;
        this.mName = name;
        this.mUri = uri;
        this.mData = data;
        this.mMemo = memo;
        this.mFees = fee;
        this.mChainId = chainId;
        this.result.taskType = TASK_GRPC_SIMULATE_MINT_NFT;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            if (mAccount.fromMnemonic) {
                String entropy = CryptoHelper.decryptData(context.getString(R.string.key_mnemonic) + mAccount.uuid, mAccount.resource, mAccount.spec);
                DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(mAccount, entropy);
                ecKey = ECKey.fromPrivate(new BigInteger(deterministicKey.getPrivateKeyAsHex(), 16));
            } else {
                String privateKey = CryptoHelper.decryptData(context.getString(R.string.key_private) + mAccount.uuid, mAccount.resource, mAccount.spec);
                ecKey = ECKey.fromPrivate(new BigInteger(privateKey, 16));
            }

            //simulate
            QueryGrpc.QueryBlockingStub authStub = QueryGrpc.newBlockingStub(ChannelBuilder.getChain(mBaseChain));
            QueryOuterClass.QueryAccountRequest request = QueryOuterClass.QueryAccountRequest.newBuilder().setAddress(mAccount.address).build();
            mAuthResponse = authStub.account(request);

            ServiceOuterClass.SimulateRequest simulateTxRequest = null;
            ServiceGrpc.ServiceBlockingStub txService = ServiceGrpc.newBlockingStub(ChannelBuilder.getChain(mBaseChain));
            if (mBaseChain.equals(IRIS_MAIN.INSTANCE)) {
                simulateTxRequest = Signer.getGrpcCreateNftIrisSimulateReq(mAuthResponse, mSigner, mDenomId, mDenomName, mId, mName, mUri, mData, mFees, mMemo, ecKey, mChainId);
            } else if (mBaseChain.equals(CRYPTO_MAIN.INSTANCE)) {
                simulateTxRequest = Signer.getGrpcCreateNftCroSimulateReq(mAuthResponse, mSigner, mDenomId, mDenomName, mId, mName, mUri, mData, mFees, mMemo, ecKey, mChainId);
            }
            if (simulateTxRequest != null) {
                ServiceOuterClass.SimulateResponse response = txService.simulate(simulateTxRequest);
                result.resultData = response.getGasInfo();
                result.isSuccess = true;
            }

        } catch (Exception e) {
            WLog.e("SimulMintNFTGrpcTask " + e.getMessage());
            result.isSuccess = false;
        }
        return result;
    }

}
