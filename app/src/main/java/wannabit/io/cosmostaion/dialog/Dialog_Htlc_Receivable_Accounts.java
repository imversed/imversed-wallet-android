package wannabit.io.cosmostaion.dialog;

import static wannabit.io.cosmostaion.base.BaseConstant.FEE_BNB_SEND;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fulldive.wallet.interactors.accounts.AccountsInteractor;
import com.fulldive.wallet.models.BaseChain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.dao.Balance;
import wannabit.io.cosmostaion.utils.WDp;

public class Dialog_Htlc_Receivable_Accounts extends DialogFragment {

    private RecyclerView mRecyclerView;
    private AccountListAdapter mAccountListAdapter;
    protected AccountsInteractor accountsInteractor;

    private ArrayList<Account> mAccounts = new ArrayList<>();

    public static Dialog_Htlc_Receivable_Accounts newInstance(Bundle bundle) {
        Dialog_Htlc_Receivable_Accounts frag = new Dialog_Htlc_Receivable_Accounts();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        accountsInteractor = getSActivity().getAppInjector().getInstance(AccountsInteractor.class);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_htlc_receivable_accouts, null);
        mRecyclerView = view.findViewById(R.id.recycler);
        mAccounts = onSelectAccountsByHtlcClaim(BaseChain.getChain(getArguments().getString("chainName")));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mAccountListAdapter = new AccountListAdapter();
        mRecyclerView.setAdapter(mAccountListAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }


    private class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.AccountHolder> {

        @NonNull
        @Override
        public AccountHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new AccountHolder(getLayoutInflater().inflate(R.layout.item_dialog_accountlist_account, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AccountHolder holder, int position) {
            final Account account = mAccounts.get(position);
            final BaseChain baseChain = BaseChain.getChain(account.baseChain);
            holder.accountKeyState.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorGray0), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.accountAddress.setText(account.address);
            holder.accountName.setText(account.getAccountTitle(requireContext()));

            if (baseChain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
                if (account.hasPrivateKey) {
                    holder.accountKeyState.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorBnb), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                WDp.showCoinDp(getContext(), getSActivity().getBaseDao(), baseChain.getMainDenom(), account.getTokenBalance(baseChain.getMainDenom()).toPlainString(), holder.accountDenom, holder.accountAvailable, baseChain);

            } else if (baseChain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
                if (account.hasPrivateKey) {
                    holder.accountKeyState.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorKava), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                WDp.showCoinDp(getContext(), getSActivity().getBaseDao(), baseChain.getMainDenom(), account.getTokenBalance(baseChain.getMainDenom()).toPlainString(), holder.accountDenom, holder.accountAvailable, baseChain);
            }

            holder.rootLayer.setOnClickListener(v -> {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("position", position);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, resultIntent);
                dismiss();
            });

        }

        @Override
        public int getItemCount() {
            return mAccounts.size();
        }


        public class AccountHolder extends RecyclerView.ViewHolder {
            RelativeLayout rootLayer;
            ImageView accountKeyState;
            TextView accountName, accountAddress, accountAvailable, accountDenom;

            public AccountHolder(@NonNull View itemView) {
                super(itemView);
                rootLayer = itemView.findViewById(R.id.rootLayer);
                accountKeyState = itemView.findViewById(R.id.accountKeyState);
                accountName = itemView.findViewById(R.id.accountName);
                accountAddress = itemView.findViewById(R.id.accountAddress);
                accountAvailable = itemView.findViewById(R.id.accountAvailable);
                accountDenom = itemView.findViewById(R.id.accountDenom);
            }
        }
    }

    private ArrayList<Account> onSelectAccountsByHtlcClaim(BaseChain chain) {
        ArrayList<Account> result = new ArrayList<>();
        List<Account> AllAccount = accountsInteractor.getAccounts().blockingGet();
        for (Account account : AllAccount) {
            if (BaseChain.getChain(account.baseChain).equals(chain) && account.hasPrivateKey) {
                if (chain.equals(BaseChain.BNB_MAIN.INSTANCE)) {
                    if (getTokenAmount(account.balances, chain.getMainDenom()).compareTo(new BigDecimal(FEE_BNB_SEND)) >= 0) {
                        result.add(account);
                    }
                } else if (chain.equals(BaseChain.KAVA_MAIN.INSTANCE)) {
                    if (getTokenAmount(account.balances, chain.getMainDenom()).compareTo(new BigDecimal("12500")) >= 0) {
                        result.add(account);
                    }
                }
            }
        }

        return result;
    }

    public BigDecimal getTokenAmount(List<Balance> balances, String symbol) {
        BigDecimal result = BigDecimal.ZERO;
        if (balances != null) {
            for (Balance balance : balances) {
                if (balance.symbol.equalsIgnoreCase(symbol)) {
                    result = balance.balance;
                }
            }
        }
        return result;
    }

    private BaseActivity getSActivity() {
        return (BaseActivity) getActivity();
    }
}
