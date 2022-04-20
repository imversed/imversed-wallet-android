package wannabit.io.cosmostaion.activities.chains.nft;

import static com.fulldive.wallet.models.BaseChain.CRYPTO_MAIN;
import static com.fulldive.wallet.models.BaseChain.IRIS_MAIN;
import static wannabit.io.cosmostaion.base.BaseConstant.CONST_PW_TX_MINT_NFT;
import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_NFTOKEN_LIST;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.protobuf.ByteString;

import java.math.BigDecimal;
import java.util.ArrayList;

import irismod.nft.Nft;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.dialog.Dialog_WatchMode;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.task.gRpcTask.NFTokenListGrpcTask;
import wannabit.io.cosmostaion.utils.WUtil;
import wannabit.io.cosmostaion.widget.NftMyHolder;

public class NFTListActivity extends BaseActivity implements TaskListener {

    private Toolbar mToolbar;
    private TextView mTitle;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TextView mEmptyNfts;
    private RelativeLayout mLoadingLayer, mBtnCreateNft;

    private NFTListAdapter mNFTListAdapter;

    private final ArrayList<Nft.IDCollection> mMyIrisNFTs = new ArrayList<>();
    private final ArrayList<chainmain.nft.v1.Nft.IDCollection> mMyCryptoNFTs = new ArrayList<>();
    private final ArrayList<String> mTokenIds = new ArrayList<>();

    private ByteString mPageKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nft_list);
        mToolbar = findViewById(R.id.toolbar);
        mTitle = findViewById(R.id.toolbarTitleTextView);
        mSwipeRefreshLayout = findViewById(R.id.layer_refresher);
        mRecyclerView = findViewById(R.id.recycler);
        mEmptyNfts = findViewById(R.id.empty_nfts);
        mLoadingLayer = findViewById(R.id.loadingLayer);
        mBtnCreateNft = findViewById(R.id.btn_create_nft);
        mTitle.setText(getString(R.string.str_nft_c));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(() -> onFetchNftListInfo());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mNFTListAdapter = new NFTListAdapter();
        mRecyclerView.setAdapter(mNFTListAdapter);

        mBtnCreateNft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAccount() == null) return;
                if (!getAccount().hasPrivateKey) {
                    Dialog_WatchMode dialog = Dialog_WatchMode.newInstance();
                    showDialog(dialog);
                    return;
                }
                Intent intent = new Intent(NFTListActivity.this, NFTCreateActivity.class);
                BigDecimal mainAvailable = getBaseDao().getAvailable(getBaseChain().getMainDenom());
                BigDecimal feeAmount = WUtil.getEstimateGasFeeAmount(NFTListActivity.this, getBaseChain(), CONST_PW_TX_MINT_NFT, 0);
                if (mainAvailable.compareTo(feeAmount) <= 0) {
                    Toast.makeText(NFTListActivity.this, R.string.error_not_enough_budget, Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(intent);
            }
        });

        onFetchNftListInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onUpdateView() {
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadingLayer.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mNFTListAdapter.notifyDataSetChanged();
    }

    public void onFetchNftListInfo() {
        mTaskCount = 1;
        mMyIrisNFTs.clear();
        mMyCryptoNFTs.clear();
        mTokenIds.clear();
        new NFTokenListGrpcTask(getBaseApplication(), this, getBaseChain(), getAccount(), mPageKey).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onTaskResponse(TaskResult result) {
        if (isFinishing()) return;
        mTaskCount--;
        if (result.taskType == TASK_GRPC_FETCH_NFTOKEN_LIST) {
            if (result.isSuccess && result.resultData != null && result.resultByteData != null) {
                if (getBaseChain().equals(IRIS_MAIN.INSTANCE)) {
                    ArrayList<Nft.IDCollection> tempList = (ArrayList<Nft.IDCollection>) result.resultData;
                    mPageKey = result.resultByteData;
                    if (tempList.size() > 0) {
                        for (Nft.IDCollection collection : tempList) {
                            for (String tokenId : collection.getTokenIdsList()) {
                                mMyIrisNFTs.add(collection);
                                mTokenIds.add(tokenId);
                            }
                        }
                    } else {
                        mEmptyNfts.setVisibility(View.VISIBLE);
                    }

                } else if (getBaseChain().equals(CRYPTO_MAIN.INSTANCE)) {
                    ArrayList<chainmain.nft.v1.Nft.IDCollection> tempList = (ArrayList<chainmain.nft.v1.Nft.IDCollection>) result.resultData;
                    mPageKey = result.resultByteData;
                    if (tempList.size() > 0) {
                        for (chainmain.nft.v1.Nft.IDCollection collection : tempList) {
                            for (String tokenId : collection.getTokenIdsList()) {
                                mMyCryptoNFTs.add(collection);
                                mTokenIds.add(tokenId);
                            }
                        }
                    } else {
                        mEmptyNfts.setVisibility(View.VISIBLE);
                    }
                }
            }

        }

        if (mTaskCount == 0) {
            if (mPageKey.size() == 0) {
                onUpdateView();
            } else {
                onFetchNftListInfo();
            }
        }
    }

    private class NFTListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new NftMyHolder(getLayoutInflater().inflate(R.layout.item_nft_list, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            final NftMyHolder holder = (NftMyHolder) viewHolder;
            if (getBaseChain().equals(IRIS_MAIN.INSTANCE)) {
                if (mMyIrisNFTs != null && mMyIrisNFTs.size() > 0) {
                    final Nft.IDCollection collection = mMyIrisNFTs.get(position);
                    final String tokenId = mTokenIds.get(position);
                    holder.onBindNFT(NFTListActivity.this, collection.getDenomId(), tokenId);
                }

            } else if (getBaseChain().equals(CRYPTO_MAIN.INSTANCE)) {
                if (mMyCryptoNFTs != null && mMyCryptoNFTs.size() > 0) {
                    final chainmain.nft.v1.Nft.IDCollection collection = mMyCryptoNFTs.get(position);
                    final String tokenId = mTokenIds.get(position);
                    holder.onBindNFT(NFTListActivity.this, collection.getDenomId(), tokenId);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (getBaseChain().equals(IRIS_MAIN.INSTANCE)) {
                return mMyIrisNFTs.size();
            } else if (getBaseChain().equals(CRYPTO_MAIN.INSTANCE)) {
                return mMyCryptoNFTs.size();
            } else {
                return 0;
            }
        }
    }
}

