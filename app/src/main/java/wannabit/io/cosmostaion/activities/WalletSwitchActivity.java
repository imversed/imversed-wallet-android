package wannabit.io.cosmostaion.activities;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fulldive.wallet.models.BaseChain;
import com.fulldive.wallet.rx.AppSchedulers;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.dao.ChainAccounts;
import wannabit.io.cosmostaion.utils.WLog;
import wannabit.io.cosmostaion.widget.mainWallet.ManageChainSwitchHolder;

public class WalletSwitchActivity extends BaseActivity {
    private RecyclerView accountRecyclerView;
    private AccountListAdapter accountListAdapter;

    private ArrayList<BaseChain> expandedChains = new ArrayList<>();
    private final ArrayList<ChainAccounts> chainAccounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_switch);

        ImageView closeButton = findViewById(R.id.btn_close);
        accountRecyclerView = findViewById(R.id.account_recycler);

        closeButton.setOnClickListener(v -> {
            saveExpandedChains();
            finish();
        });
        accountRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        accountRecyclerView.setHasFixedSize(true);
        accountListAdapter = new AccountListAdapter();
        accountRecyclerView.setAdapter(accountListAdapter);
        loadChains();
    }

    private void loadChains() {
        ArrayList<BaseChain> displayChains = getBaseDao().dpSortedChains();
        expandedChains = getBaseDao().getExpandedChains();

        final BaseChain baseChain = getBaseChain();

        for (BaseChain chain : displayChains) {
            if (expandedChains.contains(chain) || baseChain.equals(chain)) {
                chainAccounts.add(new ChainAccounts(true, chain, getBaseDao().getAccountsByChain(chain)));
            } else {
                chainAccounts.add(new ChainAccounts(false, chain, getBaseDao().getAccountsByChain(chain)));
            }
        }
        accountRecyclerView.scrollToPosition(getBaseDao().dpSortedChains().indexOf(baseChain));
        accountListAdapter.notifyDataSetChanged();
    }

    public void onChangeWallet(Long walletId) {
        Disposable disposable = accountsInteractor
                .selectAccount(walletId)
                .subscribeOn(AppSchedulers.INSTANCE.io())
                .observeOn(AppSchedulers.INSTANCE.ui())
                .doOnError(error -> WLog.e(error.toString()))
                .subscribe(
                        () -> {
                            saveExpandedChains();
                            finish();
                        },
                        error -> Toast.makeText(getBaseContext(), R.string.str_unknown_error_msg, Toast.LENGTH_SHORT).show()
                );
        compositeDisposable.add(disposable);
    }

    private void saveExpandedChains() {
        expandedChains.clear();
        for (ChainAccounts chainAccounts : chainAccounts) {
            if (chainAccounts.opened) {
                expandedChains.add(chainAccounts.baseChain);
            }
        }
        getBaseDao().setExpandedChains(expandedChains);
    }

    private class AccountListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new ManageChainSwitchHolder(getLayoutInflater().inflate(R.layout.item_account_list, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            final ManageChainSwitchHolder holder = (ManageChainSwitchHolder) viewHolder;
            holder.onBindChainSwitch(WalletSwitchActivity.this, chainAccounts.get(position), getAccount());
        }

        @Override
        public int getItemCount() {
            return chainAccounts.size();
        }
    }
}
