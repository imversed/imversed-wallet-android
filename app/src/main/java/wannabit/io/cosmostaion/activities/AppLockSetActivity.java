package wannabit.io.cosmostaion.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import com.fulldive.wallet.extensions.ContextExtensionsKt;
import com.fulldive.wallet.interactors.secret.SecretInteractor;
import com.fulldive.wallet.presentation.security.password.CheckPasswordActivity;
import com.fulldive.wallet.rx.AppSchedulers;

import io.reactivex.disposables.Disposable;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.dialog.Dialog_LockTime;
import wannabit.io.cosmostaion.utils.WLog;

public class AppLockSetActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout mBtnUsingAppLock, mBtnUsingFingerprint, mBtnAppLockTime;
    private SwitchCompat mSwitchUsingAppLock, mSwitchUsingFingerprint;
    private TextView mTvAppLockTime;
    private SecretInteractor secretInteractor;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            getBaseDao().setUsingAppLock(false);
        }
        onUpdateView();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock_set);
        secretInteractor = getAppInjector().getInstance(SecretInteractor.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        mBtnUsingAppLock = findViewById(R.id.card_using_applock);
        mBtnUsingFingerprint = findViewById(R.id.card_using_fingerprint);
        mBtnAppLockTime = findViewById(R.id.card_applock_time);
        mSwitchUsingAppLock = findViewById(R.id.switch_using_applock);
        mSwitchUsingFingerprint = findViewById(R.id.switch_fingerprint);
        mTvAppLockTime = findViewById(R.id.applock_time_text);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBtnUsingAppLock.setOnClickListener(this);
        mBtnUsingFingerprint.setOnClickListener(this);
        mBtnAppLockTime.setOnClickListener(this);

        onUpdateView();
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
        mSwitchUsingAppLock.setChecked(getBaseDao().getUsingAppLock());
        mSwitchUsingFingerprint.setChecked(getBaseDao().getUsingFingerPrint());
        mTvAppLockTime.setText(getBaseDao().getAppLockLeaveTimeString(getBaseContext()));
        if (getBaseDao().getUsingAppLock()) {
            mBtnAppLockTime.setVisibility(View.VISIBLE);
            FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(this);
            mBtnUsingFingerprint.setVisibility(
                    fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints() ? View.VISIBLE : View.GONE
            );

        } else {
            mBtnAppLockTime.setVisibility(View.GONE);
            mBtnUsingFingerprint.setVisibility(View.GONE);
        }
    }

    public void onUpdateLockTime(int time) {
        getBaseDao().setAppLockTriggerTime(time);
        onUpdateView();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBtnUsingAppLock)) {
            if (getBaseDao().getUsingAppLock()) {
                launcher.launch(
                        new Intent(AppLockSetActivity.this, CheckPasswordActivity.class),
                        ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_bottom, R.anim.fade_out)
                );

            } else {
                Disposable disposable = secretInteractor
                        .hasPassword()
                        .subscribeOn(AppSchedulers.INSTANCE.io())
                        .observeOn(AppSchedulers.INSTANCE.ui())
                        .doOnError(error -> WLog.e(error.toString()))
                        .subscribe(
                                hasPassword -> {
                                    if (hasPassword) {
                                        getBaseDao().setUsingAppLock(true);
                                        onUpdateView();
                                    } else {
                                        ContextExtensionsKt.toast(getBaseContext(), R.string.error_no_password);
                                    }
                                },
                                error -> ContextExtensionsKt.toast(getBaseContext(), R.string.str_unknown_error_msg)
                        );
                compositeDisposable.add(disposable);
            }

        } else if (v.equals(mBtnUsingFingerprint)) {
            getBaseDao().setUsingFingerPrint(!getBaseDao().getUsingFingerPrint());
            onUpdateView();

        } else if (v.equals(mBtnAppLockTime)) {
            Dialog_LockTime timeUpdate = Dialog_LockTime.newInstance();
            showDialog(timeUpdate);
        }
    }
}
