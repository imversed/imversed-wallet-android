package wannabit.io.cosmostaion.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.fulldive.wallet.di.EnrichableLifecycleCallbacks;
import com.fulldive.wallet.di.IInjectorHolder;
import com.fulldive.wallet.di.components.ApplicationComponent;
import com.fulldive.wallet.interactors.accounts.AccountsInteractor;
import com.fulldive.wallet.interactors.secret.SecretInteractor;
import com.fulldive.wallet.interactors.settings.SettingsInteractor;
import com.joom.lightsaber.Injector;
import com.joom.lightsaber.Lightsaber;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class BaseApplication extends Application implements IInjectorHolder {

    private Injector appInjector;

    private BaseData mBaseData;
    private AppStatus mAppStatus;
    private SecretInteractor secretInteractor;
    private AccountsInteractor accountsInteractor;
    private SettingsInteractor settingsInteractor;

    @Override
    public void onCreate() {
        super.onCreate();
        appInjector = new Lightsaber.Builder().build().createInjector(new ApplicationComponent(getApplicationContext()));

        secretInteractor = appInjector.getInstance(SecretInteractor.class);
        accountsInteractor = appInjector.getInstance(AccountsInteractor.class);
        settingsInteractor = appInjector.getInstance(SettingsInteractor.class);

        registerActivityLifecycleCallbacks(new LifecycleCallbacks());
        registerActivityLifecycleCallbacks(new EnrichableLifecycleCallbacks(this));

        Picasso.Builder builder = new Picasso.Builder(this);
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        Picasso.setSingletonInstance(built);

        getBaseDao().mCopySalt = UUID.randomUUID().toString();
    }

    public BaseData getBaseDao() {
        if (mBaseData == null)
            mBaseData = getInjector().getInstance(BaseData.class);
        return mBaseData;
    }

    public boolean isReturnedForeground() {
        return mAppStatus.ordinal() == AppStatus.RETURNED_TO_FOREGROUND.ordinal();
    }

    public boolean needShowLockScreen() {
        if (!isReturnedForeground() ||
                !secretInteractor.hasPassword().blockingGet() ||    // TODO: it will be refactored
                !settingsInteractor.getAppLockEnabled() ||
                (accountsInteractor.getAccounts().blockingGet().size() <= 0)) return false;

        final long interval = getAppLockInterval();
        return interval == 0L || (settingsInteractor.getLastActivityTime() + interval) < System.currentTimeMillis();
    }

    @NonNull
    @Override
    public Injector getInjector() {
        return appInjector;
    }

    public enum AppStatus {
        BACKGROUND,
        RETURNED_TO_FOREGROUND,
        FOREGROUND
    }

    public class LifecycleCallbacks implements ActivityLifecycleCallbacks {

        private int running = 0;

        @Override
        public void onActivityStarted(Activity activity) {
            if (++running == 1) {
                mAppStatus = AppStatus.RETURNED_TO_FOREGROUND;
                getBaseDao().mCopySalt = UUID.randomUUID().toString();
            } else if (running > 1) {
                mAppStatus = AppStatus.FOREGROUND;
            }

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (--running == 0) {
                mAppStatus = AppStatus.BACKGROUND;
                getBaseDao().mCopySalt = null;
                getBaseDao().mCopyEncResult = null;
            }
            if (!(activity instanceof ITimelessActivity)) {
                settingsInteractor.updateLastActivityTime();
            }
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }


        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }

    public long getAppLockInterval() {
        switch (settingsInteractor.getAppLockInterval()) {
            case 1:
                return 10000L;
            case 2:
                return 30000L;
            case 3:
                return 60000L;
            default:
                return 0L;
        }
    }
}
