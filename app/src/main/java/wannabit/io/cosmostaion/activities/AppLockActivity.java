package wannabit.io.cosmostaion.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import com.fulldive.wallet.interactors.secret.SecretInteractor;
import com.fulldive.wallet.presentation.system.keyboard.KeyboardListener;
import com.fulldive.wallet.presentation.system.keyboard.KeyboardPagerAdapter;
import com.fulldive.wallet.rx.AppSchedulers;

import io.reactivex.disposables.Disposable;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseActivity;
import wannabit.io.cosmostaion.base.ITimelessActivity;
import wannabit.io.cosmostaion.utils.WLog;
import wannabit.io.cosmostaion.widget.LockedViewPager;

public class AppLockActivity extends BaseActivity implements ITimelessActivity, KeyboardListener {

    private LinearLayout layerContents;
    private ImageView fingerImage;
    private TextView unlockMsg;
    private final ImageView[] ivCircle = new ImageView[5];

    private LockedViewPager viewPager;
    private String userInput = "";

    private CancellationSignal cancellationSignal;

    private SecretInteractor secretInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock);

        secretInteractor = getAppInjector().getInstance(SecretInteractor.class);

        layerContents = findViewById(R.id.layerContents);
        fingerImage = findViewById(R.id.img_fingerprint);
        unlockMsg = findViewById(R.id.tv_unlock_msg);
        viewPager = findViewById(R.id.keyboardPager);

        for (int i = 0; i < ivCircle.length; i++) {
            ivCircle[i] = findViewById(getResources().getIdentifier("circleImage" + i, "id", getPackageName()));
        }

        viewPager.setOffscreenPageLimit(2);
        KeyboardPagerAdapter mAdapter = new KeyboardPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFingerImageColor(R.color.colorWhite);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        onInitView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cancellationSignal != null)
            cancellationSignal.cancel();
    }

    @Override
    public void onBackPressed() {
        if (cancellationSignal != null)
            cancellationSignal.cancel();
        setResult(RESULT_CANCELED);
        moveTaskToBack(true);
    }

    @Override
    public void userInsertKey(char input) {
        if (userInput == null || userInput.length() == 0) {
            userInput = String.valueOf(input);

        } else if (userInput.length() < 5) {
            userInput = userInput + input;
        }

        if (userInput.length() == 4) {
            viewPager.setCurrentItem(1, true);

        } else if (userInput.length() == 5 && secretInteractor.isPasswordValid(userInput)) {
            onFinishInput();

        } else if (userInput.length() == 5) {
            onInitView();
            return;
        }
        onUpdateCnt();
    }

    @Override
    public void userDeleteKey() {
        if (userInput == null || userInput.length() <= 0) {
            onBackPressed();
        } else if (userInput.length() == 4) {
            userInput = userInput.substring(0, userInput.length() - 1);
            viewPager.setCurrentItem(0, true);
        } else {
            userInput = userInput.substring(0, userInput.length() - 1);
        }
        onUpdateCnt();
    }

    private void onInitView() {
        userInput = "";
        for (ImageView imageView : ivCircle) {
            imageView.setBackgroundResource(R.drawable.ic_pass_gr);
        }
        viewPager.setCurrentItem(0, true);
        onCheckFingerPrint();
    }

    private void onUnlock() {
        if (cancellationSignal != null)
            cancellationSignal.cancel();
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void onCheckFingerPrint() {
        FingerprintManagerCompat fingerprintManagerCompat = FingerprintManagerCompat.from(this);
        cancellationSignal = new CancellationSignal();
        if (fingerprintManagerCompat.isHardwareDetected() && fingerprintManagerCompat.hasEnrolledFingerprints() && settingsInteractor.getFingerprintEnabled()) {
            fingerImage.setVisibility(View.VISIBLE);
            unlockMsg.setText(R.string.str_app_unlock_msg2);

            fingerprintManagerCompat.authenticate(null, 0, cancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    super.onAuthenticationError(errMsgId, errString);
                }

                @Override
                public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                    unlockMsg.setText(helpString);
                    setFingerImageColor(R.color.colorRed);
                    super.onAuthenticationHelp(helpMsgId, helpString);
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    setFingerImageColor(R.color.colorAtom);
                    onUnlock();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }


            }, null);

        } else {
            fingerImage.setVisibility(View.GONE);
            unlockMsg.setText(R.string.str_app_unlock_msg1);
        }
    }

    private void onFinishInput() {
        showWaitDialog();
        Disposable disposable = secretInteractor
                .checkPassword(userInput)
                .subscribeOn(AppSchedulers.INSTANCE.io())
                .observeOn(AppSchedulers.INSTANCE.ui())
                .doOnError(error -> WLog.e(error.toString()))
                .doAfterTerminate(this::hideWaitDialog)
                .subscribe(
                        isCorrect -> {
                            if (isCorrect) {
                                onUnlock();
                            } else {
                                onShakeView();
                                onInitView();
                                Toast.makeText(getBaseContext(), R.string.error_invalid_password, Toast.LENGTH_SHORT).show();
                            }
                        },
                        error -> {
                            onShakeView();
                            onInitView();
                            Toast.makeText(getBaseContext(), R.string.str_unknown_error_msg, Toast.LENGTH_SHORT).show();
                        }
                );
        compositeDisposable.add(disposable);
    }

    private void onUpdateCnt() {
        if (userInput == null)
            userInput = "";

        final int inputLength = userInput.length();
        for (int i = 0; i < ivCircle.length; i++) {
            ivCircle[i].setBackgroundResource((i < inputLength) ? R.drawable.ic_pass_pu : R.drawable.ic_pass_gr);
        }
    }

    private void setFingerImageColor(int colorResId) {
        fingerImage.setColorFilter(ContextCompat.getColor(fingerImage.getContext(), colorResId), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void onShakeView() {
        layerContents.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        animation.reset();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onInitView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        layerContents.startAnimation(animation);
    }
}
