package com.fulldive.wallet.presentation.security.mnemonic;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.fulldive.wallet.presentation.security.mnemonic.layout.MnemonicLayout;

import java.util.ArrayList;
import java.util.List;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.base.BaseChain;
import wannabit.io.cosmostaion.crypto.CryptoHelper;
import wannabit.io.cosmostaion.dao.Account;
import wannabit.io.cosmostaion.dialog.Dialog_Safe_Copy;
import wannabit.io.cosmostaion.utils.WKey;
import wannabit.io.cosmostaion.utils.WUtil;

public class MnemonicCheckActivity extends BaseActivity {

    private List<String> mnemonicWords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_mnemonic_check);
        Toolbar toolbar = findViewById(R.id.toolbar);
        MnemonicLayout mnemonicsLayout = findViewById(R.id.mnemonicsLayout);
        Button copyButton = findViewById(R.id.copyButton);
        Button okButton = findViewById(R.id.okButton);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String entropy = getIntent().getStringExtra("entropy");
        mnemonicWords = new ArrayList<>(WKey.getRandomMnemonic(WUtil.hexStringToByteArray(entropy)));

        mnemonicsLayout.performAttach();    // XXX
        Account toCheck = getBaseDao().getAccount("" + getIntent().getLongExtra("checkid", -1));
        final BaseChain chain = BaseChain.getChain(toCheck.baseChain);
        if (chain != null) {
            mnemonicsLayout.setChain(chain);
            mnemonicsLayout.setMnemonicWords(mnemonicWords);
        }

        copyButton.setOnClickListener(v -> {
            Dialog_Safe_Copy delete = Dialog_Safe_Copy.newInstance();
            showDialog(delete);
        });

        okButton.setOnClickListener(v -> startMainActivity(3));
    }

    public void onRawCopy() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        StringBuilder builder = new StringBuilder();
        for (String word : mnemonicWords) {
            if (builder.length() != 0)
                builder.append(" ");
            builder.append(word);
        }
        String data = builder.toString();
        ClipData clip = ClipData.newPlainText("my data", data);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getBaseContext(), R.string.str_copied, Toast.LENGTH_SHORT).show();

    }

    public void onSafeCopy() {
        StringBuilder builder = new StringBuilder();
        for (String word : mnemonicWords) {
            if (builder.length() != 0)
                builder.append(" ");
            builder.append(word);
        }
        String data = builder.toString();
        getBaseDao().mCopyEncResult = CryptoHelper.doEncryptData(getBaseDao().mCopySalt, data, false);
        Toast.makeText(getBaseContext(), R.string.str_safe_copied, Toast.LENGTH_SHORT).show();

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
}
