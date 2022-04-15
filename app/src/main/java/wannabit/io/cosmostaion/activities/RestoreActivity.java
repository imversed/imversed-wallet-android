package wannabit.io.cosmostaion.activities;

import static wannabit.io.cosmostaion.base.BaseChain.FETCHAI_MAIN;
import static wannabit.io.cosmostaion.base.BaseChain.KAVA_MAIN;
import static wannabit.io.cosmostaion.base.BaseChain.LUM_MAIN;
import static wannabit.io.cosmostaion.base.BaseChain.OKEX_MAIN;
import static wannabit.io.cosmostaion.base.BaseChain.SECRET_MAIN;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fulldive.wallet.presentation.accounts.AddAccountDialogFragment;
import com.fulldive.wallet.presentation.chains.choicenet.ChoiceChainDialogFragment;
import com.fulldive.wallet.presentation.security.CheckPasswordActivity;
import com.fulldive.wallet.presentation.security.SetPasswordActivity;

import org.bitcoinj.crypto.MnemonicCode;

import java.util.ArrayList;
import java.util.Arrays;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.base.BaseChain;
import wannabit.io.cosmostaion.crypto.CryptoHelper;
import wannabit.io.cosmostaion.dialog.Dialog_FetchRestorePath;
import wannabit.io.cosmostaion.dialog.Dialog_KavaRestorePath;
import wannabit.io.cosmostaion.dialog.Dialog_LumRestorePath;
import wannabit.io.cosmostaion.dialog.Dialog_OkexRestoreType;
import wannabit.io.cosmostaion.dialog.Dialog_SecretRestorePath;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.utils.WKey;
import wannabit.io.cosmostaion.utils.WUtil;

