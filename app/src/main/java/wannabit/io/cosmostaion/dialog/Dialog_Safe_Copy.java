package wannabit.io.cosmostaion.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.fulldive.wallet.presentation.security.mnemonic.ShowMnemonicActivity;

import wannabit.io.cosmostaion.R;

public class Dialog_Safe_Copy extends DialogFragment {

    public static Dialog_Safe_Copy newInstance() {
        return new Dialog_Safe_Copy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_safe_copy, null);
        Button btn_negative = view.findViewById(R.id.negativeButton);
        Button btn_positive = view.findViewById(R.id.positiveButton);

        btn_negative.setOnClickListener(v -> {
            ((ShowMnemonicActivity) getActivity()).onRawCopy();
            dismiss();
        });

        btn_positive.setOnClickListener(v -> {
            ((ShowMnemonicActivity) getActivity()).onSafeCopy();
            dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }


}