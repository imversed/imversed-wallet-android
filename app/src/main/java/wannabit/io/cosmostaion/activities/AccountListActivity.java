package wannabit.io.cosmostaion.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fulldive.wallet.models.BaseChain;

import java.util.ArrayList;
import java.util.List;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.utils.WDp;

public class AccountListActivity extends BaseActivity implements View.OnClickListener {

    private TextView editButton;
    private RecyclerView chainRecyclerView;

    private ChainListAdapter mChainListAdapter;
    private AccountListAdapter mAccountListAdapter;
    private BaseChain mSelectedChain;
    private ArrayList<BaseChain> mDisplayChains = new ArrayList<>();
    private List<Account> mDisplayAccounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        editButton = findViewById(R.id.btn_edit);
        chainRecyclerView = findViewById(R.id.chain_recycler);
        RecyclerView accountRecyclerView = findViewById(R.id.recyclerView);

        mSelectedChain = getBaseChain();

        editButton.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chainRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        chainRecyclerView.setHasFixedSize(true);
        mChainListAdapter = new ChainListAdapter();
        chainRecyclerView.setAdapter(mChainListAdapter);

        accountRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        accountRecyclerView.setHasFixedSize(true);
        mAccountListAdapter = new AccountListAdapter();
        accountRecyclerView.setAdapter(mAccountListAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onChainSelect(mSelectedChain);
    }

    public void onChainSelect(BaseChain baseChain) {
        mDisplayChains = getBaseDao().dpSortedChains();
        mSelectedChain = baseChain;
        mDisplayAccounts = accountsInteractor.getChainAccounts(mSelectedChain);
        chainRecyclerView.scrollToPosition(mDisplayChains.indexOf(baseChain));

        mChainListAdapter.notifyDataSetChanged();
        mAccountListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(editButton)) {
            Intent intent = new Intent(AccountListActivity.this, WalletEditActivity.class);
            startActivity(intent);
        }
    }

    private class ChainListAdapter extends RecyclerView.Adapter<ChainListAdapter.ChainHolder> {

        @NonNull
        @Override
        public ChainListAdapter.ChainHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new ChainHolder(getLayoutInflater().inflate(R.layout.item_accountlist_chain, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ChainListAdapter.ChainHolder holder, final int position) {
            BaseChain chain = mDisplayChains.get(position);
            holder.chainCard.setOnClickListener(v -> {
                if (chain != mSelectedChain) {
                    new Handler().postDelayed(() -> {
                        onChainSelect(chain);
                        getBaseDao().setUserSortedBaseChains(mDisplayChains);
                    }, 150);
                }
            });
            holder.chainImg.setImageResource(chain.getChainIcon());
            holder.chainName.setText(chain.getChainAlterTitle());

            if (chain.equals(mSelectedChain)) {
                holder.chainCard.setBackgroundResource(R.drawable.box_chain_selected);
                holder.chainImg.setAlpha(1f);
                holder.chainName.setTextColor(getColor(R.color.colorWhite));
            } else {
                holder.chainCard.setBackgroundResource(R.drawable.box_chain_unselected);
                holder.chainImg.setAlpha(0.5f);
                holder.chainName.setTextColor(getColor(R.color.colorGray4));
            }

        }

        @Override
        public int getItemCount() {
            return mDisplayChains.size();
        }


        public class ChainHolder extends RecyclerView.ViewHolder {
            FrameLayout chainCard;
            LinearLayout chainLayer;
            ImageView chainImg;
            TextView chainName;

            public ChainHolder(@NonNull View itemView) {
                super(itemView);
                chainCard = itemView.findViewById(R.id.chainCard);
                chainLayer = itemView.findViewById(R.id.chainLayer);
                chainImg = itemView.findViewById(R.id.chainImg);
                chainName = itemView.findViewById(R.id.chainName);
            }
        }
    }


    private class AccountListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new AccountHolder(getLayoutInflater().inflate(R.layout.item_accountlist_account, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            final AccountHolder holder = (AccountHolder) viewHolder;
            final Account account = mDisplayAccounts.get(position);
            final String lastTotal = accountsInteractor.getLastTotal(account.id);
            WDp.DpMainDenom(account.baseChain, holder.accountDenom);
            holder.accountAddress.setText(account.address);
            holder.accountAvailable.setText(account.getLastTotal(BaseChain.getChain(account.baseChain), lastTotal));
            holder.accountKeyState.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorGray0), android.graphics.PorterDuff.Mode.SRC_IN);
            if (account.hasPrivateKey) {
                holder.accountKeyState.setColorFilter(WDp.getChainColor(getBaseContext(), BaseChain.getChain(account.baseChain)), android.graphics.PorterDuff.Mode.SRC_IN);
            }

            holder.accountName.setText(account.getAccountTitle(getBaseContext()));

            holder.accountCard.setOnClickListener(v -> {
                Intent intent = new Intent(AccountListActivity.this, AccountDetailActivity.class);
                intent.putExtra("id", account.id);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return mDisplayAccounts.size();
        }

        public class AccountHolder extends RecyclerView.ViewHolder {
            FrameLayout accountCard;
            LinearLayout accountContent;
            ImageView accountArrowSort, accountKeyState;
            TextView accountName, accountAddress, accountAvailable, accountDenom;

            public AccountHolder(@NonNull View itemView) {
                super(itemView);
                accountCard = itemView.findViewById(R.id.accountCard);
                accountArrowSort = itemView.findViewById(R.id.accountArrowSort);
                accountContent = itemView.findViewById(R.id.accountContent);
                accountKeyState = itemView.findViewById(R.id.accountKeyState);
                accountName = itemView.findViewById(R.id.accountName);
                accountAddress = itemView.findViewById(R.id.accountAddress);
                accountAvailable = itemView.findViewById(R.id.accountAvailable);
                accountDenom = itemView.findViewById(R.id.accountDenom);
            }
        }
    }
}