public class RestoreActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout contentsLayout;
    private Button pasteButton, confirmButton;
    private final Button[] alphabetBtns = new Button[26];
    private ImageButton deleteButton;
    private Button nextButton;
    private ImageView chainImageView;
    private TextView clearAllButton, wordsCountView;

    private final EditText[] mnemonicsEditText = new EditText[24];
    private int mnemonicPosition = 0;

    private BaseChain chain;
    private ArrayList<String> allMnemonic;
    private MnemonicAdapter mnemonicAdapter;
    private final ArrayList<String> words = new ArrayList<>();

    private int isCustomPath;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    showRestorePathActivity();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_restore);
        Toolbar toolbar = findViewById(R.id.toolbar);
        contentsLayout = findViewById(R.id.contentsLayer);
        pasteButton = findViewById(R.id.pasteButton);
        confirmButton = findViewById(R.id.confirmButton);
        deleteButton = findViewById(R.id.password_back);
        nextButton = findViewById(R.id.nextButton);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        chainImageView = findViewById(R.id.chainImg);
        clearAllButton = findViewById(R.id.toolbar_clear);
        wordsCountView = findViewById(R.id.words_cnt);

        pasteButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        clearAllButton.setOnClickListener(this);


        if (getIntent().getStringExtra("chain") != null) {
            chain = BaseChain.getChain(getIntent().getStringExtra("chain"));
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        allMnemonic = new ArrayList<>(MnemonicCode.INSTANCE.getWordList());
        final String packageName = getPackageName();
        for (int i = 0; i < alphabetBtns.length; i++) {
            alphabetBtns[i] = findViewById(getResources().getIdentifier("password_char" + i, "id", packageName));
            alphabetBtns[i].setOnClickListener(this);
        }

        for (int i = 0; i < mnemonicsEditText.length; i++) {
            final int position = i;
            mnemonicsEditText[i] = findViewById(getResources().getIdentifier("tv_mnemonic_" + i, "id", packageName));
            mnemonicsEditText[i].setShowSoftInputOnFocus(false);
            mnemonicsEditText[i].setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    mnemonicPosition = position;
                }
            });
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        mnemonicAdapter = new MnemonicAdapter();
        recyclerView.setAdapter(mnemonicAdapter);

        onCheckMnemonicCnt();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (chain == null) {
            ChoiceChainDialogFragment dialog = ChoiceChainDialogFragment.Companion.newInstance(false);
            showDialog(dialog, "dialog", false);
        } else {
            onUpdateView();
        }
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
        chainImageView.setColorFilter(WDp.getChainColor(getBaseContext(), chain), android.graphics.PorterDuff.Mode.SRC_IN);
        contentsLayout.setVisibility(View.VISIBLE);
        mnemonicsEditText[0].requestFocus();
    }

    private void onClearAll() {
        for (EditText editText : mnemonicsEditText) {
            editText.setText("");
        }
        mnemonicsEditText[0].requestFocus();
        onBeforeWord();
        onCheckMnemonicCnt();
    }

    private void onBeforeWord() {
        if (mnemonicPosition > 0) {
            mnemonicsEditText[mnemonicPosition - 1].requestFocus();
            mnemonicsEditText[mnemonicPosition].setSelection(mnemonicsEditText[mnemonicPosition].getText().length());
        }
        mnemonicAdapter.getFilter().filter(mnemonicsEditText[mnemonicPosition].getText().toString().trim());
        onCheckMnemonicCnt();
    }

    private void onNextWord() {
        if (mnemonicPosition < 23) {
            mnemonicsEditText[mnemonicPosition + 1].requestFocus();
        }
        mnemonicAdapter.getFilter().filter(mnemonicsEditText[mnemonicPosition].getText().toString().trim());
        onCheckMnemonicCnt();
    }

    private void onCheckMnemonicCnt() {
        words.clear();
        for (int i = 0; i < mnemonicsEditText.length; i++) {
            if (!TextUtils.isEmpty(mnemonicsEditText[i].getText().toString().trim())) {
                words.add(mnemonicsEditText[i].getText().toString().trim());
            } else {
                break;
            }
        }

        wordsCountView.setText(getString(R.string.str_words_count, words.size()));
        if ((words.size() == 12 || words.size() == 16 || words.size() == 24) &&
                WKey.isMnemonicWords(words)) {
            wordsCountView.setTextColor(WDp.getChainColor(getBaseContext(), chain));
        } else {
            wordsCountView.setTextColor(ContextCompat.getColor(this, R.color.colorRed));
        }
    }

    private boolean isValidWords() {
        return (words.size() == 12 || words.size() == 16 || words.size() == 24) &&
                WKey.isMnemonicWords(words) && WKey.isValidStringHdSeedFromWords(words);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(clearAllButton)) {
            onClearAll();
            return;

        } else if (v.equals(pasteButton)) {
            onClearAll();

            if (getBaseDao().mCopySalt != null && getBaseDao().mCopyEncResult != null) {
                String words = CryptoHelper.doDecryptData(getBaseDao().mCopySalt, getBaseDao().mCopyEncResult.getEncDataString(), getBaseDao().mCopyEncResult.getIvDataString());
                if (TextUtils.isEmpty(words)) {
                    return;
                }
                ArrayList<String> newinsert = new ArrayList<>(Arrays.asList(words.split("\\s+")));
                for (int i = 0; i < mnemonicsEditText.length; i++) {
                    if (newinsert.size() > i) {
                        String toinsert = newinsert.get(i).replace(" ", "");
                        mnemonicsEditText[i].setText(toinsert);
                    }
                }
                if (newinsert.size() < 23) {
                    mnemonicsEditText[newinsert.size()].requestFocus();
                } else {
                    mnemonicsEditText[23].requestFocus();
                }
                mnemonicsEditText[mnemonicPosition].setSelection(mnemonicsEditText[mnemonicPosition].getText().length());
                mnemonicAdapter.getFilter().filter(mnemonicsEditText[mnemonicPosition].getText().toString().trim());
                onCheckMnemonicCnt();

            } else {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard.getPrimaryClip() != null && clipboard.getPrimaryClip().getItemCount() > 0) {
                    String userPaste = clipboard.getPrimaryClip().getItemAt(0).coerceToText(getBaseContext()).toString().trim();
                    if (TextUtils.isEmpty(userPaste)) {
                        Toast.makeText(this, R.string.error_clipboard_no_data, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ArrayList<String> newinsert = new ArrayList<>(Arrays.asList(userPaste.split("\\s+")));
                    for (int i = 0; i < mnemonicsEditText.length; i++) {
                        if (newinsert.size() > i) {
                            String toinsert = newinsert.get(i).replace(" ", "");
                            mnemonicsEditText[i].setText(toinsert);
                        }
                    }
                    if (newinsert.size() < 23) {
                        mnemonicsEditText[newinsert.size()].requestFocus();
                    } else {
                        mnemonicsEditText[23].requestFocus();
                    }
                    mnemonicsEditText[mnemonicPosition].setSelection(mnemonicsEditText[mnemonicPosition].getText().length());
                    mnemonicAdapter.getFilter().filter(mnemonicsEditText[mnemonicPosition].getText().toString().trim());
                    onCheckMnemonicCnt();

                } else {
                    Toast.makeText(this, R.string.error_clipboard_no_data, Toast.LENGTH_SHORT).show();
                }
            }
            return;

        } else if (v.equals(confirmButton)) {
            words.clear();
            for (EditText editText : mnemonicsEditText) {
                final String text = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    words.add(text);
                } else {
                    break;
                }
            }

            if (isValidWords()) {
                if (chain.equals(KAVA_MAIN)) {
                    Dialog_KavaRestorePath dialog = Dialog_KavaRestorePath.newInstance(null);
                    showDialog(dialog, "dialog", false);
                    return;

                } else if (chain.equals(SECRET_MAIN)) {
                    Dialog_SecretRestorePath dialog = Dialog_SecretRestorePath.newInstance(null);
                    showDialog(dialog, "dialog", false);
                    return;

                } else if (chain.equals(OKEX_MAIN)) {
                    Dialog_OkexRestoreType dialog = Dialog_OkexRestoreType.newInstance(null);
                    showDialog(dialog, "dialog", false);
                    return;

                } else if (chain.equals(FETCHAI_MAIN)) {
                    Dialog_FetchRestorePath dialog = Dialog_FetchRestorePath.newInstance(null);
                    showDialog(dialog, "dialog", false);
                    return;

                } else if (chain.equals(LUM_MAIN)) {
                    Dialog_LumRestorePath dialog = Dialog_LumRestorePath.newInstance(null);
                    showDialog(dialog, "dialog", false);
                    return;

                } else {
                    onConfirmedWords();
                }

            } else {
                Toast.makeText(this, R.string.error_invalid_mnemonic_count, Toast.LENGTH_SHORT).show();
            }
            return;

        } else if (v.equals(deleteButton)) {
            String existed = mnemonicsEditText[mnemonicPosition].getText().toString().trim();
            if (TextUtils.isEmpty(existed)) {
                onBeforeWord();
            } else {
                mnemonicsEditText[mnemonicPosition].setText(existed.substring(0, existed.length() - 1));
                mnemonicsEditText[mnemonicPosition].setSelection(mnemonicsEditText[mnemonicPosition].getText().length());
            }
            mnemonicAdapter.getFilter().filter(mnemonicsEditText[mnemonicPosition].getText().toString().trim());
            onCheckMnemonicCnt();
            return;

        } else if (v.equals(nextButton)) {
            onNextWord();
            return;

        }
        if (v instanceof Button) {
            String input = ((Button) v).getText().toString();
            mnemonicsEditText[mnemonicPosition].setText(mnemonicsEditText[mnemonicPosition].getText().toString() + input);
            mnemonicsEditText[mnemonicPosition].setSelection(mnemonicsEditText[mnemonicPosition].getText().length());
            mnemonicAdapter.getFilter().filter(mnemonicsEditText[mnemonicPosition].getText().toString().trim());

        }
    }

    private void onConfirmedWords() {
        Class<?> cls;
        if (!getBaseDao().onHasPassword()) {
            cls = SetPasswordActivity.class;
        } else {
            cls = CheckPasswordActivity.class;
        }

        launcher.launch(
                new Intent(RestoreActivity.this, cls),
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_bottom, R.anim.fade_out)
        );
    }

    public void onUsingCustomPath(int using) {
        isCustomPath = using;
        onConfirmedWords();
    }

    private void showRestorePathActivity() {
        Intent intent = new Intent(RestoreActivity.this, RestorePathActivity.class);
        intent.putExtra("HDseed", WKey.getStringHdSeedFromWords(words));
        intent.putExtra("entropy", WUtil.byteArrayToHexString(WKey.toEntropy(words)));
        intent.putExtra("size", words.size());
        intent.putExtra("chain", chain.getChain());
        intent.putExtra("customPath", isCustomPath);
        startActivity(intent);
    }

    public void onChoiceNet(BaseChain chain) {
        this.chain = chain;
        onUpdateView();
    }

    public void onChainSelected(BaseChain baseChain) {
        if (getBaseDao().getAccountsByChain(baseChain).size() >= 5) {
            Toast.makeText(this, R.string.error_max_account_number, Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(() -> {
            showDialog(AddAccountDialogFragment.Companion.newInstance(baseChain.getChain()));
        }, 300);
    }

    public class MnemonicAdapter extends RecyclerView.Adapter<MnemonicAdapter.MnemonicHolder> implements Filterable {

        private ArrayList<String> mFilteredMnemonic = new ArrayList<>();

        @NonNull
        @Override
        public MnemonicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MnemonicHolder(getLayoutInflater().inflate(R.layout.item_suggest_menmonic, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MnemonicHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.itemMnemonic.setText(mFilteredMnemonic.get(position));
            holder.itemRoot.setOnClickListener(v -> {
                mnemonicsEditText[mnemonicPosition].setText(mFilteredMnemonic.get(position));
                onNextWord();
            });
        }

        @Override
        public int getItemCount() {
            return mFilteredMnemonic.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    String charString = constraint.toString();
                    if (charString.isEmpty()) {
                        mFilteredMnemonic.clear();

                    } else {
                        ArrayList<String> filteredList = new ArrayList<>();
                        for (String word : allMnemonic) {
                            if (word.startsWith(charString.toLowerCase()) && !word.equals(charString.toLowerCase())) {
                                filteredList.add(word);
                            }
                        }
                        mFilteredMnemonic = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredMnemonic;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mFilteredMnemonic = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public class MnemonicHolder extends RecyclerView.ViewHolder {
            RelativeLayout itemRoot;
            TextView itemMnemonic;

            public MnemonicHolder(View v) {
                super(v);
                itemRoot = itemView.findViewById(R.id.root_mnemonic);
                itemMnemonic = itemView.findViewById(R.id.tv_mnemonic);
            }
        }
    }
}
